---
title: Persistence APIs
date: 2020-04-13
menu: main
---

`YoSQL` supports the following persistence APIs to interact with a database:

| Name                                      | Read | Write | Call | Batch Write | Rxjava | Stream Eager | Stream Lazy |
|-------------------------------------------|------|-------|------|-------------|--------|--------------|-------------|
| [EBean](./ebean)                          | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Fluent JDBC](./fluent-jdbc)              | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [JDBC](./jdbc)                            | ✔    | ✔     | ✔    | ✔           | ✔      | ✔            | ✔           |
| [JDBI](./jdbi)                            | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [jOOQ](./jooq)                            | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [JPA](./jpa)                              | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [MyBatis](./mybatis)                      | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Pyranid](./pyranid)                      | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [R22DBC](./r2dbc)                         | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [SansOrm](./sansorm)                      | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Spring-Data JDBC](./spring-data-jdbc)    | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Spring-Data JPA](./spring-data-jpa)      | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Spring-Data R2DBC](./spring-data-r2dbc)  | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Spring-JDBC](./spring-jdbc)              | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Sql2o](./sql2o)                          | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Vert.x PG Client](./vertx-pg-client)     | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| [Vert.x JDBC Client](./vertx-jdbc-client) | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
