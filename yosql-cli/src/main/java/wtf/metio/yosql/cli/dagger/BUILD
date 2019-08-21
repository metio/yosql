# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "abstract_parser_module",
    srcs = ["AbstractOptionParserModule.java"],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:descriptions",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:options",
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "used_for",
    srcs = ["UsedFor.java"],
    deps = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/i18n",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:options",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "help_module",
    srcs = ["HelpOptionParserModule.java"],
    deps = [
        ":abstract_parser_module",
        ":used_for",
        "//third_party/dagger",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/i18n",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/parser",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:descriptions",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:options",
        "@maven//:javax_inject_javax_inject",
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "option_module",
    srcs = ["OptionParserModule.java"],
    deps = [
        "//third_party/dagger",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/parser:help_formatter",
        "@maven//:javax_inject_javax_inject",
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "version_module",
    srcs = ["VersionOptionParserModule.java"],
    deps = [
        ":abstract_parser_module",
        ":used_for",
        "//third_party/dagger",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/i18n",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/parser",
        "@maven//:javax_inject_javax_inject",
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "config_module",
    srcs = ["JOptConfigurationModule.java"],
    deps = [
        ":used_for",
        "//third_party/dagger",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/i18n",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/parser:path_converter",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/parser:result_row_converter",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:logging",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:options",
        "@maven//:javax_inject_javax_inject",
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "locale_module",
    srcs = ["JOptLocaleModule.java"],
    deps = [
        ":used_for",
        "//third_party/dagger",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/i18n",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/parser:locale_converter",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:annotations",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:descriptions",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:options",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:javax_inject_javax_inject",
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "generate_module",
    srcs = ["GenerateOptionParserModule.java"],
    deps = [
        ":abstract_parser_module",
        ":used_for",
        "//third_party/dagger",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/i18n",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/parser",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:descriptions",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:logging",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:options",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:javax_inject_javax_inject",
        "@maven//:net_sf_jopt_simple_jopt_simple",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_library(
    name = "cli_component",
    srcs = ["YoSqlCLIComponent.java"],
    visibility = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__pkg__",
    ],
    deps = [
        ":config_module",
        ":generate_module",
        ":help_module",
        ":locale_module",
        ":option_module",
        ":used_for",
        ":version_module",
        "//third_party/dagger",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/i18n",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/parser",
        "//yosql-core/src/main/java/wtf/metio/yosql",
        "//yosql-core/src/main/java/wtf/metio/yosql:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:error_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:i18n_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:logger_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:annotation_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:writer",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao/jdbc:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_code_blocks",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_fields",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/jdk:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/log4j:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/noop:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/slf4j:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:default_utilities_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:logging",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:options",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:default_parser_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:default_resolver_module",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:factory",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:io",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:preconditions",
        "//yosql-core/src/main/java/wtf/metio/yosql/utils:timer",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:javax_inject_javax_inject",
        "@maven//:net_sf_jopt_simple_jopt_simple",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)
