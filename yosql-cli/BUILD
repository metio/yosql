filegroup(
  name = "yosql-cli-raw-sources",
  srcs = glob(["src/main/java/**/*.java"]),
)

java_binary(
  name = "yosql-cli",
  main_class = "de.xn__ho_hia.yosql.cli.YoSqlCLI",
  srcs = [":yosql-cli-raw-sources"],
  visibility = ["//visibility:public"],
  deps = [
    "//yosql-core",
    "//external:jopt_simple",
  ],
)
