filegroup(
  name = "yosql-core-generator-api-raw-sources",
  srcs = glob(["*.java"]),
)
filegroup(
  name = "yosql-core-generator-logging-raw-sources",
  srcs = glob(["logging/*.java"]),
)
filegroup(
  name = "yosql-core-generator-helpers-raw-sources",
  srcs = glob(["helpers/*.java"]),
)
filegroup(
  name = "yosql-core-generator-raw-jdbc-raw-sources",
  srcs = glob(["raw_jdbc/*.java"]),
)
filegroup(
  name = "yosql-core-generator-utils-raw-sources",
  srcs = glob(["utils/*.java"]),
)

java_library(
  name = "yosql-core-generator-api",
  srcs = [
    ":yosql-core-generator-api-raw-sources",
  ],
  visibility = ["//yosql-core:__pkg__"],
  deps = [
    "//external:javax_inject",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/model:yosql-core-model",
  ],
  exports = [
    "//external:javax_inject",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/model:yosql-core-model",
  ],
)
java_library(
  name = "yosql-core-generator-helpers",
  srcs = [
    ":yosql-core-generator-helpers-raw-sources",
  ],
  visibility = ["//yosql-core:__pkg__"],
  deps = [
    "//external:javapoet_type_guesser",
    "//external:rxjava2",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/parser:yosql-core-parser",
    ":yosql-core-generator-api",
  ],
  exports = [
    "//external:javapoet_type_guesser",
    "//external:rxjava2",
    "//external:reactive_streams",
  ],
)
java_library(
  name = "yosql-core-generator-logging",
  srcs = [
    ":yosql-core-generator-logging-raw-sources",
  ],
  visibility = ["//yosql-core:__pkg__"],
  deps = [
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
    ":yosql-core-generator-api",
    ":yosql-core-generator-helpers",
  ],
  exports = [
    "//external:log4j_api",
    "//external:log4j_core",
    "//external:slf4j_api",
  ],
)
java_library(
  name = "yosql-core-generator-raw-jdbc",
  srcs = [
    ":yosql-core-generator-raw-jdbc-raw-sources",
  ],
  visibility = ["//yosql-core:__pkg__"],
  deps = [
    ":yosql-core-generator-api",
    ":yosql-core-generator-helpers",
  ],
)
java_library(
  name = "yosql-core-generator-utils",
  srcs = [
    ":yosql-core-generator-utils-raw-sources",
  ],
  visibility = ["//yosql-core:__pkg__"],
  deps = [
    ":yosql-core-generator-api",
    ":yosql-core-generator-helpers",
  ],
)
