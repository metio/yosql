---
title: Structure
date: 2019-06-16T18:33:06+02:00
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - repositories
  - files
  - structure
---

In order to call your SQL statement, a Java class must be created that contains methods for each of your statements. `YoSQL` will try to detect which repository your SQL statements will end up in. Based on the [inputBaseDirectory](../../configuration/files/inputbasedirectory/) configuration option, your project structure could look like this: 

```
<inputBaseDirectory>/
└── user/
    └── getAllUsers.sql
```

Based on the above example, `YoSQL` will determine that you want a method called `getAllUsers` in a repository called `UserRepository`. Use the [basePackageName](../../configuration/repositories/basepackagename/) option to change the base package name for all generated repositories. Together they will form the fully qualified name `<basePackageName>.UserRepository`.

```
<inputBaseDirectory>/
└── internal/
    └── user/
        └── getAllUsers.sql
```

Nested package structures are supported as well - they are simply interpreted as subpackages, that are appended to the [basePackageName](../../configuration/repositories/basepackagename/) option to form the fully qualified name `<basePackageName>.internal.UserRepository`.

```
<inputBaseDirectory>/
  └── user/
    └── vips/
        └── findSpecialUsers.sql
    └── getAllUsers.sql
```

Nesting repositories within other repositories is supported as well - `YoSql` will create two repositories for the above example: `<basePackageName>.UserRepository` with a method called `getAllUsers` and `<basePackageName>.user.VipsRepository` with a method called `findSpecialUsers`.

```
<inputBaseDirectory>/
└── internal/
    └── user/
        └── getAllUsers.sql
└── user/
    └── findUser.sql
```

Mixing nested and non-nested repositories work as well. The above example will generate the two repositories `<basePackageName>.internal.UserRepository` and `<basePackageName>.UserRepository`.

```
<inputBaseDirectory>/
└── allQueries.sql
```

Smaller projects might just want to use a single `.sql` file that contains all of your queries. In case none of your SQL statements change their target repository in their [front matter](../frontmatter/), all queries in the above structure will end up in a class called `<basePackageName>.Repository`.

```
<inputBaseDirectory>/
└── internal/
    └── user/
        └── vips/
            └── findSpecialUsers.sql
        └── getAllUsers.sql
└── user/
    └── findUser.sql
└── lotsOfQueries.sql
```

Mixing all options is of course supported as well - we recommend using any structure that best fits to your project/team. One statement per file makes it easier to quickly find single statements, however grouping multiple statements together in one file might make sense for multiple reasons, e.g. a statement might have multiple variants based on the used database or any other set of statements that are usually changed together as a single unit.
