filegroup(
  name = "yosql-cli-raw-sources",
  srcs = glob(["src/main/java/**/*.java"]),
)
filegroup(
  name = "cli-events",
  srcs = ["src/main/resources/cli-events_en.properties"],
)
filegroup(
  name = "commands",
  srcs = ["src/main/resources/commands_en.properties"],
)

genrule(
  name = "gen_my_binary_version",
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
    ":yosql-cli-raw-sources",
    ":de/xn__ho_hia/yosql/BuildInfo.java",
  ],
  visibility = ["//visibility:public"],
  deps = [
    "//external:jopt_simple",
    "//external:asciitable",
    "//external:ascii_utf_themes",
    "//external:skb_interfaces",
    "//external:commons_lang3",
    "//external:st4",
    "//external:slf4j_ext",
    "//yosql-core",
  ],
  resources = [
    ":cli-events",
    ":commands",
  ],
)
