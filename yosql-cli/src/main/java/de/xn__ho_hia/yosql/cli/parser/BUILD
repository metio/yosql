filegroup(
  name = "yosql-cli-parser-raw-sources",
  srcs = glob(["*.java"]),
)

java_library(
  name = "yosql-cli-parser",
  srcs = [":yosql-cli-parser-raw-sources"],
  visibility = ["//yosql-cli:__subpackages__"],
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
