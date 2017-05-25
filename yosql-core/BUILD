filegroup(
  name = "model-raw-sources",
  srcs = glob(["src/main/java/**/model/*.java"]),
)
filegroup(
  name = "application-events",
  srcs = ["src/main/resources/application-events_en.properties"],
)
filegroup(
  name = "loggers",
  srcs = ["src/main/resources/loggers_en.properties"],
)
filegroup(
  name = "help-options",
  srcs = ["src/main/resources/help-options_en.properties"],
)
filegroup(
  name = "help-option-descriptions",
  srcs = ["src/main/resources/help-option-descriptions_en.properties"],
)
filegroup(
  name = "general-options",
  srcs = ["src/main/resources/general-options_en.properties"],
)
filegroup(
  name = "general-option-descriptions",
  srcs = ["src/main/resources/general-option-descriptions_en.properties"],
)
filegroup(
  name = "generate-options",
  srcs = ["src/main/resources/generate-options_en.properties"],
)
filegroup(
  name = "generate-option-descriptions",
  srcs = ["src/main/resources/generate-option-descriptions_en.properties"],
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
filegroup(
  name = "validation-files",
  srcs = glob(["src/test/resources/*.txt"]),
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
  resources = [
    ":application-events",
    ":loggers",
    ":help-options",
    ":help-option-descriptions",
    ":general-options",
    ":general-option-descriptions",
    ":generate-options",
    ":generate-option-descriptions",
  ],
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
    "//external:slf4j_api",
    "//external:slf4j_ext",
    "//external:logback_classic",
    "//external:logback_core",
    ":model",
    ":dagger",
  ],
)

java_library(
  name = "parser",
  srcs = [
    ":parser-raw-sources",
  ],
  deps = [
    "//external:snakeyaml",
    "//external:slf4j_api",
    "//external:slf4j_ext",
    "//external:logback_classic",
    "//external:logback_core",
    ":model",
    ":dagger",
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
    "//external:slf4j_api",
    "//external:slf4j_ext",
    "//external:logback_classic",
    "//external:logback_core",
    ":model",
    ":dagger",
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
    "//external:slf4j_api",
    "//external:slf4j_ext",
    ":dagger",
  ],
)
java_library(
  name = "generator-dao-jdbc",
  srcs = [
    ":generator-dao-jdbc-raw-sources",
  ],
  deps = [
    "//external:slf4j_ext",
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
    "//external:slf4j_api",
    "//external:logback_classic",
    "//external:logback_core",
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
    ":dagger",
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
    ":dagger",
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
    ":dagger",
  ],
)
java_library(
  name = "generator-logging-slf4j",
  srcs = [
    ":generator-logging-slf4j-raw-sources",
  ],
  deps = [
    "//external:slf4j_api",
    "//external:logback_classic",
    "//external:logback_core",
    ":dagger",
    ":generator-api",
    ":generator-helpers",
  ],
  exports = [
    "//external:slf4j_api",
    "//external:logback_classic",
    "//external:logback_core",
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
  exports = [
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
    "//external:slf4j_api",
    "//external:slf4j_ext",
    "//external:logback_classic",
    "//external:logback_core",
    ":dagger",
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
    "//external:slf4j_api",
    "//external:slf4j_ext",
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
    "//external:slf4j_api",
    "//external:logback_classic",
  ],
)

load("@bazel_build_process//java:junit5_test.bzl", "junit5")

[junit5(
  file = file,
  deps = [
    "//yosql-testutils",
    ":model",
  ],
) for file in glob(["src/test/java/**/model/*Test.java"])]

[junit5(
  file = file,
  deps = [
    "//yosql-testutils",
    ":model",
    ":generator-helpers",
  ],
  data = [
    ":validation-files",
  ],
) for file in glob(["src/test/java/**/generator/helpers/*Test.java"])]
