---
title: Reactive
date: 2019-07-07T14:27:54+02:00
menu:
  main:
    parent: SQL
categories:
  - Integration
tags:
  - rxjava
  - reactor
  - r2dbc
---

`YoSQL` supports several reactive APIs like **rxJava** or **reactor**.

The `yosql-tooling-maven` plugin for Maven automatically enables reactive methods like the following in case a reactive API was detected as a dependency of the current project.

```java
// lazily loads all rows in a stateful flow which closes automatically
Flowable<ResultRow> findUserFlow(Object userId)
```
