---
title: Quarkus
date: 2026-08-02
menu:
  main:
    parent: Frameworks
categories:
  - Frameworks
tags:
  - quarkus
  - native-image
---

Quarkus is where `YoSQL` has the most to offer, and the reason is native images: a persistence layer
that resolves nothing by name needs no reflection registration, so there is no class to hint, no
build that succeeds and then fails on the first query, and nothing to re-check when your schema
changes.

## The build

Add the plugin next to the Quarkus one:

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
          <repositoryAnnotations>jakarta.enterprise.context.ApplicationScoped</repositoryAnnotations>
        </annotations>
      </configuration>
    </plugin>
  </plugins>
</build>
```

Generated repositories become CDI beans, and the agroal `DataSource` Quarkus configures is injected
into each one. A repository has exactly one constructor, which Quarkus's ArC treats as the injection
point without being told. Under a strict CDI container that wants to be told, add it:

```xml
<annotations>
  <repositoryAnnotations>jakarta.enterprise.context.ApplicationScoped</repositoryAnnotations>
  <constructorAnnotations>jakarta.inject.Inject</constructorAnnotations>
</annotations>
```

You need `quarkus-jdbc-postgresql` (or your driver's extension) and `quarkus-agroal`. You do **not**
need `quarkus-hibernate-orm` — that is rather the point.

## Using it

```java
@ApplicationScoped
public class TenantService {

    private final TenantRepository tenants;

    public TenantService(final TenantRepository tenants) {
        this.tenants = tenants;
    }

    @Transactional
    public void rename(final UUID id, final String slug) {
        tenants.updateTenantSlug(slug, id);
    }

}
```

An injected `DataSource` under a JTA transaction already hands out the transaction's connection, so
`@Transactional` works with no proxy and no configuration. Where you want a unit of work outside the
ambient transaction, use the [connection overloads](../../sql/transactions/).

## Native images

```shell
./mvnw package -Dnative
```

That is the whole procedure. Nothing to add to `reflect-config.json`, no
`@RegisterForReflection` on your records, no `native-image.properties` entry. The generated converter
reads each column with a `resultSet.getX(...)` call the compiler resolved and calls a constructor
the compiler resolved, so the closed-world analysis sees all of it.

Compare that with an ORM, where entities, their proxies and their collections are all reached
reflectively and every one has to be registered — usually by an extension that knows how, which is
why the extension has to exist.

Startup follows: there is no mapping metadata to read at boot, no entity manager to build, and no
schema to validate, because there is nothing that could disagree with the database that the build
did not already check.

## Dev Services

Quarkus Dev Services starts a database container for you in dev and test mode, which suits `YoSQL`
because a generated repository takes a `DataSource` and asks nothing about where it came from. Set
no JDBC URL and you get a container; set one and you get that.

Code generation itself never connects to a database — it reads `.sql` files and your record
sources — so a build with no database available still builds.

## Testing

```java
@QuarkusTest
class TenantRepositoryTest {

    @Inject
    TenantRepository tenants;

    @Test
    void findsTheTenantItInserted() {
        final var id = UUID.randomUUID();

        tenants.insertTenant(id, "acme", Instant.now());

        assertThat(tenants.findTenant(id)).map(Tenant::slug).hasValue("acme");
    }
}
```

`@QuarkusTest` with Dev Services gives a real database, which is what a test of a query wants. To
run the same tests against the native binary, `@QuarkusIntegrationTest` — worth doing once, to see
that the persistence layer genuinely needs nothing.

## Reactive

There is none. `YoSQL` generates blocking JDBC, so a repository belongs on a worker thread — under
Quarkus that means a `@Blocking` endpoint, or letting a non-reactive resource method run on the
worker pool as it does by default. If your application is built on Mutiny end to end, the reactive
SQL clients are the better fit.
