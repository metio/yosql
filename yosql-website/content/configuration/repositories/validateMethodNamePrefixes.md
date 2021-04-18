---
title: validateMethodNamePrefixes
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Repositories
categories:
  - Configuration
tags:
  - repositories
  - validation
  - name
  - prefix
---

Enable or disable validation of SQL statement names. Which prefixes are allowed for each type of statement is configured in other options. It defaults to `false`.


## Configuration Options

### Option: 'false'

The default value of the `validateMethodNamePrefixes` configuration option is `false` which disables the validation of names according to your configured prefixes.

### Option: 'true'

Changing the `validateMethodNamePrefixes` configuration option to `true` enables the validation of method names.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](/tooling/maven/).

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <repositories>
            <validateMethodNamePrefixes>true</validateMethodNamePrefixes>
          </repositories>
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
  repositories {
      validateMethodNamePrefixes = true
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --repositories-validate-method-name-prefixes=true
```

The shorter form is available as well:

```shell
$ yosql --validate-method-name-prefixes=true
```
