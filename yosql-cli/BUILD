filegroup(
  name = "yosql-cli-raw-sources",
  srcs = glob(["src/main/java/**/*.java"]),
)
filegroup(
  name = "cli-events",
  srcs = ["src/main/resources/cli-events_en.properties"],
)

java_binary(
  name = "yosql-cli",
  main_class = "de.xn__ho_hia.yosql.cli.YoSqlCLI",
  srcs = [":yosql-cli-raw-sources"],
  visibility = ["//visibility:public"],
  deps = [
    "//external:jopt_simple",
    "//external:asciitable",
    "//external:ascii_utf_themes",
    "//external:skb_interfaces",
    "//external:commons_lang3",
    "//external:st4",
    "//yosql-core",
  ],
  resources = [
    ":cli-events",
  ],
)
