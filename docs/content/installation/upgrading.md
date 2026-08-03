---
title: Upgrading
date: 2026-08-02
menu:
  main:
    parent: Installation
categories:
  - Installation
tags:
  - upgrading
---

What each release needs from you, when it needs anything. Releases not listed here are drop-in.

## 2026.8.8

The first release since 2023.5.3, and it changes enough to be worth reading before you bump the
version.

### Java 25 is now the minimum

Both to run `YoSQL` and to compile and run what it generates. Generated code uses `var`, text
blocks, records and sequenced collections.

If your project is on an older Java release, stay on `2023.5.3` until you can move. There is no
configuration that makes the generator emit older code — the switch that used to do that is gone,
because it silently produced output that no longer matched what the documentation promised.

### A converter is named by its class, and nothing else

The four-part converter description and the registry that held it are gone. A statement that used
to name a converter by alias:

```sql
-- resultRowConverter:
--   alias: itemConverter
```

now names the class:

```sql
-- resultRowConverter: com.example.persistence.converter.ToItemConverter
```

and the build configuration that declared the alias goes away entirely. In Maven:

```xml
<converter>
    <!-- delete the whole rowConverters block -->
    <rowConverters>
        <rowConverter>
            <alias>itemConverter</alias>
            <converterType>com.example.persistence.converter.ToItemConverter</converterType>
            <methodName>asUserType</methodName>
            <resultType>com.example.domain.Item</resultType>
        </rowConverter>
    </rowConverters>
</converter>
```

The same applies to `rowConverters` in Gradle and Ant, and to `--rowConverters` on the command
line. `defaultConverter` survives but now takes a single class name rather than the four parts.

`YoSQL` reads the class to find its one public method taking a `ResultSet`. That method's name is
what the repository calls and its return type is what the statement produces, so nothing repeats
what the class already says. Two consequences worth knowing:

- The converter has to be **visible as source** under
  [sourceDirectory](../../configuration/files/sourcedirectory/), because it is read rather than
  loaded. A converter that lives in a different module needs that directory pointed at it.
- The class must declare **exactly one** public method taking a `ResultSet`. None, or more than
  one, fails the build and names the class.

The field the repository holds the converter in is now the class name with a lower-case first
letter — `toItemConverter` rather than whatever the alias was. That only matters if you were
reading generated fields directly.

### Generic types cannot be result rows

A record declaring type parameters is refused rather than mapped, because a statement says nothing
about what to substitute for them. Name a concrete type instead.

### YoSQL can read your schema

New, and off unless you turn it on, so this release changes nothing here by itself.

Point it at the `create table` statements your project already keeps and it holds the rest of your
SQL to them: a column that does not exist, a parameter whose type disagrees with its column, a
nullable column read into a primitive. Nothing connects to a database, so it works in a checkout
with no services running.

```xml
<schema>
  <validation>WARN</validation>
</schema>
```

Start at `WARN` rather than `ERROR`. The first run reports everything at once, which is a list to
work through rather than a build to fix in one sitting.

It also settles what a parameter's type is, which removes the `parameters` block from write
statements entirely — see [schema validation](../../sql/schema/).

### A colon with no name after it is no longer a parameter

`:id` is a parameter. A bare `:` is not, and used to be read as one — which made two ordinary things
wrong.

A statement carrying a licence header picked up a parameter with no name from the colon in
`SPDX-License-Identifier:`. And PostgreSQL's `::` cast bound two parameters, the bare colon and the
type name, so **every parameter after a cast was bound to the wrong placeholder** — the statement
ran and answered with the wrong rows.

If you have statements using `::` casts, their generated methods change: the spurious parameters
disappear and the real ones move to the indices they should always have had. Check any such method's
signature after upgrading, and be glad if you never hit the bug.

### Every statement is now reachable with and without a connection

Where a statement got its connection used to be decided per statement by `createConnection`, and you
got one method. It is now decided by the caller, and you get two:

```java
Optional<Tenant> findTenant(UUID id);
Optional<Tenant> findTenant(Connection connection, UUID id);
```

The first opens a connection from the repository's `DataSource` and closes it; the second runs on
the one it is given. That is what lets several statements share a transaction — see
[transactions](../../sql/transactions/).

Nothing you call today changes name or signature, so existing code keeps compiling, with one
exception: **a repository whose statements all set `createConnection: false` used to have a no-arg
constructor** and now takes a `DataSource`, because it now also has methods that need one. Pass it
one, or set
[generateConnectionOverloads](../../configuration/repositories/generateconnectionoverloads/) to
`false` to keep one method per statement.

If you wrote a statement twice in a file — once plain and once with `createConnection: false` — to
get both shapes, delete the second one. It now generates a method with a `2` in its name for no
reason.

### A parameter with no type now fails instead of becoming an Object

A parameter the front matter did not type used to be bound as `java.lang.Object`. The method
compiled, accepted anything, and offered exactly the type safety of the JDBC it replaced — silently.
It is now a build error naming the file, the statement and every parameter still without a type.

Two things fill them in. A statement naming a record with
[resultRowType](../../configuration/sql/resultrowtype/) takes each parameter's type from the
component of the same name, so most read statements need nothing:

```sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id, slug from tenant where id = :id
```

`Tenant` declares `UUID id`, so `:id` is a `UUID`. For everything else — write statements, above all
— name the types. The front matter now takes a mapping of name to type alongside the list form:

```sql
-- parameters:
--   id: uuid
--   slug: string
--   createdAt: instant
```

`uuid`, `string` and `instant` are short names for the types statements are usually written in; the
full list is under [parameters](../../configuration/sql/parameters/). A fully-qualified class name
still works everywhere, and the list form is unchanged for parameters that need a `sqlType`, a
`scale` or a `variant`.

Where the build fails, the message shows the front matter to add. Nothing else has to change.

### Statements that generate nothing now fail

A statement whose name matches none of the configured prefixes, and which sets no explicit `type`,
used to be skipped without a word — so a typo removed a method from your repository and said
nothing. It is now a build error naming the file and the statement. If a release starts failing
here, it is reporting something that was already broken.

## 2023.5.3 and earlier

See the [release notes](https://github.com/metio/yosql/releases) for those versions.
