---
title: Persistence APIs
date: 2020-04-13
menu:
  main:
    weight: 140
---

`YoSQL` supports multiple persistence APIs to interact with a database. We recommend that you pick the one that is already available in your project. In case you are starting fresh, `YoSQL` will default to the JDBC implementation because it requires no external dependencies and thus your project should be able to compile the generated code just fine. Some `YoSQL` tooling like the Maven plugin might auto-detect certain settings in your project to make your life easier, however you are always in full control and can change very aspect of the generated code.

Take a look at the available [configuration](/configuration/) options to adapt the generated code according to your needs. In order to monitor execution of your SQL statements, we recommend enabling one of the supported [logging](/logging/) APIs to add log output to the generated code. Read the [SQL](/sql/) section to learn how to write SQL statements in a `YoSQL`-enabled project.

- [EBean](./ebean/): The `Ebean` based implementation. It uses the constant `EBEAN` to identify itself. 
- [Fluent JDBC](./fluent-jdbc/): The `Fluent JDBC` based implementation. It uses the constant `FLUENT_JDBC` to identify itself.
- [JDBC](./jdbc/): The `JDBC` based implementation. It uses the constant `JDBC` to identify itself.
- [JDBI](./jdbi/): The `JDBI` based implementation. It uses the constant `JDBI` to identify itself.
- [jOOQ](./jooq/): The `jOOQ` based implementation. It uses the constant `JOOQ` to identify itself.
- [JPA](./jpa/): The `JPA` based implementation. It uses the constant `JPA` to identify itself.
- [MyBatis](./mybatis/): The `MyBatis` based implementation. It uses the constant `MYBATIS` to identify itself.
- [Pyranid](./pyranid/): The `Pyranid` based implementation. It uses the constant `PYRANID` to identify itself.
- [R2DBC](./r2dbc/): The `R2DBC` based implementation. It uses the constant `R2DBC` to identify itself.
- [SansOrm](./sansorm/): The `SansOrm` based implementation. It uses the constant `SANS_ORM` to identify itself.
- [Spring-Data JDBC](./spring-data-jdbc/): The `Spring-Data JDBC` based implementation. It uses the constant `SPRING_DATA_JDBC` to identify itself.
- [Spring-Data JPA](./spring-data-jpa/): The `Spring-Data JPA` based implementation. It uses the constant `SPRING_DATA_JPA` to identify itself.
- [Spring-Data R2DBC](./spring-data-r2dbc/): The `Spring-Data R2DBC` based implementation. It uses the constant `SPRING_DATA_R2DBC` to identify itself.
- [Spring JDBC](./spring-jdbc/): The `Spring JDBC` based implementation. It uses the constant `SPRING_JDBC` to identify itself.
- [Sql2o](./sql2o/): The `Sql2o` based implementation. It uses the constant `SQL2O` to identify itself.
- [Vert.x PG Client](./vertx-pg-client/): The `Vert.x PG Client` based implementation. It uses the constant `VERTX_PG_CLIENT` to identify itself.

## Tooling

Replace `configValue` with the constant value for whatever persistence API you want to use in the generated code.

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](/tooling/maven/).

```xml
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <apis>
            <daoApi>configValue</daoApi>
            <annotationApi>PROCESSING</annotationApi> <!-- TODO: unify the 3 configs into one api config -->
          </apis>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  apis {
    daoApi = configValue
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --apis-dao-api=configValue
```

The shorter form is available as well:

```shell
$ yosql --dao-api=configValue
```
