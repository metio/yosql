---
title: classMembers
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Annotations
categories:
  - Configuration
tags:
  - annotations
  - classes
---

Controls which `@Generated` annotation members should be added to generated classes. Defaults to `WITHOUT_DATE` which uses all members except `date` in order to support reproducible builds (otherwise the generated classes would change on each generation).

## Configuration Options

### Option: 'WITHOUT_DATE'

The default value of the `classMembers` configuration option is `WITHOUT_DATE`. Setting the option to `WITHOUT_DATE` therefore produces the same code generated as the default configuration.

```java
package com.example.persistence;

import javax.annotation.processing.Generated;

@Generated(
    value = "YoSQL",
    comments = "DO NOT MODIFY - automatically generated by YoSQL"
)
public class SomeRepository {

    // ... rest of generated code

}
```

### Option: 'ALL'

Changing the `classMembers` configuration option to `ALL` outputs all annotation members.

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

### Option: 'NONE'

Changing the `classMembers` configuration option to `NONE` outputs no annotation members.

```java
package com.example.persistence;

import javax.annotation.processing.Generated;

@Generated
public class SomeRepository {

    // ... rest of generated code (same as above)

}
```

### Option: 'VALUE'

Changing the `classMembers` configuration option to `VALUE` outputs only the `value` member.

```java
package com.example.persistence;

import javax.annotation.processing.Generated;

@Generated(
    value = "YoSQL"
)
public class SomeRepository {

  // ... rest of generated code (same as above)

}
```

### Option: 'DATE'

Changing the `classMembers` configuration option to `DATE` outputs only the `date` member.

```java
package com.example.persistence;

import javax.annotation.processing.Generated;

@Generated(
    date = "<current_timestamp>"
)
public class SomeRepository {

  // ... rest of generated code (same as above)

}
```

### Option: 'COMMENT'

Changing the `classMembers` configuration option to `COMMENT` outputs only the `comment` member.

```java
package com.example.persistence;

import javax.annotation.processing.Generated;

@Generated(
    comment = "DO NOT MODIFY - automatically generated by YoSQL"
)
public class SomeRepository {

  // ... rest of generated code (same as above)

}
```

## Related Options

- [annotateClasses](../annotateclasses): Controls whether the `@Generated` annotation should be added at all.
- [classAnnotation](../classannotation): Controls which `@Generated` annotation should be used.
- [classComment](../classcomment): Controls the comment used in the `@Generated` annotation.
- [generatorName](../generatorname): Controls the value used in the `@Generated` annotation.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](../../../tooling/maven).

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <annotations>
            <classMembers>ALL</classMembers>
          </annotations>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](../../../tooling/gradle).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  annotations {
    classMembers = ALL
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](../../../tooling/bazel).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../../tooling/cli).

```shell
$ yosql --annotations-class-members=ALL
```

The shorter form is available as well:

```shell
$ yosql --class-members=ALL
```