filegroup(
  name = "testutils-raw-sources",
  srcs = glob(["src/main/java/**/*.java"]),
)

java_library(
  name = "yosql-testutils",
  srcs = [":testutils-raw-sources"],
  visibility = ["//visibility:public"],
  deps = [
    "@org_junit_platform_junit5_platform_commons//jar",
    "@org_junit_jupiter_junit5_jupiter_api//jar",
    "//yosql-core"
  ],
)
