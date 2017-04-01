filegroup(
  name = "yosql-core-model-raw-sources",
  srcs = glob(["*.java"]),
)

java_plugin(
    name = "auto_plugin",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    deps = ["@com_google_auto_value_auto_value//jar"],
)

java_library(
    name = "auto",
    exported_plugins = [":auto_plugin"],
    exports = ["@com_google_auto_value_auto_value//jar"],
)

java_library(
  name = "yosql-core-model",
  srcs = [
    ":yosql-core-model-raw-sources",
  ],
  visibility = [
    "//yosql-core:__pkg__",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/parser:__pkg__",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:__pkg__",
    "//yosql-core/src/test/java/de/xn__ho_hia/yosql/model:__pkg__",
  ],
  deps = [
    "//external:javax_annotation",
    "//external:javapoet",
    ":auto",
  ],
  exports = [
    "//external:javax_annotation",
    "//external:javapoet",
  ],
)
