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
    "com/example/persistence/AdminRepository.java",
    "com/example/persistence/UserRepository.java",
    "com/example/persistence/ItemRepository.java",
    "com/example/persistence/util/ResultRow.java",
    "com/example/persistence/util/ResultState.java",
    "com/example/persistence/util/FlowState.java",
    "com/example/persistence/converter/ToResultRowConverter.java",
  ],
  cmd = """
    $(location //yosql-cli) \
      --inputBaseDirectory yosql-example/src/main/yosql \
      --outputBaseDirectory $(@D) \
      --resultRowConverters itemConverter:com.example.app.converter.ToItemConverter:com.example.app.model.Item \
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
    "@com_zaxxer_hikaricp//jar",
    "@com_h2database_h2//jar",
    "@org_postgresql_postgresql//jar",
    "@mysql_mysql_connector_java//jar",
    "@de_vandermeer_asciitable//jar",
    "@de_vandermeer_ascii_utf_themes//jar",
    "@de_vandermeer_skb_interfaces//jar",
    "@org_apache_commons_commons_lang3//jar",
    "@org_antlr_st4//jar",
    "@org_reactivestreams_reactive_streams//jar",
    "@io_reactivex_rxjava2_rxjava2//jar",
    "@org_slf4j_slf4j_api//jar",
    "@ch_qos_logback_logback_classic//jar",
    "@ch_qos_logback_logback_core//jar",
  ],
)
