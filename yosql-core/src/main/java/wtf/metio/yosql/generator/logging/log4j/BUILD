java_library(
    name = "annotation",
    srcs = ["Log4j.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:__pkg__",
    ],
    deps = [
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "generator",
    srcs = ["Log4jLoggingGenerator.java"],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_fields",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_names",
        "@maven//:com_squareup_javapoet",
        "@maven//:org_apache_logging_log4j_log4j_api",
    ],
)

java_library(
    name = "module",
    srcs = ["Log4jLoggingModule.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:__pkg__",
    ],
    deps = [
        ":annotation",
        ":generator",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_fields",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_inject_javax_inject",
    ],
)
