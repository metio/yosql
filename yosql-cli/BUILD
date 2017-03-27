filegroup(
  name = "yosql-cli-raw-sources",
  srcs = glob(["java/**/*.java"]),
)

java_binary(
  name = "yosql-cli",
  main_class = "de.xn__ho_hia.yosql.YoSqlCLI",
  srcs = [":yosql-cli-raw-sources"],
  visibility = ["//visibility:public"],
  deps = [
    "//yosql-core",
    "//external:javapoet_type_guesser",
    "//external:javax_inject",
    "//external:javax_annotation",
    "//external:javapoet",
    "//external:snakeyaml",
    "//external:rxjava2",
    "//external:reactive_streams",
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
  ],
)
