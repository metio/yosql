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

The `javax.sql` based implementation. It does not require any dependencies outside from standard JDK classes.

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

    public final User asUserType(final ResultState result) throws SQLException {
        final ResultSet resultSet = result.getResultSet();
        final User pojo = new User();
        pojo.setId(resultSet.getInt("id"));
        pojo.setName(resultSet.getString("name"));
        return pojo;
    }

}
```
