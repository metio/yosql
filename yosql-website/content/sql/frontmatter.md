---
title: Front Matter
date: 2019-06-16T18:53:54+02:00
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - frontmatter
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
-- type: reading
-- returning: one
-- catchAndRethrow: true
SELECT  *
FROM    users
WHERE   id = :userId
```

While parsing your `.sql` files, `YoSQL` will strip the SQL comment prefix (`--`) and read the remaining text as a YAML object. The available configuration options that can be used in the front matter, can be seen [here](/configuration/sql/).

**Note**: Configuration options that are specified in a front matter of an SQL statement overwrite the same option that was specified globally, e.g. in a `pom.xml`/`build.gradle` file.
