filegroup(
  name = "yosql-cli-dagger-raw-sources",
  srcs = glob(["*.java"]),
)

java_library(
  name = "yosql-cli-dagger",
  srcs = [":yosql-cli-dagger-raw-sources"],
  visibility = ["//yosql-cli:__subpackages__"],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@net_sf_jopt_simple//jar",
    "@org_slf4j_slf4j_api//jar",
    "@ch_qos_cal10n_cal10n_api//jar",
    "@ch_qos_logback_logback_classic//jar",
    "//yosql-core",
    "//yosql-core:model",
    "//yosql-core:parser",
    "//yosql-core:dagger",
    "//yosql-core:generator-dao",
    "//yosql-core:generator-logging",
    "//yosql-core:generator-utils",
    "//yosql-cli/src/main/java/de/xn__ho_hia/yosql/cli/i18n:yosql-cli-i18n",
    "//yosql-cli/src/main/java/de/xn__ho_hia/yosql/cli/parser:yosql-cli-parser",
  ],
)
