filegroup(
  name = "model-raw-sources",
  srcs = glob(["src/main/java/**/model/*.java"]),
)
filegroup(
  name = "config-options",
  srcs = ["src/main/resources/configuration-options_en.properties"],
)
filegroup(
  name = "utils-raw-sources",
  srcs = glob(["src/main/java/**/yosql/utils/*.java"]),
)
filegroup(
  name = "generator-api-raw-sources",
  srcs = glob(["src/main/java/**/generator/api/*.java"]),
)
filegroup(
  name = "generator-dao-raw-sources",
  srcs = glob(["src/main/java/**/generator/dao/*.java"]),
)
filegroup(
  name = "generator-dao-generic-raw-sources",
  srcs = glob(["src/main/java/**/generator/dao/generic/*.java"]),
)
filegroup(
  name = "generator-dao-jdbc-raw-sources",
  srcs = glob(["src/main/java/**/generator/dao/jdbc/*.java"]),
)
filegroup(
  name = "generator-logging-raw-sources",
  srcs = glob(["src/main/java/**/generator/logging/*.java"]),
)
filegroup(
  name = "generator-logging-jdk-raw-sources",
  srcs = glob(["src/main/java/**/generator/logging/jdk/*.java"]),
)
filegroup(
  name = "generator-logging-log4j-raw-sources",
  srcs = glob(["src/main/java/**/generator/logging/log4j/*.java"]),
)
filegroup(
  name = "generator-logging-noop-raw-sources",
  srcs = glob(["src/main/java/**/generator/logging/noop/*.java"]),
)
filegroup(
  name = "generator-logging-slf4j-raw-sources",
  srcs = glob(["src/main/java/**/generator/logging/slf4j/*.java"]),
)
filegroup(
  name = "generator-helpers-raw-sources",
  srcs = glob(["src/main/java/**/generator/helpers/*.java"]),
)
filegroup(
  name = "generator-utils-raw-sources",
  srcs = glob(["src/main/java/**/generator/utilities/*.java"]),
)
filegroup(
  name = "parser-raw-sources",
  srcs = glob(["src/main/java/**/parser/*.java"]),
)
filegroup(
  name = "main-raw-sources",
  srcs = glob(["src/main/java/**/yosql/YoSql*.java"]),
)
filegroup(
  name = "dagger-raw-sources",
  srcs = glob(["src/main/java/**/dagger/*.java"]),
)

java_plugin(
    name = "auto_plugin",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    deps = ["//external:auto_value"],
)
java_library(
    name = "auto",
    exported_plugins = [":auto_plugin"],
    exports = ["//external:auto_value"],
)

