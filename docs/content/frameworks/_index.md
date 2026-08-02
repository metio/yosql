---
title: Frameworks
date: 2026-08-02
menu:
  main:
    weight: 105
---

A generated repository is a plain class with a `DataSource` constructor parameter. That is all any
framework needs, and it is why there is no `YoSQL` integration module for any of them — there would
be nothing in it.

What these pages cover is the wiring: how a repository becomes a bean, how it joins the transaction
your framework opened, and — for Quarkus — how a persistence layer that resolves nothing by name
makes a native image uneventful.

- [Spring Boot](./spring-boot/)
- [Quarkus](./quarkus/)

For the mechanics underneath all of them, see [transactions](../sql/transactions/).
