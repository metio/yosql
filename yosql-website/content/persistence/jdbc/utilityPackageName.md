---
title: utilityPackageName
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: JDBC
categories:
  - Configuration
tags:
  - utilities
  - package
  - name
---

The base package name for JDBC utility classes in generated code. Defaults to `com.example.persistence.util`.

## Configuration Options

### Option: 'com.example.persistence.util'

The default value of the `utilityPackageName` configuration option is `com.example.persistence.util`. Setting the option to `com.example.persistence.util` therefore produces the same code generated as the default configuration without any configuration option set. It produces code similar to this:

```java
package com.example.persistence.util;

public class SomeUtility {

    // ... rest of generated code

}
```

### Option: 'your.own.domain.util'

Changing the `utilityPackageName` configuration option to `your.own.domain.util` generates the following code instead:

```java
package your.own.domain.util;

public class SomeUtility {

    // ... rest of generated code (same as above)

}
```

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
          <jdbc>
            <utilityPackageName>your.own.domain.util</utilityPackageName>
          </jdbc>
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
  jdbc {
    utilityPackageName = "your.own.domain.util"
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --jdbc-utility-package-name=your.own.domain.util
```

The shorter form is available as well:

```shell
$ yosql --utility-package-name=your.own.domain.util
```
