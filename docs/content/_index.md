---
title: write more SQL
date: 2020-04-13
---

> write more SQL!

`YoSQL` is a [yesql](https://github.com/krisajenkins/yesql) inspired persistence solution for
[Java](https://www.java.com/). It turns [SQL](https://en.wikipedia.org/wiki/SQL) statements into
type-safe Java code at build time.

Write the query you want:

```sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id, slug, created_at
from tenant
where id = :id
```

and the record you want back:

```java
public record Tenant(UUID id, String slug, Instant createdAt) {
}
```

and you get `Optional<Tenant> findTenant(UUID id)`, backed by a mapper that reads each column by
name and calls the constructor. No reflection, no proxies, no annotations on your domain types.

Needs [Java 25](./installation/). Ready in about a minute — see [installation](./installation/).

## Features

### database-first

`YoSQL` allows you to use the full power of your database to overcome the individual challenges of your project. Re-use existing database tooling to iterate quickly by just running an SQL statement directly against your database without ever starting your JVM application. Bridge the gap between developers and DBAs by using your SQL statements as a common meeting ground and place for performance tuning.

### zero dependency

`YoSQL` is a true zero dependency solution. Instead of adding a new dependency to your project, `YoSQL` is available as a build-tool that is only active during build-time. Once everything is generated, `YoSQL` is no longer required at run-time. The generated code relies only on JDK classes without any external dependencies.

### reflection-free

Generated code reads a `ResultSet` through calls the compiler has already resolved — no reflection,
no proxies, no runtime type lookup. Name a record as a statement's result row type and the mapper is
written for you, still as plain `resultSet.getX(...)` calls. That is what makes a
[GraalVM](https://www.graalvm.org/) native image straightforward: nothing in the persistence layer
needs a reflection hint, and no code path can fail the first time it runs because a registration was
missing. Every release compiles a generated repository into a native image and runs it against a
real database, so that claim is checked rather than asserted.

### developer friendly

No magic involved - `YoSQL` generates code that is easy to read and debug. Step-through in case you encounter an error or use the extensive logging capabilities of `YoSQL` to monitor both code generation and SQL execution. No hidden SELECT statements or opened transactions, developers using `YoSQL` are 100% in control on how their SQL statements are executed. Get started quickly in under a minute (not reading this included): Just add the appropriate plugin to your project, and you are good to go.

## Usage

[Installation](./installation/) covers what you need and how to add `YoSQL` to a Maven, Gradle, Ant
or plain project. However you run it, the shape is the same:

1. Write SQL statements, and configure `YoSQL` if the defaults do not suit you.
2. Run your build to generate Java code.
3. Call the generated repositories from your application.

Then: [SQL files](./sql/) for how statements are written, [converters](./sql/converters/) for
turning rows into your own types, and [configuration](./configuration/) for everything you can
change about the output.
