filegroup(
  name = "yosql-core-i18n-raw-sources",
  srcs = glob(["*.java"]),
)

java_library(
  name = "yosql-core-i18n",
  srcs = [
    ":yosql-core-i18n-raw-sources",
  ],
  resources = ["//yosql-core/src/main/resources:config-options"],
  visibility = [
    "//yosql-core/src/test/java/de/xn__ho_hia/yosql/i18n:__pkg__",
    "//yosql-core:__pkg__"
  ],
  deps = [
    "//external:cal10n",
  ],
  exports = [
    "//external:cal10n",
  ],
)
