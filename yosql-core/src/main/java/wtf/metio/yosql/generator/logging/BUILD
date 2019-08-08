# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "delegate",
    srcs = ["DelegatingLoggingGenerator.java"],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "@maven//:com_squareup_javapoet",
    ],
)

java_library(
    name = "module",
    srcs = ["LoggingModule.java"],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        ":delegate",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:annotations",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/jdk:annotation",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/jdk:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/log4j:annotation",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/log4j:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/noop:annotation",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/noop:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/slf4j:annotation",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging/slf4j:module",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_inject_javax_inject",
    ],
)
