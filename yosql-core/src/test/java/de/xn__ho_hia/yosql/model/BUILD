load("@bazel_build_process//java:junit5_test.bzl", "junit5_test_in_package")

[junit5_test_in_package(
  name = file[:-5].replace("/", "."),
  package = "de.xn__ho_hia.yosql.model",
  deps = [
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/model:yosql-core-model",
    "//yosql-testutils",
  ],
) for file in glob(["*.java"])]
