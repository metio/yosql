# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

java_library(
    name = "generate_mojo",
    srcs = ["YoSqlGenerateMojo.java"],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql",
        "//yosql-core/src/main/java/wtf/metio/yosql/generator/utilities:generators",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:converters",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:errors",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:logging",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:com_squareup_javapoet",
        "@maven//:org_apache_maven_maven_model",
        "@maven//:org_apache_maven_maven_plugin_api",
        "@maven//:org_apache_maven_plugin_tools_maven_plugin_annotations",
    ],
)
