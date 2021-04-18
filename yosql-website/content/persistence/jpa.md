---
title: JPA
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Persistence APIs
categories:
  - Persistence
tags:
  - JPA
---

The `JPA` based implementation. It uses the `EntityManager` class to execute SQL statements and map results to your domain objects.

| Read | Write | Call | Batch Write | Rxjava | Stream Eager | Stream Lazy |
|------|-------|------|-------------|--------|--------------|-------------|
| ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](/tooling/maven/).

{{< maven/persistence/jpa/index >}}
