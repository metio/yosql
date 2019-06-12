java_library(
    name = "yosql",
    srcs = ["YoSql.java"],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__pkg__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-maven-plugin/src/main/java/wtf/metio/yosql:__subpackages__",
    ],
    deps = [
    ],
)

java_library(
    name = "implementation",
    srcs = ["YoSqlImplementation.java"],
    deps = [
        ":yosql",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:writer",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:io",
        "//yosql-core/src/main/java/wtf/metio/yosql/utils:timer",
    ],
)

java_library(
    name = "module",
    srcs = ["YoSqlModule.java"],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
    ],
    deps = [
        ":implementation",
        ":yosql",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:annotations",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:writer",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:io",
        "//yosql-core/src/main/java/wtf/metio/yosql/utils:timer",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "component",
    srcs = ["YoSqlComponent.java"],
    deps = [
        ":module",
        ":yosql",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:default_configuration_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:default_locale_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:error_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:i18n_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:logger_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:default_utilities_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:default_parser_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:default_resolver_module",
        "@maven//:javax_inject_javax_inject",
    ],
)
