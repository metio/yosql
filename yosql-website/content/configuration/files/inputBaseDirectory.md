---
title: inputBaseDirectory
date: 2022-04-15
menu:
  main:
    parent: Files
categories:
  - Configuration
tags:
  - files
---

The input directory for the user written SQL files.

## Configuration Options

### Option: '.'

The default value of the `inputBaseDirectory` configuration option is `.` - the current directory. Note that tooling may change the default input base directory to better reflect a typical project structure used with such a tool.

### Option: 'some/other/directory'

Changing the `inputBaseDirectory` configuration option to `some/other/directory!` configures `YoSQL` to look into the path relative directory `some/other/directory`.

### Option: '/an/absolute/path'

Changing the `inputBaseDirectory` configuration option to `/an/absolute/path!` configures `YoSQL` to look into the absolute directory path `/an/absolute/path`.

## Related Options

- [outputBaseDirectory](../outputbasedirectory/): The output directory for the generated classes.
- [skipLines](../skiplines/): The number of lines to skip in each file (e.g. a copyright header).
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
$ yosql --files-input-base-directory=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --input-base-directory=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `inputBaseDirectory` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  files {
    inputBaseDirectory.set(configValue)
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
    inputBaseDirectory = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `inputBaseDirectory` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <files>
          <inputBaseDirectory>configValue</inputBaseDirectory>
        </files>
      </configuration>
    </plugin>
  </plugins>
</build>
```
