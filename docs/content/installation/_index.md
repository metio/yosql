---
title: Installation
date: 2026-08-02
menu:
  main:
    weight: 90
---

`YoSQL` turns `.sql` files into Java repositories at build time. Pick the tooling that matches your
build, point it at your SQL, and the generated code is ordinary Java from then on.

## What you need

**Java 25 or later**, both to run `YoSQL` and to compile and run what it generates. Generated code
uses `var`, text blocks, records and sequenced collections, so it does not compile on older
releases.

Nothing else. `YoSQL` is not a dependency of your application — it runs during your build and the
code it leaves behind calls only the JDK and your JDBC driver. Nothing needs to be on the classpath
at run time, which is also why a generated repository works unchanged inside a
[GraalVM](https://www.graalvm.org/) native image.

## Choosing the tooling

| Your build | Use |
| --- | --- |
| [Maven](https://maven.apache.org/) | the [Maven plugin](../tooling/maven/) |
| [Gradle](https://gradle.org/) | the [Gradle plugin](../tooling/gradle/) |
| [Ant](https://ant.apache.org/) | the [Ant task](../tooling/ant/) |
| anything else, or no build at all | the [command line tool](../tooling/cli/) |

The command line tool is also the way to generate code once and never think about `YoSQL` again:
run it, commit the result, and drop it from your build entirely.

## Your first statement

Write a `.sql` file under `src/main/yosql`:

```sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id, slug, created_at
from tenant
where id = :id
```

Write the record it should build:

```java
package com.example.domain;

public record Tenant(UUID id, String slug, Instant createdAt) {
}
```

Run your build. You get a `TenantRepository` with a `findTenant` method returning
`Optional<Tenant>`, and a converter that reads each column by name and calls the constructor —
no reflection, nothing resolved at run time.

From here, the [tutorial](./tutorial/) builds a whole project — schema, statements, records and
tests against a real database — in about half an hour. [SQL files](../sql/) covers how statements are
written and [configuration](../configuration/) covers everything you can change about the output.

## Verifying a download

Releases of the command line tool and the Ant task ship a `SHA256SUMS` file alongside the archives,
signed with [cosign](https://docs.sigstore.dev/) keyless signing. To check an archive you downloaded
from the [releases page](https://github.com/metio/yosql/releases):

```shell
sha256sum --check --ignore-missing SHA256SUMS
cosign verify-blob SHA256SUMS \
  --bundle SHA256SUMS.bundle \
  --certificate-identity-regexp 'https://github\.com/metio/yosql/' \
  --certificate-oidc-issuer https://token.actions.githubusercontent.com
```

Artifacts published to [Maven Central](https://central.sonatype.com/namespace/wtf.metio.yosql.tooling) are
signed with PGP instead, which your build tool checks for you.

## Releases

A new version is published on the 8th of each month, named after that date — `2026.8.8` for the
August 2026 release. A month with no changes gets no release. When a release needs you to change
something, it is described under [upgrading](./upgrading/).
