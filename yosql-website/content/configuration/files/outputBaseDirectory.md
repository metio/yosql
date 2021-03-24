---
title: outputBaseDirectory
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Files
categories:
  - Configuration
tags:
  - files
  - directories
  - output
---

Controls the root directory `YoSQL` will use to output generated Java code.

## Configuration Options

### Option: '.'

The default value of the `outputBaseDirectory` configuration option is `.` - the current directory. Note that tooling may change the default output base directory to better reflect a typical project structure used with such a tool.

### Option: 'some/other/directory'

Changing the `outputBaseDirectory` configuration option to `some/other/directory!` configures `YoSQL` to write into the relative directory`some/other/directory`.

### Option: '/an/absolute/path'

Changing the `outputBaseDirectory` configuration option to `/an/absolute/path!` configures `YoSQL` to write into the absolute directory path `/an/absolute/path`.

## Related Options

- [inputBaseDirectory](/configuration/files/inputbasedirectory/): Controls the input directory for `.sql` files.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](/tooling/maven/). By default, the Maven tooling with set `inputBaseDirectory` to `src/main/yosql`.

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <files>
            <outputBaseDirectory>${project.build.directory}/generated-sources/yosql</outputBaseDirectory>
          </files>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  files {
    inputBaseDirectory = file("build/generated-sources/yosql")
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --files-input-base-directory=generated-sources/yosql
```

The shorter form is available as well:

```shell
$ yosql --input-base-directory=generated-sources/yosql
```
