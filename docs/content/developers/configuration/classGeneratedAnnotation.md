---
title: classGeneratedAnnotation
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Configuration
categories:
  - Configuration
tags:
  - classes
  - annotations
---

Should `@Generated` annotations be added to generated classes?. Defaults to `false`.

## Configuration Options

### Option: 'false'

The default value of the `classGeneratedAnnotation` is `false`. Setting the option to `false` therefore produces the same code generated as the default configuration.

```java
package com.example.persistence;

public class SomeRepository {

    // ... rest of generaed code

}
```

### Option: 'true'

Changing the `classGeneratedAnnotation` configuration option to `true` adds the `@Generated` annotation to every generated class. Its is possible to configure each value individually using other config options (TODO: link here?).

```java
package com.example.persistence;

import javax.annotation.processing.Generated;

@Generated(
    value = "YoSQL",
    date = "<current_timestamp>",
    comments = "DO NOT EDIT"
)
public class SomeRepository {

    // ... rest of generated code (same as above)

}
```

## Related Options

- TODO: list of related config options that somehow influence or are influenced by this options

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation 
for Maven](../tooling/maven).

```xml
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-maven-plugin</artifactId>
        <configuration>
          <classGeneratedAnnotation>true</classGeneratedAnnotation>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for 
Gradle](../tooling/gradle).

```groovy
apply plugin: 'yosql'

yosql {
  classGeneratedAnnotation: true
}
```

TODO: info for kotlin

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for 
Bazel](../tooling/bazel).

TODO: info for bazel

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../tooling/cli).

```shell
$ yosql --classGeneratedAnnotation=true
```
