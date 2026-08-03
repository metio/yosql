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

### YoSQL reads your schema now

If your project keeps `create table` statements where `YoSQL` can see them, this release starts
holding the rest of your SQL to them: a column that does not exist, a parameter whose type disagrees
with its column, a nullable column read into a primitive. Nothing connects to a database, so it
works in a checkout with no services running.

**It reports and does not stop.** The default is `WARN`, so no build that passed before fails
because of this — expect new warnings, not new failures. Read them; they are the queries that would
have failed on whichever request reached them first.

Once you have dealt with them, turn it up so nothing new gets in:

```xml
<schema>
  <validation>ERROR</validation>
</schema>
```

To hear nothing at all, set it to `OFF`.

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

### The `java` configuration group is gone

Its six switches decided whether generated classes, fields, methods, parameters and locals were
declared `final`. Generated code reassigns none of them, so they all are now, which is what the
defaults already said. One of them, `useSealedInterfaces`, was documented in every frontend and did
nothing at all.

Remove the block if your build has one — a `<java>` element in a `pom.xml`, a `java { }` block in a
`build.gradle`, a `<java>` element in an Ant task, or `--use-final-*` on the command line. Maven and
Ant fail on an unknown element, so this one is not optional. Nothing about the generated code
changes unless you had turned one of them off, in which case the output gains the `final` keywords
it describes.

### The `names` configuration group is gone

Its twenty-two settings renamed the variables inside generated methods — the `Connection`, the
`ResultSet`, the loop counter. None of them is part of a repository's API, so none of them was a
decision worth making: a name nobody outside the method can see cannot fit a codebase better or
worse. They also had to be kept distinct from each other, which is why a whole validator existed to
check they were.

Remove the block if your build has one — a `<names>` element in a `pom.xml`, a `names { }` block in
a `build.gradle`, a `<names>` element in an Ant task, or the matching command-line options. Maven and
Ant fail on an unknown element, so this one is not optional. Generated code is unchanged unless you
had renamed something, in which case it goes back to the default name.

The one thing those settings could rescue was a statement whose own parameter is called `connection`,
`statement`, `resultSet`, `index` or another name a generated method already uses. That is now a
build error naming the file, the statement and the parameter, instead of a Java error about a
variable already defined in a file you did not write. Rename the parameter in the SQL — the name
reaches no further than the method's signature.

## 2023.5.3 and earlier

See the [release notes](https://github.com/metio/yosql/releases) for those versions.
