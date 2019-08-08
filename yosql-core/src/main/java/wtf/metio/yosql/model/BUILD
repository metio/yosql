# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "logging",
    srcs = [
        "LoggingAPI.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
        "//yosql-maven-plugin/src/main/java/wtf/metio/yosql:__subpackages__",
        "//yosql-testutils/src/main/java/wtf/metio/yosql/testutils:__subpackages__",
    ],
    deps = [
    ],
)

java_library(
    name = "options",
    srcs = [
        "GeneralOptions.java",
        "GenerateOptions.java",
        "HelpOptions.java",
        "VersionOptions.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
    ],
    deps = [
        "@maven//:ch_qos_cal10n_cal10n_api",
    ],
)

java_library(
    name = "descriptions",
    srcs = [
        "GeneralOptionDescriptions.java",
        "GenerateOptionDescriptions.java",
        "HelpOptionDescriptions.java",
    ],
    visibility = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__subpackages__",
    ],
    deps = [
        "@maven//:ch_qos_cal10n_cal10n_api",
    ],
)

java_library(
    name = "errors",
    srcs = [
        "CodeGenerationException.java",
        "ExecutionErrors.java",
        "SqlFileParsingException.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:__pkg__",
        "//yosql-maven-plugin/src/main/java/wtf/metio/yosql:__subpackages__",
    ],
    deps = [
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "events",
    srcs = [
        "ApplicationEvents.java",
    ],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/utils:__pkg__",
    ],
    deps = [
        "@maven//:ch_qos_cal10n_cal10n_api",
    ],
)

java_library(
    name = "converters",
    srcs = [
        "ParameterConverter.java",
        "ResultRowConverter.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:__pkg__",
        "//yosql-maven-plugin/src/main/java/wtf/metio/yosql:__subpackages__",
        "//yosql-testutils/src/main/java/wtf/metio/yosql/testutils:__subpackages__",
    ],
    deps = [
    ],
)

java_library(
    name = "execution",
    srcs = [
        "ExecutionConfiguration.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/utils:__pkg__",
        "//yosql-maven-plugin/src/main/java/wtf/metio/yosql:__subpackages__",
        "//yosql-testutils/src/main/java/wtf/metio/yosql/testutils:__subpackages__",
    ],
    deps = [
        ":converters",
        ":logging",
        ":options",
        "//third_party/auto:auto_value",
        "@maven//:com_google_code_findbugs_jsr305",
        "@maven//:com_squareup_javapoet",
    ],
)

java_library(
    name = "generator",
    srcs = [
        "PackageTypeSpec.java",
    ],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
    ],
    deps = [
        "@maven//:com_squareup_javapoet",
    ],
)

java_library(
    name = "internal",
    srcs = [
        "Loggers.java",
    ],
    visibility = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
    ],
    deps = [
        "@maven//:ch_qos_cal10n_cal10n_api",
    ],
)

java_library(
    name = "config",
    srcs = [
        "ReturningMode.java",
        "SqlConfiguration.java",
        "SqlParameter.java",
        "SqlStatement.java",
        "SqlType.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:__pkg__",
        "//yosql-testutils/src/main/java/wtf/metio/yosql/testutils:__subpackages__",
    ],
    deps = [
        ":converters",
    ],
)

java_library(
    name = "i18n",
    srcs = [
        "Translator.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/utils:__pkg__",
    ],
    deps = [
        ":descriptions",
        "@maven//:ch_qos_cal10n_cal10n_api",
    ],
)
