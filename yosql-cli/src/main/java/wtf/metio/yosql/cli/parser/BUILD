# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "locale_converter",
    srcs = ["LocaleValueConverter.java"],
    visibility = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
    ],
    deps = [
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "path_converter",
    srcs = ["PathValueConverter.java"],
    visibility = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "result_row_converter",
    srcs = ["ResultRowConverterValueConverter.java"],
    visibility = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "help_formatter",
    srcs = ["YoSqlHelpFormatter.java"],
    visibility = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
    ],
    deps = [
        "@maven//:de_vandermeer_ascii_utf_themes",
        "@maven//:de_vandermeer_asciitable",
        "@maven//:de_vandermeer_skb_interfaces",
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)

java_library(
    name = "parser",
    srcs = ["YoSqlOptionParser.java"],
    visibility = [
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli:__pkg__",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:__pkg__",
    ],
    deps = [
        "@maven//:net_sf_jopt_simple_jopt_simple",
    ],
)
