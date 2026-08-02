---
title: CLI
date: 2019-06-16T18:23:45+02:00
menu:
  main:
    parent: Tooling
categories:
  - Tooling
tags:
  - CLI
---

The [command line](https://en.wikipedia.org/wiki/Command-line_interface) tool generates the same
code as the build plugins, without being part of a build. Use it when your project builds with
something `YoSQL` has no plugin for, or to generate code once and commit the result.

## Getting it

Each [release](https://github.com/metio/yosql/releases/latest) publishes two kinds of archive:

- **`yosql-tooling-cli-<version>-linux.zip`** and **`-mac.zip`** — a single native binary. It starts
  instantly and needs no Java installed at all.
- **`yosql-tooling-cli-<version>-jvm.zip`** — scripts plus jars, for any platform with Java 25.

Unpack it and put the `yosql` binary, or the `bin/yosql` script, on your `PATH`. Both archives are
covered by the signed `SHA256SUMS` — see [verifying a download](../../installation/#verifying-a-download).

## Using it

```shell
yosql generate --files-input-base-directory=/path/to/your/sql/files
```

That reads every `.sql` file under the directory and writes Java beside it. Option names follow the
[configuration](/configuration/) groups: a setting shown there as `files.outputBaseDirectory` is
`--files-output-base-directory` here. `yosql generate --help` lists all of them.

Options can also come from a file, which is easier to keep in version control than a long command:

```shell
yosql generate @yosql.args
```

where `yosql.args` holds one option per line:

```text
--files-input-base-directory=src/main/yosql
--files-output-base-directory=src/main/java
--repositories-base-package-name=com.example.persistence
```
