# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

genrule(
    name = "build_info",
    outs = ["BuildInfo.java"],
    cmd = """
    cat > $@ <<EOF
package wtf.metio.yosql.cli;
public class BuildInfo {
  public static final String VERSION = "$(RELEASE_VERSION)";
  public static final String BUILD = "$(BUILD_NUMBER)";
  public static final String REVISION = "$(GIT_REVISION)";
  public static final String BUILD_BY = "$(BUILD_BY)";
  public static final String BUILD_AT = "$(BUILD_AT)";
}
EOF
""",
)

java_library(
    name = "build",
    srcs = [":build_info"],
)

java_binary(
    name = "yosql-cli",
    srcs = ["YoSqlCLI.java"],
    main_class = "wtf.metio.yosql.cli.YoSqlCLI",
    resources = [
        "//yosql-cli/src/main/resources:localizations",
        "//yosql-core/src/main/resources:localizations",
    ],
    visibility = [
        "//yosql-example:__pkg__",
    ],
    deps = [
        ":build",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/dagger:cli_component",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/i18n",
        "//yosql-cli/src/main/java/wtf/metio/yosql/cli/parser",
        "//yosql-core/src/main/java/wtf/metio/yosql",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:descriptions",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:internal",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:options",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:net_sf_jopt_simple_jopt_simple",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)
