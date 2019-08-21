# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "methods_generator",
    srcs = ["GenericMethodsGenerator.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao/jdbc:__pkg__",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:abstract_methods_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:annotation_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_code_blocks",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_fields",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_methods",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_names",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_parameters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "@maven//:com_squareup_javapoet",
    ],
)

java_library(
    name = "repository_generator",
    srcs = ["GenericRepositoryGenerator.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao/jdbc:__pkg__",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:abstract_methods_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:annotation_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_code_blocks",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_fields",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_javadoc",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_methods",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_names",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_parameters",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_types",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:config",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:generator",
        "@maven//:com_squareup_javapoet",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)
