# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "generators",
    srcs = [
        "FlowStateGenerator.java",
        "ResultRowGenerator.java",
        "ResultStateGenerator.java",
        "ToResultRowConverterGenerator.java",
    ],
    visibility = [
        "//yosql-maven-plugin/src/main/java/wtf/metio/yosql:__subpackages__",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:annotation_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_code_blocks",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_fields",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_methods",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_modifiers",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_names",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_parameters",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_types",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)

java_library(
    name = "default_utilities_generator",
    srcs = ["DefaultUtilitiesGenerator.java"],
    deps = [
        ":generators",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:generator",
    ],
)

java_library(
    name = "default_utilities_module",
    srcs = ["DefaultUtilitiesModule.java"],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        ":default_utilities_generator",
        ":generators",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "@maven//:javax_inject_javax_inject",
    ],
)
