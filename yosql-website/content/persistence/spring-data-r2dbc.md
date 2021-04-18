---
title: Spring-Data R2DBC
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Persistence APIs
categories:
  - Persistence
tags:
  - R2DBC
  - Spring-Data
  - Spring
---

The `spring-data-r2dbc` based implementation. It generates Spring-Data repository interfaces that use the entire SQL statements as annotation value.

| Read | Write | Call | Batch Write | Rxjava | Stream Eager | Stream Lazy |
|------|-------|------|-------------|--------|--------------|-------------|
| ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](/tooling/maven/).

{{< maven/persistence/spring-data-r2dbc/index >}}
