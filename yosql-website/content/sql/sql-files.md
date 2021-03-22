---
title: SQL Files
date: 2019-07-07T14:27:54+02:00
menu:
  main:
    parent: SQL
categories:
  - Integration
tags:
  - sql
  - files
---

There are some restrictions on what counts as a `.sql` file for `YoSQL`.

## Naming

In order to correctly guess which type (reading/writing/calling) your statement is, `YoSQL` does not parse your SQL code, but uses the file name of your `.sql` files or the `name` [front matter](../frontmatter/). It applies the following rules to determine the type of a statement from its name:

- All names that start with the [configured read prefixes](../../configuration/repositories/allowedreadprefixes/) are assigned the `READING` type.
- All names that start with the [configured write prefixes](../../configuration/repositories/allowedwriteprefixes/) are assigned the `WRITING` type.
- All names that start with the [configured call prefixes](../../configuration/repositories/allowedcallprefixes/) are assigned the `CALLING` type.
- All other statements are assigned the `UNKNOWN` type. **Statements of this type are ignored while generating code.**
  
You can always overwrite that guess with a specific `type` value in your [front matter](../frontmatter/). This can be useful if you want to use a special name for your statement, but don't want to adhere to the configured prefixes. On the other hand, use [validateMethodNamePrefixes](../../configuration/repositories/validatemethodnameprefixes/) to enforce that all statements are named accordingly to the configured prefixes.

## File Extension

By default, `YoSQL` only considers files that end in `.sql`, but this can be configured using the [sqlFilesSuffix](../../configuration/files/sqlfilessuffix) option. 

## File Charset

By default, `YoSQL` uses the **UTF-8** charset. In order to change this, use the [sqlFilesCharset](../../configuration/files/sqlfilescharset) option.

## Statement Separator

By default, `YoSQL` uses `;` to separate multiple SQL statements within a single file. In order to change this, use the [sqlStatementSeparator](../../configuration/files/sqlstatementseparator) option.

## License Headers

In case your `.sql` files contain a license header, use the [skipLines](../../configuration/files/skiplines) option to skip those initial lines. Otherwise `YoSQL` will consider those lines to be part of the first statement in your `.sql` file.
