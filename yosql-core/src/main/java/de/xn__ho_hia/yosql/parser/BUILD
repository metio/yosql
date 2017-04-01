filegroup(
  name = "yosql-core-parser-raw-sources",
  srcs = glob(["*.java"]),
)

java_library(
  name = "yosql-core-parser",
  srcs = [
    ":yosql-core-parser-raw-sources",
  ],
  visibility = [
    "//yosql-core:__pkg__",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:__pkg__",
  ],
  deps = [
    "//external:javax_inject",
    "//external:snakeyaml",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/model:yosql-core-model",
  ],
  exports = [
    "//external:snakeyaml",
  ],
)
