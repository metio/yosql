---
title: allowedCallPrefixes
date: 2022-04-15
menu:
  main:
    parent: Repositories
categories:
  - Configuration
tags:
  - repositories
---

Configures which name prefixes are allowed for statements that are calling stored procedures.

## Configuration Options

### Option: 'call, execute, evaluate, eval'

The default value of the `allowedCallPrefixes` configuration option is `call, execute, evaluate, eval` to allow several commonly used names for calling procedures.

### Option: 'do'

Changing the `allowedCallPrefixes` configuration option to `do` only allows names with the prefix `do` to call stored procedures.

## Related Options

- [allowedReadPrefixes](../allowedreadprefixes/): Configures which name prefixes are allowed for statements that are reading data from your database.
- [allowedWritePrefixes](../allowedwriteprefixes/): Configures which name prefixes are allowed for statements that are writing data to your database.
- [basePackageName](../basepackagename/): The base package name for all repositories
- [batchPrefix](../batchprefix/): 
- [batchSuffix](../batchsuffix/): 
- [catchAndRethrow](../catchandrethrow/): Catch exceptions during SQL execution and re-throw them as RuntimeExceptions
- [eagerName](../eagername/): 
- [generateBatchApi](../generatebatchapi/): Generate batch methods
- [generateInterfaces](../generateinterfaces/): Generate interfaces for all repositories
- [generateRxJavaApi](../generaterxjavaapi/): Generate batch methods
- [generateStandardApi](../generatestandardapi/): Generate standard methods
- [generateStreamEagerApi](../generatestreameagerapi/): Generate batch methods
- [generateStreamLazyApi](../generatestreamlazyapi/): Generate batch methods
- [injectConverters](../injectconverters/): Toggles whether converters should be injected as constructor parameters.
- [lazyName](../lazyname/): 
- [repositoryNameSuffix](../repositorynamesuffix/): The repository name suffix to use.
- [rxjava2Prefix](../rxjava2prefix/): 
- [rxjava2Suffix](../rxjava2suffix/): 
- [streamPrefix](../streamprefix/): 
- [streamSuffix](../streamsuffix/): 
- [validateMethodNamePrefixes](../validatemethodnameprefixes/): Validate user given names against list of allowed prefixes per type.

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --repositories-allowed-call-prefixes=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --allowed-call-prefixes=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `allowedCallPrefixes` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  repositories {
    allowedCallPrefixes.set(configValue)
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
  repositories {
    allowedCallPrefixes = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `allowedCallPrefixes` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <repositories>
          <allowedCallPrefixes>configValue</allowedCallPrefixes>
        </repositories>
      </configuration>
    </plugin>
  </plugins>
</build>
```
