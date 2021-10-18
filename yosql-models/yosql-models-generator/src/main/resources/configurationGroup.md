---
title: {{group.name}}
date: {{currentDate}}
menu:
  main:
    parent: Configuration
categories:
  - Configuration
tags:
  - {{#lower}}{{group.name}}{{/lower}}
{{#group.tags}}
  - {{.}}
{{/group.tags}}
---

{{group.description}}

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --{{#lower}}{{group.name}}{{/lower}}-config-option=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```shell
$ yosql --config-option=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The {{group.name}} group can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "{{yosqlVersion}}"
}

yosql {
  {{#lower}}{{group.name}}{{/lower}} {
    configSetting.set(configValue)
  }
}
```

or in Groovy syntax like this:

```groovy
plugins {
  id "java"
  id "wtf.metio.yosql" version "{{yosqlVersion}}"
}

yosql {
  {{#lower}}{{group.name}}{{/lower}} {
    configSetting = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The {{group.name}} group can be configured using Maven like this:

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <version>{{yosqlVersion}}</version>
        <configuration>
          <{{#lower}}{{group.name}}{{/lower}}>
            <configSetting>configValue</configSetting>
          </{{#lower}}{{group.name}}{{/lower}}>
        </configuration>
      </plugin>
    </plugins>
  </build>
```