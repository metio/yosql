# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "loggers",
    srcs = [
        "Generator.java",
        "Parser.java",
        "Reader.java",
        "Timer.java",
        "Utilities.java",
        "Writer.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/dagger:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/files:__pkg__",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql/orchestration:__pkg__",
    ],
    deps = [
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "delegating",
    srcs = [
        "Delegating.java",
    ],
    deps = [
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "localized",
    srcs = [
        "Localized.java",
    ],
    deps = [
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "nonLocalized",
    srcs = [
        "NonLocalized.java",
    ],
    deps = [
        "@maven//:javax_inject_javax_inject",
    ],
)
