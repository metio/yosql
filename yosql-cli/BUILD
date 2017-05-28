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
    "@org_slf4j_slf4j_ext//jar",
    "//yosql-core",
  ],
  resources = [
    ":cli-events",
    ":commands",
  ],
)
