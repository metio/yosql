filegroup(
  name = "yosql-cli-i18n-raw-sources",
  srcs = glob(["*.java"]),
)

java_library(
  name = "yosql-cli-i18n",
  srcs = [":yosql-cli-i18n-raw-sources"],
  visibility = ["//yosql-cli:__subpackages__"],
  deps = [
    "@ch_qos_cal10n_cal10n_api//jar"
  ],
  resources = [
    "//yosql-cli/src/main/resources:cli-events",
    "//yosql-cli/src/main/resources:commands",
  ],
)
