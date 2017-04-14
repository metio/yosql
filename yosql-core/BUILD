filegroup(
  name = "model-raw-sources",
  srcs = glob(["src/main/java/**/model/*.java"]),
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
  name = "model",
  srcs = [
    ":model-raw-sources",
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

filegroup(
  name = "utils-raw-sources",
  srcs = glob(["src/main/java/**/yosql/utils/*.java"]),
)

java_library(
  name = "utils",
  srcs = [
    ":utils-raw-sources",
  ],
  deps = [
    "//external:javax_inject",
  ],
)

filegroup(
  name = "parser-raw-sources",
  srcs = glob(["src/main/java/**/parser/*.java"]),
)

java_library(
  name = "parser",
  srcs = [
    ":parser-raw-sources",
  ],
  deps = [
    "//external:javax_inject",
    "//external:snakeyaml",
    ":model",
  ],
  exports = [
    "//external:snakeyaml",
  ],
)

filegroup(
  name = "generator-api-raw-sources",
  srcs = glob(["src/main/java/**/generator/*.java"]),
)
filegroup(
  name = "generator-logging-raw-sources",
  srcs = glob(["src/main/java/**/generator/logging/*.java"]),
)
filegroup(
  name = "generator-helpers-raw-sources",
  srcs = glob(["src/main/java/**/generator/helpers/*.java"]),
)
filegroup(
  name = "generator-raw-jdbc-raw-sources",
  srcs = glob(["src/main/java/**/generator/raw_jdbc/*.java"]),
)
filegroup(
  name = "generator-utils-raw-sources",
  srcs = glob(["src/main/java/**/generator/utils/*.java"]),
)

java_library(
  name = "generator-api",
  srcs = [
    ":generator-api-raw-sources",
  ],
  deps = [
    "//external:javax_inject",
    ":model",
  ],
  exports = [
    "//external:javax_inject",
    ":model",
  ],
)
java_library(
  name = "generator-helpers",
  srcs = [
    ":generator-helpers-raw-sources",
  ],
  deps = [
    "//external:javapoet_type_guesser",
    "//external:rxjava2",
    ":parser",
    ":generator-api",
  ],
  exports = [
    "//external:javapoet_type_guesser",
    "//external:rxjava2",
    "//external:reactive_streams",
  ],
)
java_library(
  name = "generator-logging",
  srcs = [
    ":generator-logging-raw-sources",
  ],
  deps = [
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
    ":generator-api",
    ":generator-helpers",
  ],
  exports = [
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
  ],
)
java_library(
  name = "generator-raw-jdbc",
  srcs = [
    ":generator-raw-jdbc-raw-sources",
  ],
  deps = [
    ":generator-api",
    ":generator-helpers",
  ],
)
java_library(
  name = "generator-utils",
  srcs = [
    ":generator-utils-raw-sources",
  ],
  deps = [
    ":generator-api",
    ":generator-helpers",
  ],
)

filegroup(
  name = "i18n-raw-sources",
  srcs = glob(["src/main/java/**/i18n/*.java"]),
)
filegroup(
  name = "config-options",
  srcs = ["src/main/resources/configuration-options_en.properties"],
)

java_library(
  name = "i18n",
  srcs = [
    ":i18n-raw-sources",
  ],
  resources = [":config-options"],
  deps = [
    "//external:cal10n",
  ],
  exports = [
    "//external:cal10n",
  ],
)

filegroup(
  name = "main-raw-sources",
  srcs = glob(["src/main/java/**/YoSql*.java"]),
)

java_library(
  name = "yosql-core",
  srcs = [
    ":main-raw-sources",
  ],
  visibility = ["//visibility:public"],
  deps = [
    ":model",
    ":i18n",
    ":utils",
    ":parser",
    ":generator-api",
    ":generator-helpers",
    ":generator-logging",
    ":generator-raw-jdbc",
    ":generator-utils",
  ],
  exports = [
    ":model",
    ":i18n",
    ":utils",
    ":parser",
    ":generator-api",
    ":generator-helpers",
    ":generator-logging",
    ":generator-raw-jdbc",
    ":generator-utils",
  ],
)

load("@bazel_build_process//java:junit5_test.bzl", "junit5")

[junit5(
  file = file,
  deps = [
    ":model",
    "//yosql-testutils",
  ],
) for file in glob(["src/test/java/**/model/*Test.java"])]
[junit5(
  file = file,
  deps = [
    ":i18n",
    "//yosql-testutils",
  ],
) for file in glob(["src/test/java/**/i18n/*Test.java"])]
