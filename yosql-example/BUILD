genrule(
    name = "example_generated",
    srcs = ["//yosql-example/src/main/resources:example_sql_files"],
    outs = [
        "com/example/persistence/SchemaRepository.java",
        "com/example/persistence/CompanyRepository.java",
        "com/example/persistence/PersonRepository.java",
        "com/example/persistence/AdminRepository.java",
        "com/example/persistence/UserRepository.java",
        "com/example/persistence/ItemRepository.java",
        "com/example/persistence/util/ResultRow.java",
        "com/example/persistence/util/ResultState.java",
        "com/example/persistence/util/FlowState.java",
        "com/example/persistence/converter/ToResultRowConverter.java",
    ],
    cmd = """
    $(location //yosql-cli/src/main/java/wtf/metio/yosql/cli:yosql-cli) \
      --inputBaseDirectory yosql-example/src/main/resources/yosql \
      --outputBaseDirectory $(@D) \
      --resultRowConverters itemConverter:com.example.app.converter.ToItemConverter:com.example.app.model.Item \
      --logLevel off
  """,
    tools = ["//yosql-cli/src/main/java/wtf/metio/yosql/cli:yosql-cli"],
    visibility = [
        "//yosql-example/src/main/java/com/example/app:__pkg__",
    ],
)
