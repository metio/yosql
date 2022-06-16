---
title: SQL Files
date: 2019-07-07T14:27:54+02:00
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - sql
  - files
---

Writing `.sql` files is the essential work that needs to be done in order to use `YoSQL`. Each file can contain multiple SQL statements. Each statement has its own configuration and metadata attached to it.

## Statement Type

`YoSQL` supports the tree types of SQL statements and is able to generate code for them: `READING` for SQL statements that read data, `WRITING` for SQL statements that write data, and `CALLING` for SQL statements that call stored procedures.

In order to correctly guess which type your statement is, `YoSQL` does not parse your SQL code, but uses the file name of your `.sql` files or the `name` front matter. It applies the following rules to determine the statement type from its name:

- All names that start with the [configured read prefixes](/configuration/repositories/allowedreadprefixes/) are assigned the `READING` type.
- All names that start with the [configured write prefixes](/configuration/repositories/allowedwriteprefixes/) are assigned the `WRITING` type.
- All names that start with the [configured call prefixes](/configuration/repositories/allowedcallprefixes/) are assigned the `CALLING` type.

SQL statements that cannot be mapped to one of the available types **are not considered** while generating code! You can always overwrite that guess with a specific [type](/configuration/sql/type/) value in your front matter. This can be useful if you want to use a special name for your statement, but don't want to adhere to the configured prefixes. On the other hand, enable [validateMethodNamePrefixes](/configuration/repositories/validatemethodnameprefixes/) to enforce that all statements are named accordingly to the configured prefixes.

## Front Matter

Each SQL statement can have an optional front matter section written in YAML that is placed inside an SQL comment. Configuration options that are specified in a front matter of an SQL statement overwrite the same option that was specified globally, e.g. in a `pom.xml`/`build.gradle` file.

```sql
-- name: someName
-- description: Retrieves a single user
-- repository: com.example.persistence.YourRepository
-- vendor: H2
-- parameters:
--   - name: userId
--     type: int
-- type: reading
-- returning: one
-- catchAndRethrow: true
SELECT  *
FROM    users
WHERE   id = :userId
```

While parsing your `.sql` files, `YoSQL` will strip the SQL comment prefix (`--`) and read the remaining text as a YAML object. The available configuration options that can be used in the front matter, can be seen [here](/configuration/sql/).

## File Extension

By default, `YoSQL` only considers files that end in `.sql`, but this can be configured using the [sqlFilesSuffix](/configuration/files/sqlfilessuffix) option. Lots of editors have built-in syntax support for SQL and they auto-enable that once you open an `.sql` file, so we recommend to stick to the default and only change if it necessary.

## File Charset

By default, `YoSQL` uses the **UTF-8** charset. In order to change this, use the [sqlFilesCharset](/configuration/files/sqlfilescharset) option.

## Statement Separator

By default, `YoSQL` uses `;` to separate multiple SQL statements within a single file. In order to change this, use the [sqlStatementSeparator](/configuration/files/sqlstatementseparator) option.

```sql
-- name: firstStatement
SELECT  *
FROM    users
WHERE   id = :userId
;

-- name: secondStatement
SELECT  *
FROM    customers
WHERE   id = :customerId
;
```

## License Headers

In case your `.sql` files contain a license header, use the [skipLines](/configuration/files/skiplines) option to skip those initial lines. Otherwise `YoSQL` will consider those lines to be part of the first statement in your `.sql` file.
