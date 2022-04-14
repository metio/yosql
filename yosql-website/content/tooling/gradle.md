---
title: Gradle
date: 2019-06-16T18:23:45+02:00
menu:
  main:
    parent: Tooling
categories:
  - Tooling
tags:
  - Gradle
---

[Gradle](https://gradle.org/) projects can use the [yosql-tooling-gradle](https://plugins.gradle.org/plugin/wtf.metio.yosql) plugin to use `YoSQL` in their builds. The following steps show how a basic setup looks like. In case you are looking for more details, check out the configuration section further down below.

1. Add the [plugin](https://plugins.gradle.org/plugin/wtf.metio.yosql) to your `build.gradle(.kts)` file as describe in the Gradle plugin portal.
2. Add .sql files in `src/main/yosql` and write SQL statements into them. Take a look at the various options to [structure](/sql/structure/) your [SQL files](/sql/sql-files/).
    ```
    <project_root>/
    ├── build.gradle.kts
    ├── settings.gradle.kts
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
3. Execute the `yosql` task (or just run `gradle build`) to generate the Java code.

## Configuration

You can configure how YoSQL operates and how the generated code looks like by using the `yosql` task extension. Take a look at the [available configuration options](/configuration/) in order to see what can be configured.
