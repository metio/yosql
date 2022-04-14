---
title: daoApi
date: 2022-04-14
menu:
  main:
    parent: Api
categories:
  - Configuration
tags:
  - api
---

The persistence API to use.

`YoSQL` supports multiple persistence APIs to interact with a database. We recommend that you pick the one that is already available in your project. In case you are starting fresh, `YoSQL` will default to the JDBC implementation because it requires no external dependencies and thus your project should be able to compile the generated code just fine. Some `YoSQL` tooling like the Maven plugin might auto-detect certain settings in your project to make your life easier, however you are always in full control and can change very aspect of the generated code.

## Configuration Options

### Option: 'JDBC'

The `javax.sql` based implementation of `YoSQL` to access your database. It does not require any dependencies outside from standard JDK classes exposed by the [JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity) API.

### Option: 'R2DBC'

The `R2DBC` based implementation.

### Option: 'SPRING_JDBC'

The `spring-jdbc` based implementation. It uses the `JdbcTemplate` or `NamedParameterJdbcTemplate` class to execute SQL statements and map result to your domain objects.

### Option: 'JOOQ'

The `jOOQ` based implementation. It uses the `DSLContext` class to execute SQL statements and map results to your domain objects.

### Option: 'JPA'

The `JPA` based implementation. It uses the `EntityManager` class to execute SQL statements and map results to your domain objects.

## Related Options

- [annotationApi](../annotationapi/): The annotation API to use.
- [loggingApi](../loggingapi/): The logging API to use.

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --api-dao-api=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --dao-api=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `daoApi` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  api {
    daoApi.set(configValue)
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
  api {
    daoApi = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `daoApi` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <api>
          <daoApi>configValue</daoApi>
        </api>
      </configuration>
    </plugin>
  </plugins>
</build>
```
