# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "configuration_module",
    srcs = [
        "DefaultConfigurationModule.java",
    ],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:logging",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:options",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "locale_module",
    srcs = [
        "DefaultLocaleModule.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        "//third_party/dagger",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "error_module",
    srcs = [
        "ErrorModule.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
    ],
)

java_library(
    name = "logger_module",
    srcs = [
        "LoggerModule.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/files:factory",
        "//yosql-core/src/main/java/wtf/metio/yosql/files:parser",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:internal",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)

java_library(
    name = "module",
    srcs = [
        "DaggerModule.java",
    ],
    deps = [
        ":configuration_module",
        ":error_module",
        ":locale_module",
        ":logger_module",
        "//third_party/dagger",
    ],
)
