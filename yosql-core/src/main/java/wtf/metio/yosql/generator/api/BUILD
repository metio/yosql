java_library(
    name = "generators",
    srcs = [
        "BatchMethodGenerator.java",
        "FieldsGenerator.java",
        "Java8StreamMethodGenerator.java",
        "LoggingGenerator.java",
        "MethodsGenerator.java",
        "RepositoryGenerator.java",
        "RxJavaMethodGenerator.java",
        "StandardMethodGenerator.java",
        "UtilitiesGenerator.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
    ],
    deps = [
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:generator",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "writer",
    srcs = [
        "TypeWriter.java",
    ],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)

java_library(
    name = "annotation_generator",
    srcs = [
        "AnnotationGenerator.java",
    ],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_annotation_javax_annotation_api",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "abstract_methods_generator",
    srcs = [
        "AbstractMethodsGenerator.java",
    ],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__subpackages__",
    ],
    deps = [
        ":generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "@maven//:com_squareup_javapoet",
    ],
)
