---
title: dataSource
date: 2022-04-14
menu:
  main:
    parent: Jdbc
categories:
  - Configuration
tags:
  - jdbc
---

The name for a DataSource variable.

## Configuration Options

## Related Options

- [batch](../batch/): The name for a Batch variable.
- [columnCount](../columncount/): The name for a ColumnCount variable.
- [columnLabel](../columnlabel/): The name for a ColumnLabel variable.
- [connection](../connection/): The name for a Connection variable.
- [defaultConverter](../defaultconverter/): The default converter to use, if no other is specified on a query itself.
- [flowStateClassName](../flowstateclassname/): The class name of the flow-state class
- [indexSuffix](../indexsuffix/): The name suffix to add for index lookup tables.
- [indexVariable](../indexvariable/): The name for a index variable.
- [jdbcIndexVariable](../jdbcindexvariable/): The name for a JDBC index variable.
- [list](../list/): The name for a List variable.
- [metaData](../metadata/): The name for a MetaData variable.
- [rawSuffix](../rawsuffix/): The name suffix to add for raw SQL statements.
- [resultRowClassName](../resultrowclassname/): The class name of the result-row class
- [resultSet](../resultset/): The name for a ResultSet variable.
- [resultStateClassName](../resultstateclassname/): The class name of the result-state class
- [row](../row/): The name for a row variable.
- [statement](../statement/): The name for a Statement variable.
- [userTypes](../usertypes/): The converters configured by the user.
- [utilityPackageName](../utilitypackagename/): The package name for all utility classes.

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --jdbc-data-source=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --data-source=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `dataSource` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  jdbc {
    dataSource.set(configValue)
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
  jdbc {
    dataSource = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `dataSource` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <jdbc>
          <dataSource>configValue</dataSource>
        </jdbc>
      </configuration>
    </plugin>
  </plugins>
</build>
```
