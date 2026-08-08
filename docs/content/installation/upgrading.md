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

## 2026.9.8

A `schema.sqlStatementsDirectory` holding versioned migrations is now read in version order.
Previously the files were read in name order, so `V10__` and `V12__` were applied ahead of `V2__`
and every column added past the ninth migration was missing from the schema `YoSQL` worked from.

Nothing needs changing, but two things are worth rechecking if you pointed this at a Flyway or
Liquibase directory. Schema validation lowered to `WARN` because it reported columns that plainly
exist can go back to `ERROR`. Parameter and result types written out by hand because inference
"did not work" are now inferred, and the front matter still wins where the two disagree — so a type
declared against the wrong schema stays wrong until you delete it.

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

### The `TI` logging API is gone

`logging.api` no longer accepts `TI`. The generator behind it was never written: it answered every
question with an empty block while reporting that logging was off, and the repositories it produced
did not compile — a bare `if () {` reading a variable nothing declared.

If your build sets it — `<api>TI</api>` in a `pom.xml`, `api = 'TI'` in a `build.gradle`, or
`--logging-api=TI` on the command line — pick one of `NONE`, `JUL`, `SYSTEM`, `LOG4J`, `SLF4J` or
`TINYLOG`. `NONE` is the closest to what `TI` actually did, which was nothing.

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

### `skipLines` is gone, and a licence header is dropped on its own

A `.sql` file that opens with a block comment has it dropped, however many lines it runs to. The
count that used to have to match — and to be kept matching every time somebody edited the header —
is no longer a setting.

Remove `skipLines` from your build; Maven and Ant fail on an unknown element. If your headers are
written as `--` lines rather than as a block comment, rewrite them as `/* … */`. A `--` line at the
top of a file is front matter, and `-- SPDX-License-Identifier: 0BSD` is as good a YAML mapping as
`-- name: findTenant`, so there is no way to tell the two apart.

### Generated code now says so, and the `annotations` switches are gone

Every generated class, field and method carries `@Generated`:

```java
@Generated(
    value = "wtf.metio.yosql",
    comments = "generated by YoSQL 2026.8.8 - do not modify, this file is rewritten on every build"
)
```

It used to be off unless you turned it on, which is backwards for a marker whose whole job is to tell
coverage tools, linters and the next reader that nobody wrote this by hand. Eleven settings decided
whether it appeared, which annotation class it used, which members it carried and what each of them
said; all eleven are gone, and so is the `javax.annotation.Generated` option, which has not existed
in the JDK since Java 11.

The `value` is the generator's fully qualified name, which is what the annotation's own
documentation asks for. The `comments` carry the release that wrote the file — the one fact about
generated code that is nowhere else in it, since which `.sql` file a method came from is already in
its javadoc.

There is deliberately **no date**. Two builds of the same statements produce the same code, and a
timestamp would be the only thing in the output that differed between them.

`javax.annotation.processing.Generated` lives in the `java.compiler` module, which a classpath build
always has. A **modular** project compiling generated code inside its own module needs
`requires static java.compiler;` in its `module-info.java` — static, because the annotation is
discarded after compilation and nothing needs it at run time.

Remove the `<annotations>` switches from your build — `annotationApi`, `annotateClasses`,
`annotateFields`, `annotateMethods`, `classMembers`, `fieldMembers`, `methodMembers`,
`classComment`, `fieldComment`, `methodComment` and `generatorName`. Maven and Ant fail on an unknown
element. The three settings that add **your own** annotations to generated repositories,
constructors and methods are unchanged.

### Repository and method names are no longer spelled out in configuration

Eight settings decided what to put around a generated name — `repositoryNamePrefix`,
`repositoryNameSuffix`, `repositoryInterfacePrefix`, `repositoryInterfaceSuffix`,
`executeOncePrefix`, `executeOnceSuffix`, `executeBatchPrefix` and `executeBatchSuffix`. Six of them
defaulted to nothing at all, and the two that did something were carrying the two names that have to
differ from each other:

- A repository is its directory plus `Repository`, so `tenant/*.sql` becomes `TenantRepository`.
- Its interface is the same name without that suffix — `Tenant` — and gets an `I` in front only when
  there is no suffix to drop.
- A batch method is its statement plus `Batch`, so `insertTenant` and `insertTenantBatch` can live in
  the same repository.

That is what the defaults already produced. Remove the settings from your build and from any front
matter that set them per statement; Maven and Ant fail on an unknown element. If you had renamed
something, the generated names change back — name the repository with
[repository](../../configuration/sql/repository/) in the front matter, which still decides it
outright.

### `sqlStatementsDirectory` is relative to your project, not to where the build was started

[sqlStatementsDirectory](../../configuration/schema/sqlstatementsdirectory/) says where the DDL
describing your schema lives. Written as a relative path, it used to be resolved against whatever
directory the build was running in rather than against the module being built — so in a multi-module
project, `mvn verify` from the root looked for the schema under the root while `mvn verify` inside
the module found it. Every frontend resolves it against the project now, the same as every other
directory setting.

Nothing reports the difference, which is what makes it worth checking: a schema directory that
resolves to nothing is indistinguishable from a schema that raises no complaints, because reading
the schema is designed never to fail a build. If you set this to a relative path, confirm it is
relative to the module's own directory. An absolute path is unaffected.

Maven is where this is most likely to have bitten you. The CLI runs in the directory you invoke it
from, which is the answer it now arrives at deliberately; Gradle ran in the project's own directory,
so it already found the right one, and no longer depends on that being true.

### Ant can set the method name prefixes and the annotations

Ant builds an attribute setter only for a type it can make out of a string, and
[allowedCallPrefixes](../../configuration/repositories/allowedcallprefixes/),
[allowedReadPrefixes](../../configuration/repositories/allowedreadprefixes/) and
[allowedWritePrefixes](../../configuration/repositories/allowedwriteprefixes/) are lists — so the
task declared them and then refused them, with `doesn't support the "allowedReadPrefixes"
attribute`. The whole [annotations](../../configuration/annotations/) group went the same way: the
nested element could be written, and nothing inside it could be set.

Both work now. The three lists are one attribute, separated by commas:

```xml
<repositories validateMethodNamePrefixes="true"
              allowedReadPrefixes="fetch,find"/>
```

An annotation is a nested element, and each of its members is a nested element of that:

```xml
<annotations>
    <repositoryAnnotations type="jakarta.inject.Named">
        <member key="value" value="tenants"/>
    </repositoryAnnotations>
</annotations>
```

Nothing here can have broken a build: none of it could be set before. If you turned
`validateMethodNamePrefixes` off because the prefix lists were out of reach, it is worth turning
back on.

## 2023.5.3 and earlier

See the [release notes](https://github.com/metio/yosql/releases) for those versions.
