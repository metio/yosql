load("@bazel_build_process//java:junit5_test.bzl", "junit5_test")

VERSION="123";

filegroup(
  name = "yosql-core-raw-sources",
  srcs = glob(["src/main/java/**/*.java"]),
)

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
  srcs = glob(["src/main/java/**/utils/*.java"]),
)

filegroup(
    name = "config-options",
    srcs = ["src/main/resources/configuration-options_en.properties"],
)

filegroup(
  name = "yosql-core-test-raw-sources",
  srcs = glob(["src/test/java/**/*.java"]),
)

java_plugin(
    name = "freebuilder_plugin",
    processor_class = "org.inferred.freebuilder.processor.Processor",
    deps = ["//external:freebuilder"],
)

java_library(
    name = "freebuilder",
    exported_plugins = [":freebuilder_plugin"],
    exports = ["//external:freebuilder"],
    visibility = ["//visibility:public"],
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
  visibility = ["//yosql-cli:__pkg__"],
  deps = [
    "//external:cal10n",
  ],
)
java_library(
  name = "yosql-core-model",
  srcs = [
    ":yosql-core-model-raw-sources",
  ],
  visibility = ["//yosql-cli:__pkg__"],
  deps = [
    "//external:javax_annotation",
    "//external:javapoet",
    ":auto",
  ],
)

java_library(
  name = "yosql-core",
  srcs = [
    ":yosql-core-generator-raw-sources",
    ":yosql-core-parser-raw-sources",
    ":yosql-core-utils-raw-sources",
    ":yosql-core-main-raw-sources",
  ],
  visibility = ["//visibility:public"],
  deps = [
    "//external:javapoet_type_guesser",
    "//external:javax_inject",
    "//external:javapoet",
    "//external:snakeyaml",
    "//external:rxjava2",
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
    ":yosql-core-model",
    ":yosql-core-i18n",
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
