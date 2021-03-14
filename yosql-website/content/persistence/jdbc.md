---
title: JDBC
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Persistence APIs
categories:
  - DAO
tags:
  - JDBC
---

The `javax.sql` based implementation. It does not require any dependencies outside from standard JDK classes.

| Read | Write | Call | Batch Write | Rxjava | Stream Eager | Stream Lazy |
|------|-------|------|-------------|--------|--------------|-------------|
| ✔    | ✔     | ✔    | ✔           | ✔      | ✔            | ✔           |