---
title: skipLines
date: 2022-04-15
menu:
  main:
    parent: Files
categories:
  - Configuration
tags:
  - files
---

The number of lines to skip in each file (e.g. a copyright header).

## Configuration Options

### Option: '0'

The default value of the `skipLines` configuration option is `0` - which does not skip any lines.

### Option: '0'

Changing the `skipLines` configuration option to `5` configures `YoSQL` skip the first 5 lines in each `.sql` file in encounters.

## Related Options

- [inputBaseDirectory](../inputbasedirectory/): The input directory for the user written SQL files.
- [outputBaseDirectory](../outputbasedirectory/): The output directory for the generated classes.
- [sqlFilesCharset](../sqlfilescharset/): The charset to use while reading .sql files.
- [sqlFilesSuffix](../sqlfilessuffix/): The file ending to use while searching for SQL files.
- [sqlStatementSeparator](../sqlstatementseparator/): The separator to split SQL statements inside a single .sql file.

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --files-skip-lines=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --skip-lines=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `skipLines` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  files {
    skipLines.set(configValue)
  }
}
```

or in Groovy syntax like this:

```groovy
plugins {
  id "java"
  id "wtf.metio.yosql" version "0.0.0-SNAPSHOT"
}

yosql {
  files {
    skipLines = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `skipLines` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <files>
          <skipLines>configValue</skipLines>
        </files>
      </configuration>
    </plugin>
  </plugins>
</build>
```
