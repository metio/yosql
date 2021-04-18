---
title: Maven
date: 2019-06-16T18:23:45+02:00
menu:
  main:
    parent: Tooling
categories:
  - Tooling
tags:
  - Maven
---

[Maven](https://maven.apache.org/) projects can use the `yosql-tooling-maven` plugin to use `YoSQL` in their builds. The following steps show how a basic setup looks like. In case you are looking for more details, check out the configuration section further down below.

1. Add the [plugin](https://search.maven.org/artifact/wtf.metio.yosql.tooling/yosql-tooling-maven) to your `pom.xml`:
   {{< mavenplugin >}}
2. Add .sql files in `src/main/yosql` and write SQL statements into them. Take a look at the various options to [structure](/sql/structure/) your [SQL files](/sql/sql-files/).
    ```
    <project_root>/
    ├── pom.xml
    └── src/
        └── main/
            └── yosql/
                └── domainObject/
                    ├── queryData.sql
                    └── changeYourData.sql
                └── aggregateRoot/
                    ├── findRoot.sql
                    └── addData.sql
    ```
3. Execute the `yosql:generate` goal (or just run `mvn generate-sources`) to generate the Java code.

## Build Helper Plugin

As an optional and final step to complete the setup of `YoSQL`, you can add the [build-helper-maven-plugin](https://www.mojohaus.org/build-helper-maven-plugin/) to your build in order to mark the [outputBaseDirectory](/configuration/files/outputbasedirectory/) as a source directory in your IDE like this:

```xml
<build>
    <plugins>
        ...
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>build-helper-maven-plugin</artifactId>
            <executions>
                <execution>
                    <id>add-source</id>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>add-source</goal>
                    </goals>
                    <configuration>
                        <sources>
                            <source>${project.build.directory}/generated-sources/yosql</source>
                        </sources>
                    </configuration>
                </execution>
            </executions>
        </plugin>
        ...
    </plugins>
</build>
```

## Configuration

You can configure how YoSQL operates and how the generated code looks like by using the [default Maven configuration 
mechanism](https://maven.apache.org/guides/mini/guide-configuring-plugins.html). Take a look at the [available configuration options](/configuration/) in order to see what can be configured.

{{< mavenplugin_full >}}

The `generate` goal binds itself automatically to the `generate-sources` phase. In case you want to run it in another phase, change the above example accordingly.

### Multiple Configurations

In some cases it might be preferable to generate some repositories with a specific set of configuration options while using another set for other repositories. There are several ways how this can be accomplished:

1. Place SQL files in different Maven modules.
2. Use a single module with multiple `execution` configurations.
3. Override configuration for individual SQL statements.

#### Multiple `execution`s

Make sure that multiple executions do not make use of the same .sql files. Otherwise, the executions will overwrite 
the generated code of each other. The last execution will win. Share configuration across all executions by using a single top level `configuration` block.

{{< mavenplugin_multi >}}

#### Dev/Prod Split

In case you want generate logging statements while developing, but not have any log statements in production code in order to optimize runtime performance, you can use the following setup:

{{< mavenplugin_split >}}

This setup requires no additional step during development (which you would have to do a lot), but instead allows you to specify the logging API like this `mvn -DloggingApi=NONE` in order to change the logging API per invocation of Maven, thus allow you to pass in `NONE` to disable log statements during release builds.
