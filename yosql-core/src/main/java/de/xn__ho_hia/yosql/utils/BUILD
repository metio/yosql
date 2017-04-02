filegroup(
  name = "yosql-core-utils-raw-sources",
  srcs = glob(["*.java"]),
)

java_library(
  name = "yosql-core-utils",
  srcs = [
    ":yosql-core-utils-raw-sources",
  ],
  visibility = ["//yosql-core:__pkg__"],
  deps = [
    "//external:javax_inject",
  ],
)
