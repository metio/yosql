---
title: CLI
date: 2019-06-16T18:23:45+02:00
menu:
  main:
    parent: Tooling
categories:
  - Tooling
tags:
  - CLI
---

In order to use the `YoSQL` [CLI](https://en.wikipedia.org/wiki/Command-line_interface) tooling follow these steps:

1. Download the `yosql-tooling-cli` zip file from the [latest release](https://github.com/metio/yosql/releases/latest) (or any prior version).
2. Place `bin/yosql` (or `bin/yosql.bat` on Windows) on your PATH.
3. Write `.sql` files in a directory of your choice (e.g. `/path/to/your/sql/files`).
4. Call `yosql --inputBaseDirectory /path/to/your/sql/files`.
5. Adjust the [configuration](/configuration/) according to your requirements.
