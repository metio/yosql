---
title: Spring-JDBC
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Persistence APIs
categories:
  - DAO
tags:
  - JDBC
  - Spring
---

The `spring-jdbc` based implementation. It uses the `JdbcTemplate` or `NamedParameterJdbcTemplate` class to execute SQL staements and map result to your domain objects.

| Read | Write | Call | Batch Write | Rxjava | Stream Eager | Stream Lazy |
|------|-------|------|-------------|--------|--------------|-------------|
| ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
