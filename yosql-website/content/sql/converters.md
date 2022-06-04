---
title: Converters
date: 2019-07-07T14:27:54+02:00
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - converters
---

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
Optional<User> findUsers()
List<User> findUsers()
Stream<User> findUsersStream()
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
