load("@bazel_build_process//java:junit5_test.bzl", "junit5_test")

java_library(
  name = "yosql-core",
  srcs = glob(["src/main/java/**/*.java"]),
  visibility = ["//visibility:public"],
  deps = [
    "//external:javapoet_type_guesser",
    "//external:javax_inject",
    "//external:javapoet",
    "//external:snakeyaml",
    "//external:rxjava2",
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api"
  ],
)

java_library(
  name = "yosql-core-tests",
  srcs = glob(["src/test/java/**/*.java"]),
  deps = [
    ":yosql-core",
    "//external:junit5_jupiter_api",
  ],
)

junit5_test(
  name = "de.xn__ho_hia.yosql.model.LoggingAPITest",
  runtime_deps = [
    ":yosql-core-tests",
  ],
)

junit5_test(
  name = "de.xn__ho_hia.yosql.model.SqlTypeTest",
  runtime_deps = [
    ":yosql-core-tests",
  ],
)

java_test(
  name = "alltests",
  main_class = "org.junit.platform.console.ConsoleLauncher",
  use_testrunner = False,
  size = "small",
  args = [
    "--scan-class-path",
  ],
  runtime_deps = [
    ":yosql-core-tests",
    "//external:junit5_jupiter_engine",
    "//external:junit5_platform_console",
    "//external:junit5_platform_commons",
    "//external:junit5_platform_engine",
    "//external:junit5_platform_launcher",
    "//external:opentest4j",
  ],
)

