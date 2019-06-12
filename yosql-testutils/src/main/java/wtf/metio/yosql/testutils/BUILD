java_library(
    name = "enum_tck",
    srcs = ["EnumTCK.java"],
    deps = [
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

java_library(
    name = "properties_tck",
    srcs = ["PropertiesTCK.java"],
    deps = [
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_library(
    name = "validation_files",
    srcs = [
        "ValidationFile.java",
        "ValidationFileParameterResolver.java",
        "ValidationFileTest.java",
    ],
    deps = [
        "@maven//:com_squareup_javapoet",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

java_library(
    name = "test_execution",
    srcs = ["TestExecutionConfigurations.java"],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:logging",
    ],
)

java_library(
    name = "test_config",
    srcs = ["TestSqlConfigurations.java"],
    deps = ["//yosql-core/src/main/java/wtf/metio/yosql/model:config"],
)
