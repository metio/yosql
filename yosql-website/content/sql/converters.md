---
title: Converters
date: 2019-07-07T14:29:29+02:00
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - converters
  - results
---

Changing the result type is possible with the help of a converter:

```sql
-- parameters:
--   - name: userId
--     type: int
-- results:
--   converter: my.own.UserConverter
--   resultType: my.own.User
SELECT  *
FROM    users
WHERE   id = :userId
```

Which in turn changes the generated code into this:

```java
// uses 'User' instead of 'ResultRow' & 'int' instead of 'Object'
List<User> findUser(int userId)
Stream<User> findUserStreamEager(int userId)
Stream<User> findUserStreamLazy(int userId)
Flowable<User> findUserFlow(int userId)
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
