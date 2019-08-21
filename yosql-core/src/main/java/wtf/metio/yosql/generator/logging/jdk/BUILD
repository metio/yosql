# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "annotation",
    srcs = ["JDK.java"],
    visibility = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:__pkg__",
    ],
    deps = [
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "generator",
    srcs = ["JdkLoggingGenerator.java"],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_fields",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_names",
        "@maven//:com_squareup_javapoet",
    ],
)

java_library(
    name = "module",
    srcs = ["JdkLoggingModule.java"],
    visibility = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/logging:__pkg__",
    ],
    deps = [
        ":annotation",
        ":generator",
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/api:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/helpers:typical_fields",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_inject_javax_inject",
    ],
)
