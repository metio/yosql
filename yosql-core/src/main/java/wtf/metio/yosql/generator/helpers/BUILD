# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "typical_modifiers",
    srcs = ["TypicalModifiers.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:__pkg__",
    ],
)

java_library(
    name = "typical_names",
    srcs = ["TypicalNames.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:__pkg__",
    ],
)

java_library(
    name = "typical_types",
    srcs = ["TypicalTypes.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:__pkg__",
    ],
    deps = [
        ":typical_modifiers",
        "@maven//:com_squareup_javapoet",
    ],
)

java_library(
    name = "typical_fields",
    srcs = ["TypicalFields.java"],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__pkg__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:__pkg__",
    ],
    deps = [
        ":typical_modifiers",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:annotation_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "typical_methods",
    srcs = ["TypicalMethods.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:__pkg__",
    ],
    deps = [
        ":typical_modifiers",
        "@maven//:com_squareup_javapoet",
    ],
)

java_library(
    name = "typical_javadoc",
    srcs = ["TypicalJavadoc.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__subpackages__",
    ],
    deps = [
        ":typical_modifiers",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "@maven//:com_squareup_javapoet",
        "@maven//:de_xn__ho_hia_javapoet_javapoet_type_guesser",
    ],
)

java_library(
    name = "typical_parameters",
    srcs = ["TypicalParameters.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:__pkg__",
    ],
    deps = [
        ":typical_modifiers",
        ":typical_names",
        ":typical_types",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser",
        "@maven//:com_squareup_javapoet",
        "@maven//:de_xn__ho_hia_javapoet_javapoet_type_guesser",
    ],
)

java_library(
    name = "typical_code_blocks",
    srcs = ["TypicalCodeBlocks.java"],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__pkg__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:__pkg__",
    ],
    deps = [
        ":typical_fields",
        ":typical_modifiers",
        ":typical_names",
        ":typical_types",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:annotations",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/parser",
        "@maven//:com_squareup_javapoet",
        "@maven//:de_xn__ho_hia_javapoet_javapoet_type_guesser",
        "@maven//:io_reactivex_rxjava2_rxjava",
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "module",
    srcs = ["HelperModule.java"],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__pkg__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        ":typical_code_blocks",
        ":typical_fields",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:annotations",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:annotation_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "@maven//:javax_inject_javax_inject",
    ],
)
