# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

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
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
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
