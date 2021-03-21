---
title: SQL
date: 2020-04-13
menu:
  main:
    weight: 140
---

This part of the documentation is intended for **developers** looking for information on how to write SQL statements in 
their own project.

In order to learn how to structure your `.sql` files, take a look at the [repositories](./repositories/) section. Each of your `.sql` files can have an optional [front matter](./frontmatter/) part that can be used to fine-tine a single SQL statement. Depending on the persistence API you have picked, SQL statements can be configured to return types from your project in order to improve the type-safety of your code. There is some general information about those [converters](./converters/), however each [persistence API](../persistence/) usually has its own little quirks that you can read upon in their individual sections.
