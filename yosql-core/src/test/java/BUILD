load("@bazel_build_process//java:junit5_test.bzl", "junit5_classpath_test")

filegroup(
  name = "yosql-core-model-test-raw-sources",
  srcs = glob(["**/*.java"]),
)

java_library(
  name = "yosql-core-model-test",
  srcs = [
    ":yosql-core-model-test-raw-sources",
  ],
  deps = [
    "//external:junit5_jupiter_api",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/model:yosql-core-model",
  ],
)

junit5_classpath_test(
  name = "de.xn__ho_hia.yosql.model.LoggingAPITest",
  runtime_deps = [
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/model:yosql-core-model",
    ":yosql-core-model-test",
  ]
)

junit5_classpath_test(
  name = "de.xn__ho_hia.yosql.model.SqlTypeTest",
  runtime_deps = [
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/model:yosql-core-model",
    ":yosql-core-model-test",
  ]
)
