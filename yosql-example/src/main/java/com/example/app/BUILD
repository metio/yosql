filegroup(
    name = "example_java_files",
    srcs = glob([
        "**/*.java",
        "*.java",
    ]),
)

java_binary(
    name = "yosql-example",
    srcs = [
        ":example_java_files",
        "//yosql-example:example_generated",
    ],
    main_class = "com.example.app.ExampleApp",
    deps = [
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:ch_qos_logback_logback_core",
        "@maven//:com_h2database_h2",
        "@maven//:com_zaxxer_HikariCP",
        "@maven//:de_vandermeer_ascii_utf_themes",
        "@maven//:de_vandermeer_asciitable",
        "@maven//:de_vandermeer_skb_interfaces",
        "@maven//:io_reactivex_rxjava2_rxjava",
        "@maven//:mysql_mysql_connector_java",
        "@maven//:org_antlr_ST4",
        "@maven//:org_apache_commons_commons_lang3",
        "@maven//:org_postgresql_postgresql",
        "@maven//:org_reactivestreams_reactive_streams",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
