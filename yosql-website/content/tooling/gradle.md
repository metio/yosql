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

[Gradle](https://gradle.org/) projects can use the `yosql-tooling-gradle` plugin to use `YoSQL` in their builds. The following steps show how a basic setup looks like. In case you are looking for more details, check out the configuration section further down below.

1. Add the plugin to your `build.gradle.kts`:
    ```kotlin
    plugins {
        id("wtf.metio.yosql")
    }
    ```
2. Add .sql files in `src/main/yosql` and write SQL statements into them.
    ```
    <project_root>/
    ├── build.gradle.kts
    └── src/
        └── main/
            └── yosql/
                └── domainObject/
                    ├── yourQuery.sql
                    └── changeYourData.sql
                └── aggregateRoot/
                    ├── anotherQuery.sql
                    └── addData.sql
    ```
3. Execute the `yosql` task (or just run `gradle build`) to generate the Java code.
