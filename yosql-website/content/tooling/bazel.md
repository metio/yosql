---
title: Bazel
date: 2019-06-16T18:23:45+02:00
menu:
  main:
    parent: Tooling
categories:
  - Tooling
tags:
  - Bazel
---

[bazel](https://bazel.build/) users can use the [yosql-tooling-cli](../cli/) in their builds by following these steps:

1. Download the `yosql-tooling-cli` zip file from the [latest release](https://github.com/metio/yosql/releases/latest) (or any prior version).
2. Use a [java_import](https://bazel.build/reference/be/java#java_import) rule to capture all `.jar` files used by `yosql-tooling-cli`

```
java_import(
    name = "yosql_tooling_cli",
    jars = [
        "lib/yosql-tooling-cli-x.y.z.jar",
        "lib/yosql-codegen-x.y.z.jar",
        "lib/yosql-models-immutables-x.y.z.jar",
        ... every other jar file from the 'lib' folder
    ],
)
```

3. Use a [java_binary](https://bazel.build/reference/be/java#java_binary) rule to create a runnable binary for bazel

```
java_binary(
    name = "yosql",
    deps = [
        ":yosql_tooling_cli",
    ],
    main_class = "wtf.metio.yosql.tooling.cli.YoSQL",
)
```

4. Write .sql files in a directory of your choice (e.g. `persistence`)

```
project/
├── WORKSPACE
├── BUILD
└── persistence/   
    └── user/
        ├── findUser.sql
        └── addUser.sql
    └── item/
        ├── queryAllItems.sql
        └── createItemTable.sql
```

5. Declare a [filegroup](https://bazel.build/reference/be/general#filegroup) that contains all of your SQL files:

```
filegroup(
  name = "your-sql-files",
  srcs = glob(["persistence/**/*.sql"]),
)
```

6. Generate Java code by calling the previously defined `java_binary`:

```
genrule(
  name = "your-repositories",
  srcs = [":your-sql-files"],
  outs = [
    "com/example/persistence/UserRepository.java",
    "com/example/persistence/ItemRepository.java",
    ... all of your generated code
  ],
  cmd = """
    $(location :yosql) generate
  """,
  tools = [":yosql"],
)
```

7. Depend on the generated sources by using the target name of the generated code in the `srcs` of another rule.
