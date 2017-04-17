filegroup(
  name = "yosql-example-raw-sources",
  srcs = glob(["src/main/java/**/*.java"]),
)
filegroup(
  name = "yosql-example-sql-files",
  srcs = glob(["src/main/yosql/**/*.sql"]),
)

genrule(
  name = "yosql-example-generated",
  srcs = [":yosql-example-sql-files"],
  outs = [
    "com/example/persistence/SchemaRepository.java",
    "com/example/persistence/CompanyRepository.java",
    "com/example/persistence/PersonRepository.java",
    "com/example/persistence/util/ResultRow.java",
    "com/example/persistence/util/ResultState.java",
    "com/example/persistence/util/FlowState.java",
    "com/example/persistence/converter/ToResultRowConverter.java",
  ],
  cmd = """
    ./$(location //yosql-cli) \
      --inputBaseDirectory yosql-example/src/main/yosql \
      --outputBaseDirectory $(@D) \
      --logLevel off
  """,
  tools = ["//yosql-cli"],
)

java_binary(
  name = "yosql-example",
  main_class = "com.example.app.ExampleApp",
  srcs = [
    ":yosql-example-raw-sources",
    ":yosql-example-generated",
  ],
  visibility = ["//visibility:public"],
  deps = [
    "//external:h2",
    "//external:postgresql",
    "//external:mysql",
    "//yosql-core",
  ],
)
