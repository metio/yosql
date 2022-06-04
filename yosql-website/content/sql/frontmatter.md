---
title: Front Matter
date: 2019-06-16T18:53:54+02:00
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - front matter
  - YAML
---

Each SQL statement can have an optional front matter section written in YAML that is placed inside an SQL comment.

```sql
-- name: someName
-- description: Retrieves a single user
-- repository: com.example.persistence.YourRepository
-- vendor: H2
-- parameters:
--   - name: userId
--     type: int
-- type: READING
-- returning: ONE
-- catchAndRethrow: true
SELECT  *
FROM    users
WHERE   id = :userId
```

While parsing your `.sql` files, `YoSQL` will strip the SQL comment prefix (`--`) and read the remaining text as a YAML object. Its available fields are explained below.

## name

The `name` field can be used to change the name of an SQL statement. Typically, the name is auto-detected from the `.sql` file name, however if you want to place multiple SQL statements in one file, use `name` to give each statement a unique name instead of relying on the auto-numbering `YoSQL` is using by default.

```sql
-- name: someName
SELECT  *
FROM    users
WHERE   id = :userId
```

## description

The `description` field can be used to add a description to your SQL statements that will be part of generated Javadocs.

```sql
-- description: Retrieves a single user
SELECT  *
FROM    users
WHERE   id = :userId
```

## repository

The `repository` field can be used to change which repository will contain the SQL statement. By default, `YoSQL` will auto-detect the repository based on the location of the `.sql` file. In case you want to structure your `.sql` files in a specific way, but move some SQL statements to specific repositories, use `repository` together with the fully-qualified name of the target repository.

```sql
-- repository: com.example.persistence.YourRepository
SELECT  *
FROM    users
WHERE   id = :userId
```

The `repository` field takes the [basePackageName](/configuration/repositories/basepackagename/) into account, e.g. you could shorten the above example to just `YourRepository` if your base package is `com.example.persistence`. If your repository starts with the base package already, it won't be added twice. Likewise, it is possible to add any number of subpackages in front of your repository, e.g. `your.own.domain.YourRepository`.

## vendor

The `vendor` field can be used to assign a SQL statement to a specific database vendor. Use `vendor` in case you are writing SQL statements that are different for each database. We recommend, to provide a vendor-less statement as a fallback, in order to support as many databases as possible.

```sql
-- vendor: H2
SELECT  *
FROM    users
WHERE   id = :userId
;

-- vendor: Microsoft SQL Server
SELECT  *
FROM    users
WHERE   id = :userId
;

SELECT  *
FROM    users
WHERE   id = :userId
;
```

With the above configuration in place, the first statement will be executed when running against a H2 database, the second one when using Microsoft SQL Server and the last one for every other database.

## parameters

The `parameters` field can be used to change the type of input parameters. We recommend to use `parameters` on all statements that expect user inputs to improve the type-safety of your code.

```sql
-- parameters:
--   - name: userId
--     type: int
SELECT  *
FROM    users
WHERE   id = :userId
```

The above configuration will generate code that use the primitive `int` type instead of `java.lang.Object` for the parameter `userId` in generated code.

## type

The `type` field can be used to change the type of your SQL statement. By default, `YoSQL` will [auto-detect the type based on the name](../sql-files/) of your SQL statement. Possibles types are `READING`, `WRITING`, `CALLING`.

```sql
-- type: READING
SELECT  *
FROM    users
WHERE   id = :userId
```

## returning

The `returning` field can be used to change what the generated methods are returning. By default, `MULTIPLE` is used which can contain zero-to-many entities. Use `SINGLE` to get a single result, `NONE` to get no result or `CURSOR` to get a streaming cursor. These values are case insensitive, thus `multiple` is the same as `MULTIPLE`.

```sql
-- returning: NONE
UPDATE users
SET name = :name
WHERE id = :userId
```

```sql
-- returning: SINGLE
SELECT  *
FROM    users
WHERE   id = :userId
```

```sql
-- returning: MULTIPLE
SELECT  *
FROM    users
```

```sql
-- returning: CURSOR
SELECT  *
FROM    users
```
