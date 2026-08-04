---
title: Tutorial
date: 2026-08-02
menu:
  main:
    parent: Installation
categories:
  - Installation
tags:
  - tutorial
---

A schema, three statements, and a test that runs them against a real PostgreSQL. Half an hour, and
at the end you have the shape every `YoSQL` project has.

You need [Java 25](../) and a container runtime for Testcontainers. Everything else is Maven.

## 1. The build

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>tenants</artifactId>
  <version>1.0.0-SNAPSHOT</version>

  <properties>
    <maven.compiler.release>25</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <version>42.7.9</version>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>6.1.2</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>postgresql</artifactId>
      <version>1.21.3</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <version>2026.8.8</version>
        <executions>
          <execution>
            <goals>
              <goal>generate</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <repositories>
            <basePackageName>com.example.persistence</basePackageName>
          </repositories>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

Note what is **not** there: no `YoSQL` dependency. The plugin runs during the build and the code it
leaves behind needs only the JDK and your driver.

## 2. The schema

`YoSQL` does not manage schemas, so this is a statement like any other — it just happens to be DDL.
Put it in `src/main/yosql/schema/createSchema.sql`:

```sql
-- name: createTenantTable
-- returning: none
-- writesReturnUpdateCount: false
create table if not exists tenant (
    id         uuid         not null primary key,
    account_id uuid         not null,
    slug       varchar(64)  not null unique,
    created_at timestamp with time zone not null
)
```

`writesReturnUpdateCount: false` makes the method `void` — a row count means nothing for a
`create table`.

In a real project this belongs in Flyway or Liquibase. Here it keeps the tutorial to one tool.

## 3. The record

Result rows are read from **source**, so the record has to exist before the build runs.
`src/main/java/com/example/domain/Tenant.java`:

```java
package com.example.domain;

import java.time.Instant;
import java.util.UUID;

public record Tenant(UUID id, UUID accountId, String slug, Instant createdAt) {
}
```

A component reads the column its own name implies, `camelCase` as `snake_case` — `accountId` reads
`account_id`. Nothing configures that.

## 4. The statements

`src/main/yosql/tenant/tenants.sql`:

```sql
-- name: insertTenant
-- returning: none
insert into tenant (id, account_id, slug, created_at)
values (:id, :accountId, :slug, :createdAt)
;

-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id, account_id, slug, created_at
from tenant
where id = :id
;

-- name: findTenantsByAccount
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
select id, account_id, slug, created_at
from tenant
where account_id = :accountId
order by slug
;
```

Three things worth noticing:

- **Nothing names a parameter type.** `:id` and `:accountId` on the reads come from the components
  of the record they build; on the insert they come from the columns they are named after, because
  the `create table` in step 2 is a statement `YoSQL` reads like any other. Write the types out only
  where neither can answer.
- **The file is `tenants.sql` under `tenant/`, so all three land in `TenantRepository`.** The
  directory decides the repository, not the file.
- **Every name starts with `insert` or `find`.** A name matching none of the configured prefixes
  fails the build rather than generating nothing.

## 5. Generate

```shell
mvn generate-sources
```

Under `target/generated-sources/yosql` you now have `com/example/persistence/TenantRepository.java`
and `com/example/persistence/converter/ToTenantConverter.java`. Read them — that is the point of the
tool. The converter is one `resultSet.getX(...)` call per component and then the constructor, and
the repository is the JDBC you would have written.

Each statement generated **two** methods: one taking a `DataSource` connection and one taking a
`Connection` you supply. The second is what a transaction uses.

## 6. Run it

`src/test/java/com/example/TenantRepositoryTest.java`:

```java
package com.example;

import com.example.domain.Tenant;
import com.example.persistence.SchemaRepository;
import com.example.persistence.TenantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TenantRepositoryTest {

    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:18");

    private static DataSource dataSource;

    @BeforeAll
    static void startDatabase() {
        POSTGRES.start();
        final var source = new PGSimpleDataSource();
        source.setUrl(POSTGRES.getJdbcUrl());
        source.setUser(POSTGRES.getUsername());
        source.setPassword(POSTGRES.getPassword());
        dataSource = source;
        new SchemaRepository(dataSource).createTenantTable();
    }

    @Test
    void findsTheTenantItInserted() {
        final var tenants = new TenantRepository(dataSource);
        final var id = UUID.randomUUID();
        final var accountId = UUID.randomUUID();
        final var createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        tenants.insertTenant(id, accountId, "acme", createdAt);

        final var found = tenants.findTenant(id);
        assertTrue(found.isPresent());
        assertEquals("acme", found.orElseThrow().slug());
        assertEquals(accountId, found.orElseThrow().accountId());
    }

    @Test
    void readsBackEveryTenantOfAnAccount() {
        final var tenants = new TenantRepository(dataSource);
        final var accountId = UUID.randomUUID();

        tenants.insertTenant(UUID.randomUUID(), accountId, "beta", Instant.now());
        tenants.insertTenant(UUID.randomUUID(), accountId, "alpha", Instant.now());

        final var found = tenants.findTenantsByAccount(accountId);
        assertEquals(2, found.size());
        assertEquals("alpha", found.getFirst().slug());
    }

}
```

```shell
mvn test
```

No mocks and no fixtures. A generated repository takes a `DataSource` and nothing else, so a test
that constructs one is testing your SQL — which is the only part of this you wrote.

## 7. Break it on purpose

The most useful thing to learn about `YoSQL` is what it refuses. Try each of these and run
`mvn generate-sources`:

**Drop a column from the select.** Remove `created_at` from `findTenant` — the build fails saying no
selected column supplies `createdAt`.

**Add one nothing reads.** Add `, 1 as extra` — the build fails saying no component claims `extra`.

**Rename a parameter.** Change `:id` to `:tenantId` in `findTenant` — no component is called
`tenantId`, so the build fails saying no type is known for it.

**Rename the method to `fetchTenant`.** `fetch` is not a configured read prefix, so the build fails
rather than silently generating nothing.

Every one of those is a defect that an ORM or a string-concatenating DAO would have handed you at
run time, on whichever request hit it first.

## Where to go next

- [SQL files](../../sql/sql-files/) — everything the front matter accepts.
- [Cookbook](../../sql/cookbook/) — `IN` lists, pagination, optional filters, streaming, enums.
- [Transactions](../../sql/transactions/) — several statements, one connection.
- [Spring Boot](../../frameworks/spring-boot/) or [Quarkus](../../frameworks/quarkus/) — the same
  repositories as beans.
- [Configuration](../../configuration/) — everything you can change about the output.
