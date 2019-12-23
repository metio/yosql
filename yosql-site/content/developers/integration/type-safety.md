---
title: "Type Safety"
date: 2019-06-16T18:53:54+02:00
draft: true
---

[source]
----
<project_root>/
└── src/
    └── main/
        └── yosql/
            └── user/
                ├── findUser.sql
                └── getAllUsers.sql
----

The `findUser.sql` file:

[source, sql]
----
SELECT  *
FROM    users
WHERE   id = :userId
----

The type of the generated `userId` parameter will default to `java.lang.Object` as long as no other information is 
given. YoSQL does not parse your database schema, nor does it somehow infer the type of `userId` using some smart 
algorithm. Instead it relies on its users to do the work (sorry!).

We can change the type of `userId` by adding a front matter to the statement that looks like this:

[source, sql]
----
--
-- parameters:
--   - name: userId
--     type: int
--
SELECT  *
FROM    users
WHERE   id = :userId
----

After re-generating the code, `userId` will now be of type `int`. More complex types as possible as well, as long as 
you use their fully qualified name, e.g. `java.lang.String` or `your.custom.domain.Entity`.
