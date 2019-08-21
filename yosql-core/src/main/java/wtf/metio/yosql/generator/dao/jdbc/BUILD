# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "annotation",
    srcs = ["JDBC.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__pkg__",
    ],
    deps = [
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "generators",
    srcs = [
        "JdbcBatchMethodGenerator.java",
        "JdbcFieldsGenerator.java",
        "JdbcJava8StreamMethodGenerator.java",
        "JdbcRxJavaMethodGenerator.java",
        "JdbcStandardMethodGenerator.java",
    ],
    deps = [
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
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "@maven//:com_squareup_javapoet",
        "@maven//:de_xn__ho_hia_javapoet_javapoet_type_guesser",
        "@maven//:io_reactivex_rxjava2_rxjava",
    ],
)

java_library(
    name = "module",
    srcs = ["DaoJdbcModule.java"],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao:__pkg__",
    ],
    deps = [
        ":annotation",
        ":generators",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:annotations",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:annotation_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao/generic:methods_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/dao/generic:repository_generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_code_blocks",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_fields",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)
