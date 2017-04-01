load("@bazel_build_process//java:junit5_test.bzl", "junit5_test")

VERSION="123";

filegroup(
  name = "yosql-core-raw-sources",
  srcs = glob(["java/**/*.java"]),
)

filegroup(
  name = "yosql-core-test-raw-sources",
  srcs = glob(["javatest/**/*.java"]),
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
  name = "yosql-core",
  srcs = [":yosql-core-raw-sources",],
  resources = ["//yosql-core/resources:config-options"],
  visibility = ["//visibility:public"],
  deps = [
    "//external:javapoet_type_guesser",
    "//external:javax_inject",
    "//external:javax_annotation",
    "//external:javapoet",
    "//external:snakeyaml",
    "//external:rxjava2",
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
    "//external:cal10n",
    ":auto",
  ],
)

genrule(
  name = "yosql-core-sources",
  srcs = [":yosql-core-raw-sources"],
  visibility = ["//visibility:public"],
  outs = ["yosql-core-sources.jar"],
  cmd = "jar cf $@ $(SRCS)",
)

genrule(
  name = "release-yosql-core-sources",
  srcs = [":yosql-core-sources"],
  visibility = ["//visibility:public"],
  outs = ["yosql-core-sources-%s.jar" % VERSION],
  cmd = "cp -T $< $@",
)

genrule(
  name = "release-yosql-core",
  srcs = [":yosql-core"],
  visibility = ["//visibility:public"],
  outs = ["yosql-core-%s.jar" % VERSION],
  cmd = "cp -T $< $@",
)

java_library(
  name = "yosql-core-tests",
  srcs = [":yosql-core-test-raw-sources"],
  deps = [
    ":yosql-core",
    "//external:junit5_jupiter_api",
  ],
)

junit5_test(
  name = "de/xn__ho_hia/yosql/model/LoggingAPITest",
  deps = [":yosql-core"]
)

junit5_test(
  name = "de/xn__ho_hia/yosql/model/SqlTypeTest",
  deps = [":yosql-core"]
)
