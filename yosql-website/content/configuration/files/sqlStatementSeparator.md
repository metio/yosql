---
title: sqlStatementSeparator
date: 2019-09-27T18:51:08+02:00
menu:
main:
parent: Files
categories:
- Configuration
  tags:
- sql
- files
- suffix
---

The `sqlStatementSeparator` option can be used to change the character that separates multiple SQL statements within a single `.sql` file. It defaults to `;`.

## Configuration Options

### Option: ';'

The default value of the `sqlStatementSeparator` configuration option is `.sql`. It matches all files that end with `.sql`.

### Option: '|'

Changing the `sqlStatementSeparator` configuration option to `|` configures `YoSQL` split `.sql` files using the `|` character.

## Related Options

- [inputBaseDirectory](../inputbasedirectory/): Controls the base directory for `.sql` files.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](../../tooling/maven).

```xml
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <files>
            <sqlStatementSeparator>configValue</sqlStatementSeparator>
          </files>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](../tooling/gradle).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  files {
      sqlStatementSeparator = configValue
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](../tooling/bazel).

TODO: info for bazel

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../tooling/cli).

```shell
$ yosql --files-sql-statement-separator=configValue
```

The shorter form is available as well:

```shell
$ yosql --sql-statement-separator=configValue
```
