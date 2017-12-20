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
  name = "parser-preconditions",
  srcs = ["src/main/resources/parser-preconditions_en.properties"],
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
  visibility = [
    "//yosql-cli:__subpackages__",
    "//yosql-micro-benchmarks:__subpackages__",
    "//yosql-testutils:__subpackages__",
  ],
  resources = [
    ":application-events",
    ":parser-preconditions",
    ":loggers",
    ":help-options",
    ":help-option-descriptions",
    ":general-options",
    ":general-option-descriptions",
    ":generate-options",
    ":generate-option-descriptions",
  ],
  deps = [
    "@javax_annotation_javax_annotation_api//jar",
    "@javax_inject_javax_inject//jar",
    "@javapoet//jar",
    "@com_google_dagger_dagger//jar",
    "@ch_qos_cal10n_cal10n_api//jar",
    ":auto",
  ],
)

java_library(
  name = "utils",
  srcs = [
    ":utils-raw-sources",
  ],
  visibility = [
    "//yosql-micro-benchmarks:__subpackages__",
    "//yosql-cli:__subpackages__",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@slf4j_api//jar",
    "@slf4j_ext//jar",
    "@logback_classic//jar",
    "@logback_core//jar",
    ":model",
    ":dagger",
  ],
)

java_library(
  name = "parser",
  srcs = [
    ":parser-raw-sources",
  ],
  visibility = [
    "//yosql-micro-benchmarks:__subpackages__",
    "//yosql-cli:__subpackages__",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@snakeyaml//jar",
    "@slf4j_api//jar",
    "@slf4j_ext//jar",
    "@ch_qos_cal10n_cal10n_api//jar",
    "@logback_classic//jar",
    "@logback_core//jar",
    ":model",
    ":dagger",
    ":dagger2",
  ],
)

java_library(
  name = "generator-api",
  srcs = [
    ":generator-api-raw-sources",
  ],
  visibility = ["//yosql-micro-benchmarks:__subpackages__"],
  deps = [
    "@slf4j_api//jar",
    "@slf4j_ext//jar",
    "@logback_classic//jar",
    "@logback_core//jar",
    "@javapoet//jar",
    "@javax_inject_javax_inject//jar",
    ":model",
    ":dagger",
  ],
)
java_library(
  name = "generator-dao-generic",
  srcs = [
    ":generator-dao-generic-raw-sources",
  ],
  deps = [
    "@slf4j_api//jar",
    "@slf4j_ext//jar",
    "@javapoet//jar",
    ":model",
    ":generator-api",
    ":generator-helpers",
    ":dagger",
  ],
)
java_library(
  name = "generator-dao-jdbc",
  srcs = [
    ":generator-dao-jdbc-raw-sources",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@javapoet//jar",
    "@javapoet_type_guesser//jar",
    "@io_reactivex_rxjava2_rxjava2//jar",
    "@slf4j_ext//jar",
    ":dagger",
    ":model",
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
  visibility = [
    "//yosql-micro-benchmarks:__subpackages__",
    "//yosql-cli:__subpackages__",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@slf4j_api//jar",
    "@logback_classic//jar",
    "@logback_core//jar",
    ":model",
    ":dagger",
    ":generator-api",
    ":generator-helpers",
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
    "@javax_inject_javax_inject//jar",
    "@javapoet_type_guesser//jar",
    "@javapoet//jar",
    "@io_reactivex_rxjava2_rxjava2//jar",
    ":model",
    ":parser",
    ":dagger",
    ":generator-api",
  ],
)
java_library(
  name = "generator-logging-jdk",
  srcs = [
    ":generator-logging-jdk-raw-sources",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@javapoet//jar",
    ":generator-api",
    ":generator-helpers",
    ":dagger",
  ],
)
java_library(
  name = "generator-logging-log4j",
  srcs = [
    ":generator-logging-log4j-raw-sources",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@javapoet//jar",
    "@log4j_api//jar",
    "@log4j_core//jar",
    ":generator-api",
    ":generator-helpers",
    ":dagger",
  ],
)
java_library(
  name = "generator-logging-noop",
  srcs = [
    ":generator-logging-noop-raw-sources",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@javapoet//jar",
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
    "@slf4j_api//jar",
    "@javax_inject_javax_inject//jar",
    "@javapoet//jar",
    "@logback_classic//jar",
    "@logback_core//jar",
    ":generator-api",
    ":generator-helpers",
    ":dagger",
  ],
)
java_library(
  name = "generator-logging",
  srcs = [
    ":generator-logging-raw-sources",
  ],
  visibility = [
    "//yosql-micro-benchmarks:__subpackages__",
    "//yosql-cli:__subpackages__",
  ],
  deps = [
    "@javapoet//jar",
    ":model",
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
  visibility = [
    "//yosql-micro-benchmarks:__subpackages__",
    "//yosql-cli:__subpackages__",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@javapoet//jar",
    "@slf4j_api//jar",
    "@slf4j_ext//jar",
    "@logback_classic//jar",
    "@logback_core//jar",
    ":model",
    ":generator-api",
    ":generator-helpers",
    ":dagger",
  ],
)

java_library(
  name = "main",
  srcs = [
    ":main-raw-sources",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
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
    "@javax_inject_javax_inject//jar",
    "@com_google_dagger_dagger//jar",
    "@com_google_dagger_dagger_compiler//jar",
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
    "@com_google_dagger_dagger//jar"
  ],
)

java_library(
  name = "dagger",
  srcs = [
    ":dagger-raw-sources",
  ],
  visibility = [
    "//yosql-cli:__subpackages__",
    "//yosql-micro-benchmarks:__subpackages__",
  ],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@com_google_dagger_dagger//jar",
    "@com_google_dagger_dagger_compiler//jar",
    "@slf4j_api//jar",
    "@slf4j_ext//jar",
    "@ch_qos_cal10n_cal10n_api//jar",
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
    "//yosql-testutils",
    ":model",
  ],
) for file in glob(["src/test/java/**/model/*Test.java"])]

[junit5(
  file = file,
  deps = [
    "@javapoet//jar",
    "//yosql-testutils",
    ":model",
    ":generator-helpers",
  ],
  data = [
    ":validation-files",
  ],
) for file in glob(["src/test/java/**/generator/helpers/*Test.java"])]

[junit5(
  file = file,
  deps = [
    "@javapoet//jar",
    "//yosql-testutils",
    ":model",
    ":generator-api",
    ":generator-utils",
  ],
  data = [
    ":validation-files",
  ],
) for file in glob(["src/test/java/**/generator/utilities/*Test.java"])]
