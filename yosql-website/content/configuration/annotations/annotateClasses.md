---
title: annotateClasses
date: 2022-04-15
menu:
  main:
    parent: Annotations
categories:
  - Configuration
tags:
  - annotations
  - classes
---

Controls whether Generated annotations should be added to the generated classes.

## Configuration Options

### Option: 'false'

The default value of the `annotateClasses` configuration option is `false`. Setting the option to `false` therefore produces the same code generated as the default configuration.

```java
package com.example.persistence;

public class SomeRepository {

    // ... rest of generated code

}

```

### Option: 'true'

Changing the `annotateClasses` configuration option to `true` adds the `@Generated` annotation to every generated classes. Its is possible to configure each value individually using other config options.

```java
package com.example.persistence;

import javax.annotation.processing.Generated;

@Generated(
    value = "YoSQL",
    date = "<current_timestamp>",
    comments = "DO NOT MODIFY - automatically generated by YoSQL"
)
public class SomeRepository {

    // ... rest of generated code (same as above)

}

```

## Related Options

- [annotateFields](../annotatefields/): Controls whether Generated annotations should be added to the generated fields.
- [annotateMethods](../annotatemethods/): Controls whether Generated annotations should be added to the generated methods.
- [classComment](../classcomment/): Sets the comment used for annotated classes.
- [classMembers](../classmembers/): The annotation members to use for classes.
- [fieldComment](../fieldcomment/): Sets the comment used for annotated fields.
- [fieldMembers](../fieldmembers/): The annotation members to use for fields.
- [generatorName](../generatorname/): The name of the code generator
- [methodComment](../methodcomment/): Sets the comment used for annotated methods.
- [methodMembers](../methodmembers/): The annotation members to use for methods.

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --annotations-annotate-classes=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --annotate-classes=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `annotateClasses` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  annotations {
    annotateClasses.set(configValue)
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
  annotations {
    annotateClasses = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `annotateClasses` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <annotations>
          <annotateClasses>configValue</annotateClasses>
        </annotations>
      </configuration>
    </plugin>
  </plugins>
</build>
```