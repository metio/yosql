# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "preconditions",
    srcs = [
        "ParserPreconditions.java",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "parser",
    srcs = [
        "SqlFileParser.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "@maven//:ch_qos_cal10n_cal10n_api",
    ],
)

java_library(
    name = "io",
    srcs = [
        "SqlFileResolver.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-maven-plugin/src/main/java/wtf/metio/yosql/parser:__pkg__",
    ],
    deps = [
        "@maven//:ch_qos_cal10n_cal10n_api",
    ],
)

java_library(
    name = "path_io",
    srcs = [
        "PathBasedSqlFileResolver.java",
    ],
    deps = [
        ":io",
        ":preconditions",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)

java_library(
    name = "factory",
    srcs = [
        "SqlConfigurationFactory.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "@maven//:org_slf4j_slf4j_ext",
        "@maven//:org_yaml_snakeyaml",
    ],
)

java_library(
    name = "default_parser_module",
    srcs = [
        "DefaultParserModule.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        ":default_sql_file_parser",
        ":parser",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:factory",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)

java_library(
    name = "default_sql_file_parser",
    srcs = [
        "DefaultSqlFileParser.java",
    ],
    deps = [
        ":parser",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:factory",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)

java_library(
    name = "default_resolver_module",
    srcs = [
        "DefaultResolverModule.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        ":io",
        ":path_io",
        ":preconditions",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)