java_library(
  name = "model",
  srcs = [
    ":model-raw-sources",
  ],
  resources = [":config-options"],
  deps = [
    "//external:javax_annotation",
    "//external:javax_inject",
    "//external:javapoet",
    "//external:dagger",
    "//external:cal10n",
    ":auto",
  ],
  exports = [
    "//external:javax_annotation",
    "//external:javax_inject",
    "//external:javapoet",
    "//external:dagger",
    "//external:cal10n",
  ],
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

java_library(
  name = "parser",
  srcs = [
    ":parser-raw-sources",
  ],
  deps = [
    "//external:snakeyaml",
    ":model",
    ":dagger2",
  ],
  exports = [
    "//external:snakeyaml",
  ],
)

java_library(
  name = "generator-api",
  srcs = [
    ":generator-api-raw-sources",
  ],
  deps = [
    ":model",
  ],
  exports = [
    ":model",
  ],
)
java_library(
  name = "generator-dao-generic",
  srcs = [
    ":generator-dao-generic-raw-sources",
  ],
  deps = [
    ":generator-api",
    ":generator-helpers",
  ],
)
java_library(
  name = "generator-dao-jdbc",
  srcs = [
    ":generator-dao-jdbc-raw-sources",
  ],
  deps = [
    ":dagger",
    ":generator-api",
    ":generator-dao-generic",
    ":generator-helpers",
  ],
)
java_library(
  name = "generator-dao",
  srcs = [
    ":generator-dao-raw-sources",
  ],
  deps = [
    ":dagger",
    ":generator-api",
    ":generator-helpers",
    ":generator-dao-generic",
    ":generator-dao-jdbc",
  ],
  exports = [
    ":generator-dao-generic",
    ":generator-dao-jdbc",
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
    ":dagger",
    ":generator-api",
  ],
  exports = [
    "//external:javapoet_type_guesser",
    "//external:reactive_streams",
    "//external:rxjava2",
  ],
)
java_library(
  name = "generator-logging-jdk",
  srcs = [
    ":generator-logging-jdk-raw-sources",
  ],
  deps = [
    ":generator-api",
    ":generator-helpers",
  ],
)
java_library(
  name = "generator-logging-log4j",
  srcs = [
    ":generator-logging-log4j-raw-sources",
  ],
  deps = [
    "//external:log4j_api",
    "//external:log4j_core",
    ":generator-api",
    ":generator-helpers",
  ],
  exports = [
    "//external:log4j_api",
    "//external:log4j_core",
  ],
)
java_library(
  name = "generator-logging-noop",
  srcs = [
    ":generator-logging-noop-raw-sources",
  ],
  deps = [
    ":generator-api",
  ],
)
java_library(
  name = "generator-logging-slf4j",
  srcs = [
    ":generator-logging-slf4j-raw-sources",
  ],
  deps = [
    "//external:slf4j_api",
    ":generator-api",
    ":generator-helpers",
  ],
  exports = [
    "//external:slf4j_api",
  ],
)
java_library(
  name = "generator-logging",
  srcs = [
    ":generator-logging-raw-sources",
  ],
  deps = [
    ":dagger",
    ":generator-api",
    ":generator-helpers",
    ":generator-logging-jdk",
    ":generator-logging-log4j",
    ":generator-logging-noop",
    ":generator-logging-slf4j",
  ],
)

java_library(
  name = "generator-utils",
  srcs = [
    ":generator-utils-raw-sources",
  ],
  deps = [
    ":dagger2",
    ":generator-api",
    ":generator-helpers",
  ],
)

java_library(
  name = "main",
  srcs = [
    ":main-raw-sources",
  ],
  deps = [
    ":model",
    ":utils",
    ":parser",
    ":dagger",
    ":generator-api",
    ":generator-helpers",
    ":generator-logging",
    ":generator-dao",
    ":generator-utils",
  ],
  exports = [
    ":model",
    ":utils",
    ":parser",
    ":dagger",
    ":generator-api",
    ":generator-helpers",
    ":generator-logging",
    ":generator-dao",
    ":generator-utils",
  ],
)

java_plugin(
  name = "dagger_plugin",
  generates_api = True,
  processor_class = "dagger.internal.codegen.ComponentProcessor",
  deps = [
    "//external:javax_inject",
    "//external:dagger",
    "//external:dagger-compiler",
    "@dagger_producers//jar",
    "@dagger_javapoet//jar",
    "@google_java_format//jar",
    "@guava//jar",
    "@errorprone_javac//jar",
  ],
)

java_library(
  name = "dagger2",
  visibility = ["//visibility:public"],
  exported_plugins = [":dagger_plugin"],
  exports = [
    "//external:dagger"
  ],
)

java_library(
  name = "dagger",
  srcs = [
    ":dagger-raw-sources",
  ],
  deps = [
    "//external:javax_inject",
    "//external:dagger",
    "//external:dagger-compiler",
    ":dagger2",
    ":model",
  ],
  exports = [
    ":dagger2",
  ],
)

java_library(
  name = "yosql-core",
  visibility = ["//visibility:public"],
  exports = [
    ":main",
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
