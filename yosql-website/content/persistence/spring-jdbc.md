---
title: Spring JDBC
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Persistence APIs
categories:
  - Persistence
tags:
  - JDBC
  - Spring
---

**WORK IN PROGRESS**

The `spring-jdbc` based implementation. It uses the `JdbcTemplate` or `NamedParameterJdbcTemplate` class to execute SQL statements and map result to your domain objects.

| Read | Write | Call | Batch Write | Rxjava | Stream Eager | Stream Lazy |
|------|-------|------|-------------|--------|--------------|-------------|
| ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
