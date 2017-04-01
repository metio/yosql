load("@bazel_build_process//java:junit5_test.bzl", "junit5_test")

filegroup(
  name = "yosql-core-main-raw-sources",
  srcs = glob(["src/main/java/**/YoSql.java"]),
)
filegroup(
  name = "yosql-core-generator-raw-sources",
  srcs = glob(["src/main/java/**/generator/**/*.java"]),
)
filegroup(
  name = "yosql-core-i18n-raw-sources",
  srcs = glob(["src/main/java/**/i18n/*.java"]),
)
filegroup(
  name = "yosql-core-model-raw-sources",
  srcs = glob(["src/main/java/**/model/*.java"]),
)
filegroup(
  name = "yosql-core-parser-raw-sources",
  srcs = glob(["src/main/java/**/parser/*.java"]),
)
filegroup(
  name = "yosql-core-utils-raw-sources",
  srcs = glob(
    ["src/main/java/**/utils/*.java"],
    exclude = ["src/main/java/**/generator/utils/*.java"]
  ),
)

filegroup(
    name = "config-options",
    srcs = ["src/main/resources/configuration-options_en.properties"],
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
    visibility = ["//visibility:public"],
)

java_library(
  name = "yosql-core-i18n",
  srcs = [
    ":yosql-core-i18n-raw-sources",
  ],
  resources = [":config-options"],
  deps = [
    "//external:cal10n",
  ],
)
java_library(
  name = "yosql-core-model",
  srcs = [
    ":yosql-core-model-raw-sources",
  ],
  deps = [
    "//external:javax_annotation",
    "//external:javapoet",
    ":auto",
  ],
)
java_library(
  name = "yosql-core-utils",
  srcs = [
    ":yosql-core-utils-raw-sources",
  ],
)
java_library(
  name = "yosql-core-parser",
  srcs = [
    ":yosql-core-parser-raw-sources",
  ],
  deps = [
    "//external:snakeyaml",
    "//external:javax_inject",
    ":yosql-core-model",
  ],
)
java_library(
  name = "yosql-core-generator",
  srcs = [
    ":yosql-core-generator-raw-sources",
  ],
  deps = [
    "//external:javax_inject",
    "//external:javapoet",
    "//external:javapoet_type_guesser",
    "//external:rxjava2",
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
    ":yosql-core-model",
    ":yosql-core-parser",
  ],
)

java_library(
  name = "yosql-core",
  srcs = [
    ":yosql-core-main-raw-sources",
  ],
  visibility = ["//visibility:public"],
  deps = [
    "//external:javax_inject",
    ":yosql-core-model",
    ":yosql-core-i18n",
    ":yosql-core-utils",
    ":yosql-core-parser",
    ":yosql-core-generator",
  ],
  exports = [
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
    "//external:cal10n",
    ":yosql-core-model",
    ":yosql-core-i18n",
    ":yosql-core-utils",
    ":yosql-core-parser",
    ":yosql-core-generator",
  ],
)

junit5_test(
  name = "de/xn__ho_hia/yosql/model/LoggingAPITest",
  deps = [":yosql-core-model"]
)

junit5_test(
  name = "de/xn__ho_hia/yosql/model/SqlTypeTest",
  deps = [":yosql-core-model"]
)
