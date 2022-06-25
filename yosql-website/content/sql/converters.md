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

The JDBC API offers no built-in object mapping mechanism. In order to use high level types of your domain, use a **converter** to map results to your types. Converters are just plain Java classes that have at least one method that converts a `java.sql.ResultSet` into some other type. Each converter has an alias to make it easier to reference different converters without specifying their fully qualified name.

## Map Converter

In case you do not want to use custom domain types, `YoSQL` provides a built-in default converter that returns a `Map<String, Object>`. That converter is declared as the [defaultConverter](../../configuration/converter/defaultconverter/) if not otherwise specified by yourself, thus newly generated code will always return `Map`s at first. You can disable generating the mapping converter by using [generateMapConverter](../../configuration/converter/generatemapconverter/). You can change its location with [mapConverterClass](../../configuration/converter/mapconverterclass/), its method name with [mapConverterMethod](../../configuration/converter/mapconvertermethod/) and its alias with [mapConverterAlias](../../configuration/converter/mapconverteralias/). Methods that use the mapping converter have a signature similar to this:

```java
Optional<Map<String, Object>> someMethod()
List<Map<String, Object>> someMethod()
Stream<Map<String, Object>> someMethod()
```

## Default Converter

If not otherwise specified, generated code will use the default converter to converter between `java.sql.ResultSet` and some type declared by the configured default converter.
In case you want to adjust the converter used by all your statements, set the [defaultConverter](../../configuration/converter/defaultconverter/) configuration option accordingly. By default, this points to the map converter mentioned above.

## Custom Converter

In order to use your own domain types in generated code, write a custom converter like in the following example and register each custom converter using the [rowConverters](../../configuration/converter/rowconverters/) configuration option:

```java
package my.own;

import java.sql.ResultSet;
import java.sql.SQLException;

import my.own.User;
import my.own.persistence.util.ResultState;

public class UserConverter {

    public User apply(ResultState result) throws SQLException {
        ResultSet resultSet = result.getResultSet();
        User pojo = new User();
        pojo.setId(resultSet.getInt("id"));
        pojo.setName(resultSet.getString("name"));
        return pojo;
    }

}
```

You can choose package, class name and method name at will. The converter method gives you full control about how you want to handle `ResultSet`s. You can use your own converter either by specifying it as the default converter mentioned above or by declaring it as a [resultRowConverter](../../configuration/sql/resultrowconverter) with either its fully qualified name or its alias like this in the front matter of your SQL statements:

```sql
-- resultRowConverter: my.own.UserConverter
SELECT  *
FROM    users
```

Generated code will now use your custom converter along with the result type configured for your converter, e.g.:

```java
Optional<User> someMethod()
List<User> someMethod()
Stream<User> someMethod()
```

**Tip**: In case you want to use custom types, but do not want to write your own converter, consider using [SimpleFlatMapper](https://simpleflatmapper.org/0102-getting-started-jdbc.html) with its built-in support for JDBC `ResultSet`s.
