---
title: Alternatives
date: 2026-08-02
menu:
  main:
    parent: Community
---

Java has no shortage of ways to talk to a database. This is a frank account of how `YoSQL`
compares to the ones you are most likely choosing between, including where they are the better
answer.

## The short version

| | Where the query lives | Runtime dependency | Reflection | Result mapping |
| --- | --- | --- | --- | --- |
| **YoSQL** | `.sql` files | none | none | generated from a record |
| [jOOQ](https://www.jooq.org/) | Java DSL | jOOQ | some | generated from the schema |
| [MyBatis](https://mybatis.org/mybatis-3/) | XML or annotations | MyBatis | yes | configured |
| [Spring Data JDBC](https://spring.io/projects/spring-data-jdbc) | derived names or `@Query` | Spring | yes | convention |
| [JDBI 3](https://jdbi.org/) | annotations or fluent API | JDBI | yes | mappers |
| [Hibernate](https://hibernate.org/) / JPA | JPQL, criteria, derived names | Hibernate | yes | mapped entities |
| [Panache](https://quarkus.io/guides/hibernate-orm-panache) | active record or repository | Quarkus + Hibernate | build-time indexed | mapped entities |

## Where YoSQL wins

**Nothing ships with your application.** `YoSQL` runs during the build and leaves Java behind. There
is no library on the runtime classpath, no version to keep in step with your framework, and no
upgrade that changes how your queries behave. You can stop using `YoSQL` at any point by keeping
the generated code — see [the command line tool](../../tooling/cli/).

**No reflection, anywhere.** Generated code reads a `ResultSet` through calls the compiler resolved.
That is what makes a [GraalVM](https://www.graalvm.org/) native image uneventful: no reflection
hints, no registration to forget, no failure that only appears in the compiled image. Every release
compiles a generated repository into a `--no-fallback` image and runs it against a real database, so
this is checked rather than claimed.

**The SQL is SQL.** It sits in `.sql` files your editor highlights, your DBA can read, and you can
paste straight into a console to run against production. Tuning a query does not mean recompiling
anything or working out what a DSL emitted.

**Errors happen at build time.** A column no component reads, a component no column supplies, a
parameter whose type nothing gives — each fails the build naming the file and the statement, rather
than throwing on the first request that reaches it.

**Your schema is checked, without a database.** `YoSQL` reads the `create table` statements your
project already keeps and holds your queries to them: a column that does not exist, a parameter
whose type disagrees with its column, a nullable column read into a primitive. Nothing connects to
anything, so it works in a checkout with no services running — see
[schema validation](../../sql/schema/).

## Where the others win

**[jOOQ](https://www.jooq.org/) knows your schema more thoroughly.** It generates from a live
database, so it type-checks the whole query — expressions, functions, joins — where `YoSQL`
[reads your DDL](../../sql/schema/) and checks columns, parameter types and result rows. Its DSL composes — building a query from
conditions decided at runtime is natural, where `YoSQL` would have you write the variants out. If
your queries are assembled rather than written, jOOQ is the better tool. Its commercial licence for
non-open-source databases is the trade.

**[Hibernate](https://hibernate.org/) manages object graphs.** Dirty checking, cascading, lazy
loading and a first-level cache are real features, and reimplementing them over generated JDBC is a
bad idea. If your domain is a graph of entities you mutate and flush, use an ORM. `YoSQL` suits
applications that read rows and write statements.

**[Spring Data JDBC](https://spring.io/projects/spring-data-jdbc) and
[JDBI](https://jdbi.org/) need no build step.** An interface and an annotation are less machinery
than a code generator wired into your build. For a handful of queries that is a fair trade, and both
integrate with Spring's transaction management without you thinking about it.

**[MyBatis](https://mybatis.org/mybatis-3/) has dynamic SQL.** Its `<if>` and `<foreach>` elements
build statements from conditions at runtime. `YoSQL` has no equivalent — a statement is one
statement — so a query with six optional filters is six statements or a `WHERE (:filter IS NULL OR
column = :filter)` construction.

**[Panache](https://quarkus.io/guides/hibernate-orm-panache) is fewer lines for CRUD.** If the
application is mostly `findById` and `persist`, an active-record API written for that is shorter
than any amount of generated code.

## Honest limitations

These are things `YoSQL` does not do today, so you can rule it out quickly rather than discover them
later:

- **No dynamic SQL.** A statement is fixed at build time. See the
  [cookbook](../../sql/cookbook/) for how optional filters and `IN` lists are handled.
- **Java 25 or later**, both to run the generator and to compile what it emits.
- **One statement, one round trip.** Nothing batches across statements or caches results. That is
  deliberate, and it means performance is exactly what your SQL does.

## The family

`YoSQL` takes its name and its idea from [yesql](https://github.com/krisajenkins/yesql), and the same
idea has been had in most languages:

- [yesql](https://github.com/krisajenkins/yesql) (Clojure)
- [HugSQL](https://www.hugsql.org/) (Clojure)
- [ElSql](https://github.com/OpenGamma/ElSql) (Java)
- [jasql](https://bitbucket.org/rick/jasql/src/develop/) (C#)
- [eql](https://github.com/artemeff/eql) (Erlang)
- [sqlc](https://sqlc.dev/) (Go)
- [dotsql](https://github.com/gchaincl/dotsql) (Go)
- [goyesql](https://github.com/nleof/goyesql) (Go)
- [yeshql](https://hackage.haskell.org/package/yeshql) (Haskell)
- [sqlt](https://github.com/eugeneware/sqlt) (JavaScript)
- [preql](https://github.com/NGPVAN/preql) (JavaScript)
- [jsyesql](https://github.com/fanatid/jsyesql) (JavaScript)
- [YepSQL](https://github.com/LionsHead/YepSQL) (PHP)
- [yayql](https://github.com/gnarmis/yayql) (Ruby)
