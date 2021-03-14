---
title: APIs
date: 2020-04-13
menu: main
---

`YoSQL` supports the following APIs to interact with a database:

| Name                                      | Read | Write | Call | Batch Write | Rxjava | Stream Eager | Stream Lazy |
|-------------------------------------------|------|-------|------|-------------|--------|--------------|-------------|
| [JDBC](./jdbc)                            | ✔    | ✔     | ✔    | ✔           | ✔      | ✔            | ✔           |
| [Spring-JDBC](./spring-jdbc)              | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Spring-Data JDBC](./spring-data-jdbc)    | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [jOOQ](./jooq)                            | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [JPA](./jpa)                              | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |

- https://github.com/eclipse-vertx/vertx-sql-client
- TODO: https://github.com/speedment/speedment
- TODO: https://www.sql2o.org/
