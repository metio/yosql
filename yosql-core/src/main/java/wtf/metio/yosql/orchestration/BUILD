# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "named_thread_factory",
    srcs = [
        "NamedForkJoinWorkerThreadFactory.java",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
    ],
)

java_library(
    name = "thread_pool_factory",
    srcs = [
        "ThreadPoolFactory.java",
    ],
    deps = [
        ":named_thread_factory",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
    ],
)

java_library(
    name = "writer",
    srcs = [
        "DefaultTypeWriter.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        "//third_party/dagger",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:generator",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:com_squareup_javapoet",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)

java_library(
    name = "timer",
    srcs = [
        "DefaultTimer.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        ":named_thread_factory",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)
