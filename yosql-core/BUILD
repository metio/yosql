load("@bazel_build_process//java:junit5_test.bzl", "junit5_test")

filegroup(
  name = "yosql-core-main-raw-sources",
  srcs = glob(["src/main/java/**/YoSql.java"]),
)
filegroup(
  name = "yosql-core-generator-api-raw-sources",
  srcs = glob(["src/main/java/**/generator/*.java"]),
)
filegroup(
  name = "yosql-core-generator-logging-raw-sources",
  srcs = glob(["src/main/java/**/generator/logging/*.java"]),
)
filegroup(
  name = "yosql-core-generator-helpers-raw-sources",
  srcs = glob(["src/main/java/**/generator/helpers/*.java"]),
)
filegroup(
  name = "yosql-core-generator-raw-jdbc-raw-sources",
  srcs = glob(["src/main/java/**/generator/raw_jdbc/*.java"]),
)
filegroup(
  name = "yosql-core-generator-utils-raw-sources",
  srcs = glob(["src/main/java/**/generator/utils/*.java"]),
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
    exclude = ["src/main/java/**/generator/utils/*.java"],
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
  exports = [
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
  exports = [
    "//external:javax_annotation",
    "//external:javapoet",
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
    "//external:javax_inject",
    "//external:snakeyaml",
    ":yosql-core-model",
  ],
  exports = [
    "//external:snakeyaml",
  ],
)
java_library(
  name = "yosql-core-generator-api",
  srcs = [
    ":yosql-core-generator-api-raw-sources",
  ],
  deps = [
    "//external:javax_inject",
    ":yosql-core-model",
  ],
  exports = [
    "//external:javax_inject",
    ":yosql-core-model",
  ],
)
java_library(
  name = "yosql-core-generator-helpers",
  srcs = [
    ":yosql-core-generator-helpers-raw-sources",
  ],
  deps = [
    "//external:javapoet_type_guesser",
    "//external:rxjava2",
    ":yosql-core-parser",
    ":yosql-core-generator-api",
  ],
  exports = [
    "//external:javapoet_type_guesser",
    "//external:rxjava2",
    "//external:reactive_streams",
  ],
)
java_library(
  name = "yosql-core-generator-logging",
  srcs = [
    ":yosql-core-generator-logging-raw-sources",
  ],
  deps = [
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
    ":yosql-core-generator-api",
    ":yosql-core-generator-helpers",
  ],
  exports = [
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
  ],
)
java_library(
  name = "yosql-core-generator-raw-jdbc",
  srcs = [
    ":yosql-core-generator-raw-jdbc-raw-sources",
  ],
  deps = [
    ":yosql-core-generator-api",
    ":yosql-core-generator-helpers",
  ],
)
java_library(
  name = "yosql-core-generator-utils",
  srcs = [
    ":yosql-core-generator-utils-raw-sources",
  ],
  deps = [
    ":yosql-core-generator-api",
    ":yosql-core-generator-helpers",
  ],
)

java_library(
  name = "yosql-core",
  srcs = [
    ":yosql-core-main-raw-sources",
  ],
  visibility = ["//visibility:public"],
  deps = [
    ":yosql-core-model",
    ":yosql-core-i18n",
    ":yosql-core-utils",
    ":yosql-core-parser",
    ":yosql-core-generator-api",
    ":yosql-core-generator-helpers",
    ":yosql-core-generator-logging",
    ":yosql-core-generator-raw-jdbc",
    ":yosql-core-generator-utils",
  ],
  exports = [
    ":yosql-core-model",
    ":yosql-core-i18n",
    ":yosql-core-utils",
    ":yosql-core-parser",
    ":yosql-core-generator-api",
    ":yosql-core-generator-helpers",
    ":yosql-core-generator-logging",
    ":yosql-core-generator-raw-jdbc",
    ":yosql-core-generator-utils",
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
