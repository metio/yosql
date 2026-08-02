---
title: Spring Boot
date: 2026-08-02
menu:
  main:
    parent: Frameworks
categories:
  - Frameworks
tags:
  - spring
  - spring-boot
---

Five minutes, and no `YoSQL` dependency on your runtime classpath at the end of it.

## The build

Add the plugin. Spring Boot's own configuration is untouched:

```xml
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
        <annotations>
          <repositoryAnnotations>org.springframework.stereotype.Repository</repositoryAnnotations>
        </annotations>
      </configuration>
    </plugin>
  </plugins>
</build>
```

`repositoryAnnotations` puts `@Repository` on every generated class, so component scanning finds them
and constructor injection gives each one the application's `DataSource`. No `@Bean` methods, no
configuration class.

## The statement and the record

```sql
-- src/main/yosql/tenant/findTenant.sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id, slug, created_at
from tenant
where id = :id
```

```java
package com.example.domain;

public record Tenant(UUID id, String slug, Instant createdAt) {
}
```

Build, and `com.example.persistence.TenantRepository` exists with an
`Optional<Tenant> findTenant(UUID)`.

## Using it

```java
@Service
public class TenantService {

    private final TenantRepository tenants;

    public TenantService(final TenantRepository tenants) {
        this.tenants = tenants;
    }

    public Optional<Tenant> find(final UUID id) {
        return tenants.findTenant(id);
    }

}
```

## Transactions

For `@Transactional` to reach the generated repositories, they need a `DataSource` that hands out
the transaction's connection. That is what `TransactionAwareDataSourceProxy` is:

```java
@Configuration
public class PersistenceConfiguration {

    @Bean
    @Primary
    public DataSource transactionAwareDataSource(
            @Qualifier("dataSource") final DataSource dataSource) {
        return new TransactionAwareDataSourceProxy(dataSource);
    }

}
```

With that in place, a `@Transactional` method that calls two repositories runs both statements on
one connection, in one transaction, and rolls both back together — while neither repository knows
anything about Spring.

Give the `DataSourceTransactionManager` the **underlying** `DataSource`, not the proxy. Spring
Boot's autoconfiguration builds the manager from the unwrapped bean, so the `@Qualifier` above
matters.

Where you want a unit of work that is deliberately *not* the ambient transaction, use the
[connection overloads](../../sql/transactions/) and pass a connection you opened yourself.

## Testing

A generated repository has one dependency, so a test needs a database and nothing else.
`@JdbcTest` with Testcontainers is the whole setup:

```java
@JdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TenantRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:18");

    @Autowired
    DataSource dataSource;

    @Test
    void findsTheTenantItInserted() {
        final var tenants = new TenantRepository(dataSource);
        final var id = UUID.randomUUID();

        tenants.insertTenant(id, "acme", Instant.now());

        assertThat(tenants.findTenant(id)).map(Tenant::slug).hasValue("acme");
    }
}
```

Constructing the repository directly is deliberate — it is a class with a constructor, and a test
that builds one is testing your SQL rather than your wiring.

## Substituting a repository

Generated classes are `final`, so mocking them is not on offer. Turn on
[generateInterfaces](../../configuration/repositories/generateinterfaces/) with an interface suffix,
depend on the interface, and substitute whatever you like:

```xml
<repositories>
  <generateInterfaces>true</generateInterfaces>
  <repositoryInterfaceSuffix>Api</repositoryInterfaceSuffix>
</repositories>
```

Consider whether you want to. A repository whose whole content is SQL is a poor thing to mock — a
test that fakes it asserts that your code calls a method, not that your query is right.

## Native images

Spring Boot's AOT processing and GraalVM need reflection hints for anything resolved by name.
Generated repositories resolve nothing by name, so they need none — no `@RegisterReflectionForBinding`
for your row types, no runtime hints class. That is not a claim about Spring; it is a property of the
generated code, and every `YoSQL` release compiles a generated repository into a native image and
runs it against a real database to check it.
