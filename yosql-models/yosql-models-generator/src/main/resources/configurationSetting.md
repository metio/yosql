---
title: {{setting.name}}
date: {{currentDate}}
menu:
  main:
    parent: {{group.name}}
categories:
  - Configuration
tags:
  - {{#lower}}{{group.name}}{{/lower}}
{{#setting.tags}}
  - {{.}}
{{/setting.tags}}
generated: true
---

{{setting.description}}

{{#hasExplanation}}
{{setting.explanation}}

{{/hasExplanation}}
## Configuration Options

{{#setting.examples}}
### Option: '{{value}}'

{{description}}

{{#result}}
```java
{{{.}}}
```

{{/result}}
{{/setting.examples}}
{{#hasRelatedSettings}}
## Related Options

{{#relatedSettings}}
- [{{name}}](../{{#lower}}{{name}}{{/lower}}/): {{description}}
{{/relatedSettings}}
{{/hasRelatedSettings}}

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --{{#lower}}{{group.name}}{{/lower}}-{{#kebab}}{{setting.name}}{{/kebab}}=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --{{#kebab}}{{setting.name}}{{/kebab}}=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `{{setting.name}}` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "{{yosqlVersion}}"
}

yosql {
  {{#lower}}{{group.name}}{{/lower}} {
    {{setting.name}}.set(configValue)
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
    {{setting.name}} = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `{{setting.name}}` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>{{yosqlVersion}}</version>
      <configuration>
        <{{#lower}}{{group.name}}{{/lower}}>
          <{{setting.name}}>configValue</{{setting.name}}>
        </{{#lower}}{{group.name}}{{/lower}}>
      </configuration>
    </plugin>
  </plugins>
</build>
```
