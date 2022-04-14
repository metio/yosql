---
title: useGenerics
date: 2022-04-14
menu:
  main:
    parent: Java
categories:
  - Configuration
tags:
  - java
---

Controls the usage of generic types in generated code.

## Configuration Options

### Option: 'true'

The default value of the `useGenerics` configuration option is `true` which enables the use of generics in generated code.

### Option: 'false'

Changing the `useGenerics` configuration option to `false` disables the use of generics in generated code.

## Related Options

- [apiVersion](../apiversion/): Controls the Java SDK API version to use in generated code.
- [useDiamondOperator](../usediamondoperator/): Controls whether the diamond operator is used in generated code.
- [useFinal](../usefinal/): Controls whether variables and parameters are declared as final in generated code.
- [useProcessingApi](../useprocessingapi/): Controls the usage of the processing API in generated code.
- [useRecords](../userecords/): Controls the usage of records in generated code.
- [useStreamAPI](../usestreamapi/): Controls the usage of the stream API in generated code.
- [useTextBlocks](../usetextblocks/): Controls the usage of text blocks in generated code.
- [useVar](../usevar/): Controls the usage of the 'var' keyword in generated code.

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --java-use-generics=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --use-generics=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `useGenerics` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  java {
    useGenerics.set(configValue)
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
  java {
    useGenerics = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `useGenerics` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <java>
          <useGenerics>configValue</useGenerics>
        </java>
      </configuration>
    </plugin>
  </plugins>
</build>
```
