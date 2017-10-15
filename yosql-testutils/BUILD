filegroup(
  name = "testutils-raw-sources",
  srcs = glob(["src/main/java/**/*.java"]),
)

java_library(
  name = "yosql-testutils",
  srcs = [":testutils-raw-sources"],
  visibility = ["//visibility:public"],
  deps = [
    "@junit_platform_commons//jar",
    "@junit_jupiter_api//jar",
    "@ch_qos_cal10n_cal10n_api//jar",
    "@slf4j_api//jar",
    "@javapoet//jar",
    "@apiguardian_api//jar",
    "//yosql-core:model"
  ],
)
