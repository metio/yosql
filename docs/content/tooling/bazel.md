---
title: Bazel
date: 2019-06-16T18:22:51+02:00
menu:
  main:
    parent: Tooling
categories:
  - Tooling
tags:
  - Bazel
---

[bazel](https://bazel.build/) users can use the `yosql-cli` in their builds by following the steps. Replace placeholders with values from the actual release notes.

1. Add git repository to your `WORKSPACE`:

```
git_repository(
    name = "yosql",
    remote = "https://github.com/metio/yosql.git",
    tag = "0.0.1-bazel",
)
```

2. Write .sql files in a directory of your choice (e.g. `persistence`)

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

3. Declare a `filegroup` that contains all of your SQL files:

```
filegroup(
  name = "your-sql-files",
  srcs = glob(["persistence/**/*.sql"]),
)
```

4. Generate utilities first since they dont change that often:

```
genrule(
  name = "your-utilities",
  srcs = [":your-sql-files"],
  outs = [
    "com/example/persistence/util/ResultRow.java",
    "com/example/persistence/util/ResultState.java",
    "com/example/persistence/util/FlowState.java",
  ],
  cmd = """
    $(location @yosql//yosql-cli) generate utilities
  """,
  tools = ["@yosql//yosql-cli"],
)
```

5. Generate converters next, in order to connect user code to repositories


```
genrule(
  name = "your-converters",
  srcs = [":your-sql-files"],
  outs = [
    "com/example/persistence/converter/ToResultRowConverter.java",
  ],
  cmd = """
    $(location @yosql//yosql-cli) generate converters
  """,
  tools = ["@yosql//yosql-cli"],
)
```


6. Generate repositories last, since they'll change everytime a SQL file changes as well:

```
genrule(
  name = "your-repositories",
  srcs = [":your-sql-files"],
  outs = [
    "com/example/persistence/UserRepository.java",
    "com/example/persistence/ItemRepository.java",
  ],
  cmd = """
    $(location @yosql//yosql-cli) generate repositories
  """,
  tools = ["@yosql//yosql-cli"],
)
```

8. Depend on the generated sources by using the target name of the generated code in the `srcs` of another rule.
