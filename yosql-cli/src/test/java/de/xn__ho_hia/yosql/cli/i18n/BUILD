load("@bazel_build_process//java:junit5_test.bzl", "junit5")

[junit5(
  file = file,
  deps = [
    "//yosql-testutils",
  ],
) for file in glob(["src/test/java/**/model/*Test.java"])]
