filegroup(
  name = "testutils-raw-sources",
  srcs = glob(["src/main/java/**/*.java"]),
)

java_library(
  name = "yosql-testutils",
  srcs = [":testutils-raw-sources"],
  visibility = ["//visibility:public"],
  deps = [
    "//external:junit5_jupiter_api",
  ],
)
