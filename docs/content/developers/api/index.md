---
title: DAO APIs
date: 2020-04-13
menu:
  main:
    parent: Developers
---

`YoSQL` supports the following APIs to interact with a database:

| Name       | Read | Write | Call | Batch Write | Rxjava | Stream Eager | Stream Lazy |
|------------|------|-------|------|-------------|--------|--------------|-------------|
| JDBC       | ✔    | ✔     | ✔    | ✔           | ✔      | ✔            | ✔           |
| SpringJDBC | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| jOOQ       | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| JPA        | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| Vert.x     | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |
| R2DBC      | ✘    | ✘     | ✘    | ✘           | ✘      | ✘            | ✘           |

- https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/data-access.html
- https://www.jooq.org/
- https://github.com/eclipse-vertx/vertx-sql-client
- https://r2dbc.io/
- TODO: https://spring.io/projects/spring-data-jdbc
- TODO: https://spring.io/projects/spring-data-r2dbc
- TODO: https://spring.io/projects/spring-data-jpa
- TODO: https://mybatis.org/mybatis-3/
- TODO: https://ebean.io/
- TODO: http://jdbi.org/
- TODO: https://github.com/speedment/speedment
- TODO: https://www.sql2o.org/
