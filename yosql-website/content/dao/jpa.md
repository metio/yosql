---
title: JPA
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Persistence APIs
categories:
  - DAO
tags:
  - JPA
---

The `JPA` based implementation. It uses the `EntityManager` class to execute SQL statements and map results to your domain objects.

| Read | Write | Call | Batch Write | Rxjava | Stream Eager | Stream Lazy |
|------|-------|------|-------------|--------|--------------|-------------|
| ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
