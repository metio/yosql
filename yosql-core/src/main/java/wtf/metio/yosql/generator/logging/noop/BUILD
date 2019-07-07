java_library(
    name = "annotation",
    srcs = ["NoOp.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:__pkg__",
    ],
    deps = [
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "generator",
    srcs = ["NoOpLoggingGenerator.java"],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "@maven//:com_squareup_javapoet",
    ],
)

java_library(
    name = "module",
    srcs = ["NoOpLoggingModule.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:__pkg__",
    ],
    deps = [
        ":annotation",
        ":generator",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "@maven//:com_squareup_javapoet",
    ],
)
