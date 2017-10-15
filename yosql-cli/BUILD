filegroup(
  name = "cli-events",
  srcs = ["src/main/resources/cli-events_en.properties"],
)
filegroup(
  name = "commands",
  srcs = ["src/main/resources/commands_en.properties"],
)
filegroup(
  name = "yosql-cli-dagger-raw-sources",
  srcs = glob(["src/main/java/de/xn__ho_hia/yosql/cli/dagger/*.java"]),
)
filegroup(
  name = "yosql-cli-i18n-raw-sources",
  srcs = glob(["src/main/java/de/xn__ho_hia/yosql/cli/i18n/*.java"]),
)
filegroup(
  name = "yosql-cli-parser-raw-sources",
  srcs = glob(["src/main/java/de/xn__ho_hia/yosql/cli/parser/*.java"]),
)

java_library(
  name = "yosql-cli-i18n",
  srcs = [":yosql-cli-i18n-raw-sources"],
  deps = [
    "@ch_qos_cal10n_cal10n_api//jar"
  ],
  resources = [
    ":cli-events",
    ":commands",
  ],
)

java_library(
  name = "yosql-cli-dagger",
  srcs = [":yosql-cli-dagger-raw-sources"],
  deps = [
    "@javax_inject_javax_inject//jar",
    "@net_sf_jopt_simple//jar",
    "@slf4j_api//jar",
    "@ch_qos_cal10n_cal10n_api//jar",
    "@logback_classic//jar",
    "//yosql-core",
    "//yosql-core:model",
    "//yosql-core:parser",
    "//yosql-core:dagger",
    "//yosql-core:generator-dao",
    "//yosql-core:generator-logging",
    "//yosql-core:generator-utils",
    ":yosql-cli-i18n",
    ":yosql-cli-parser",
  ],
)

java_library(
  name = "yosql-cli-parser",
  srcs = [":yosql-cli-parser-raw-sources"],
  deps = [
    "@net_sf_jopt_simple//jar",
    "@de_vandermeer_asciitable//jar",
    "@de_vandermeer_ascii_utf_themes//jar",
    "@de_vandermeer_skb_interfaces//jar",
    "@org_apache_commons_commons_lang3//jar",
    "@org_antlr_st4//jar",
    "//yosql-core:model",
  ],
)


genrule(
  name = "build_info",
  outs = ["de/xn__ho_hia/yosql/BuildInfo.java"],
  cmd = """
    cat > $@ <<EOF
package de.xn__ho_hia.yosql;
public class BuildInfo {
  public static final String VERSION = "$(RELEASE_VERSION)";
  public static final String BUILD = "$(BUILD_NUMBER)";
  public static final String REVISION = "$(GIT_REVISION)";
  public static final String BUILD_BY = "$(BUILD_BY)";
  public static final String BUILD_AT = "$(BUILD_AT)";
}
EOF
""")

java_binary(
  name = "yosql-cli",
  main_class = "de.xn__ho_hia.yosql.cli.YoSqlCLI",
  srcs = [
    "src/main/java/de/xn__ho_hia/yosql/cli/YoSqlCLI.java",
    ":build_info",
  ],
  visibility = ["//visibility:public"],
  deps = [
    "@net_sf_jopt_simple//jar",
    "@de_vandermeer_asciitable//jar",
    "@de_vandermeer_ascii_utf_themes//jar",
    "@de_vandermeer_skb_interfaces//jar",
    "@org_apache_commons_commons_lang3//jar",
    "@org_antlr_st4//jar",
    "@slf4j_api//jar",
    "@slf4j_ext//jar",
    "@ch_qos_cal10n_cal10n_api//jar",
      "@org_reactivestreams_reactive_streams//jar",
    "//yosql-core",
    "//yosql-core:model",
    ":yosql-cli-i18n",
    ":yosql-cli-dagger",
    ":yosql-cli-parser",
  ],
  resources = [
    ":cli-events",
    ":commands",
  ],
)
