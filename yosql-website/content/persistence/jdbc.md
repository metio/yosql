---
title: JDBC
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Persistence APIs
categories:
  - Persistence
tags:
  - JDBC
---

The `javax.sql` based implementation of `YoSQL` to access your database. It does not require any dependencies outside from standard JDK classes exposed by the [JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity) API. The available configuration is listed split into multiple pages which are listed at the bottom of this page.

## Tooling

In order to use the JDBC API in generated code, set the `persistenceApi` option to `JDBC`. Further configuration can be performed using the dedicated `jdbc` section.

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](/tooling/maven/).

{{< maven/persistence/jdbc/index >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  apis {
    persistenceApi = JDBC
  }
  jdbc {
    ... jdbc configuration
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --apis-dao-api=JDBC
```

The shorter form is available as well:

```shell
$ yosql --dao-api=JDBC
```

## Manual Converters

The JDBC API offers no built-in object mapping mechanism. In order to use high level types of your domain, use a converter to map results to your types.

```sql
-- results:
--   converter: my.own.UserConverter
SELECT  *
FROM    users
```

You can either specify the fully-qualified name of the converter or use its alias. The result type is read from the converter configuration as well. This in turn changes the generated code into this:

```java
// uses "User" as type argument for all return types
List<User> findUsers()
Stream<User> findUsersStreamEager()
Stream<User> findUsersStreamLazy()
Flowable<User> findUsersFlow()
```

`my.own.UserConverter` could look like this:

```java
package my.own;

import java.sql.ResultSet;
import java.sql.SQLException;

import my.own.User;
import my.own.persistence.util.ResultState;

public class UserConverter {

    public final User apply(final ResultState result) throws SQLException {
        final ResultSet resultSet = result.getResultSet();
        final User pojo = new User();
        pojo.setId(resultSet.getInt("id"));
        pojo.setName(resultSet.getString("name"));
        return pojo;
    }

}
```
