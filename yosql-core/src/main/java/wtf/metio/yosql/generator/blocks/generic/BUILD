java_library(
    name = "annotation_generator",
    srcs = [
        "DefaultAnnotationGenerator.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_annotation_javax_annotation_api",
        "@maven//:javax_inject_javax_inject",
    ],
)
