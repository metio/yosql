filegroup(
  name = "yosql-core-main-raw-sources",
  srcs = glob(["src/main/java/**/YoSql.java"]),
)

java_library(
  name = "yosql-core",
  srcs = [
    ":yosql-core-main-raw-sources",
  ],
  visibility = ["//visibility:public"],
  deps = [
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/model:yosql-core-model",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/i18n:yosql-core-i18n",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/utils:yosql-core-utils",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/parser:yosql-core-parser",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-api",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-helpers",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-logging",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-raw-jdbc",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-utils",
  ],
  exports = [
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/model:yosql-core-model",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/i18n:yosql-core-i18n",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/utils:yosql-core-utils",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/parser:yosql-core-parser",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-api",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-helpers",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-logging",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-raw-jdbc",
    "//yosql-core/src/main/java/de/xn__ho_hia/yosql/generator:yosql-core-generator-utils",
  ],
)
