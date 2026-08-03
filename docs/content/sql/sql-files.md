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

While parsing your `.sql` files, `YoSQL` will strip the SQL comment prefix (`--`) and read the remaining text as a YAML object. The available configuration options that can be used in the front matter, are listed under [SQL statement configuration](/configuration/sql/).

### Names a parameter cannot have

A generated method declares a few variables of its own — the `Connection`, the `PreparedStatement`,
the `ResultSet`, the query, the loop counters — and a parameter cannot be called after one of them,
because both become identifiers in the same method. The full list is `LOG`, `query`, `rawQuery`,
`executedQuery`, `databaseProductName`, `action`, `exception`, `dataSource`, `connection`,
`statement`, `resultSetMetaData`, `databaseMetaData`, `resultSet`, `columnCount`, `columnLabel`,
`batch`, `list`, `jdbcIndex`, `index` and `row`.

`YoSQL` says so with the file, the statement and the parameter rather than letting Java complain
about a variable already defined. Rename it in the SQL — the name reaches no further than the
method's signature.

## Lists of values

A prepared statement has one placeholder per value, and how many values there are is only known when
the method is called. Declare a parameter as a collection and `YoSQL` builds the placeholders for it:

```sql
-- name: findTenantsByIds
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
-- parameters:
--   - name: ids
--     type: java.util.List<java.util.UUID>
--   - name: accountId
--     type: java.util.UUID
select id, slug from tenant
where id in (:ids)
  and account_id = :accountId
```

The method takes a `List<UUID>`, and the query it runs has as many placeholders between the brackets
as the caller passed. Every other parameter still lands on the placeholder the statement wrote it on,
however long the list is. `List`, `Set`, `Collection` and `Iterable` all work; an array does not,
because an array parameter is how a [batch statement](/configuration/sql/executebatchprefix/) passes
one value per execution.

An **empty** collection matches no row, which is what `in` on an empty set means. Negated it is not:
`not in` on an empty set matches *every* row, and no list of placeholders can say that — so a
statement that would hit it throws instead of quietly returning nothing.

A statement written once per database cannot expand a collection. Each vendor's SQL may place its
parameters differently, one method binds all of them, and it can only count placeholders in one
order — so that combination stops the build rather than binding the wrong values on the second
database.

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

A `.sql` file may open with a block comment, and `YoSQL` drops it:

```sql
/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: findTenant
select id from tenant where id = :id
```

However many lines it runs to, and there is nothing to configure. A block comment anywhere else in
the file stays where it is, because that is where a vendor puts an optimizer hint —
`select /*+ INDEX(tenant tenant_pkey) */ …` reaches the database intact.

Write the header as a block comment rather than as `--` lines. A `--` line at the top of a file is
front matter, and `-- SPDX-License-Identifier: 0BSD` is as good a YAML mapping as
`-- name: findTenant` — so a licence written that way would quietly become configuration.
