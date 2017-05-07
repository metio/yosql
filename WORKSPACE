local_repository(
    name = "bazel_build_process",
    path = "/home/sebhoss/git/sebhoss/bazel-build-process",
)

load("@bazel_tools//tools/build_defs/repo:maven_rules.bzl", "maven_jar")
load("@bazel_build_process//dependencies:sebhoss.bzl", "sebhoss_dependencies")
load("@bazel_build_process//dependencies:square.bzl", "square_dependencies")
load("@bazel_build_process//dependencies:logging.bzl", "logging_dependencies")

maven_server(
    name = "default",
    url = "https://repository.metio.wtf/repository/maven-public/",
)

sebhoss_dependencies()
square_dependencies()
logging_dependencies()

maven_jar(
    name = "javax_inject_javax_inject",
    artifact = "javax.inject:javax.inject:1",
)
bind(
    name = "javax_inject",
    actual = "@javax_inject_javax_inject//jar",
)


maven_jar(
    name = "javax_annotation_javax_annotation_api",
    artifact = "com.google.code.findbugs:jsr305:3.0.0",
)
bind(
    name = "javax_annotation",
    actual = "@javax_annotation_javax_annotation_api//jar",
)


maven_jar(
    name = "org_inferred_freebuilder",
    artifact = "org.inferred:freebuilder:1.14",
)
bind(
    name = "freebuilder",
    actual = "@org_inferred_freebuilder//jar",
)

maven_jar(
    name = "com_google_auto_value_auto_value",
    artifact = "com.google.auto.value:auto-value:1.2",
)
bind(
    name = "auto_value",
    actual = "@com_google_auto_value_auto_value//jar",
)

maven_jar(
    name = "org_yaml_snakeyaml",
    artifact = "org.yaml:snakeyaml:1.17",
)
bind(
    name = "snakeyaml",
    actual = "@org_yaml_snakeyaml//jar",
)

maven_jar(
    name = "junit_junit",
    artifact = "junit:junit:4.12",
)
bind(
    name = "junit",
    actual = "@junit_junit//jar",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_console",
    artifact = "org.junit.platform:junit-platform-console:1.0.0-M3",
)
bind(
    name = "junit5_platform_console",
    actual = "@org_junit_platform_junit5_platform_console//jar",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_commons",
    artifact = "org.junit.platform:junit-platform-commons:1.0.0-M3",
)
bind(
    name = "junit5_platform_commons",
    actual = "@org_junit_platform_junit5_platform_commons//jar",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_engine",
    artifact = "org.junit.platform:junit-platform-engine:1.0.0-M3",
)
bind(
    name = "junit5_platform_engine",
    actual = "@org_junit_platform_junit5_platform_engine//jar",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_launcher",
    artifact = "org.junit.platform:junit-platform-launcher:1.0.0-M3",
)
bind(
    name = "junit5_platform_launcher",
    actual = "@org_junit_platform_junit5_platform_launcher//jar",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_runner",
    artifact = "org.junit.platform:junit-platform-runner:1.0.0-M3",
)
bind(
    name = "junit5_platform_runner",
    actual = "@org_junit_platform_junit5_platform_runner//jar",
)

maven_jar(
    name = "org_junit_jupiter_junit5_jupiter_api",
    artifact = "org.junit.jupiter:junit-jupiter-api:5.0.0-M3",
)
bind(
    name = "junit5_jupiter_api",
    actual = "@org_junit_jupiter_junit5_jupiter_api//jar",
)

maven_jar(
    name = "org_junit_jupiter_junit5_jupiter_engine",
    artifact = "org.junit.jupiter:junit-jupiter-engine:5.0.0-M3",
)
bind(
    name = "junit5_jupiter_engine",
    actual = "@org_junit_jupiter_junit5_jupiter_engine//jar",
)

maven_jar(
    name = "org_opentest4j_opentest4j",
    artifact = "org.opentest4j:opentest4j:1.0.0-M1",
)
bind(
    name = "opentest4j",
    actual = "@org_opentest4j_opentest4j//jar",
)

maven_jar(
    name = "io_reactivex_rxjava2_rxjava2",
    artifact = "io.reactivex.rxjava2:rxjava:2.0.6",
)
bind(
    name = "rxjava2",
    actual = "@io_reactivex_rxjava2_rxjava2//jar",
)

maven_jar(
    name = "org_reactivestreams_reactive_streams",
    artifact = "org.reactivestreams:reactive-streams:1.0.0.final",
)
bind(
    name = "reactive_streams",
    actual = "@org_reactivestreams_reactive_streams//jar",
)

maven_jar(
    name = "net_sf_jopt_simple",
    artifact = "net.sf.jopt-simple:jopt-simple:4.6",
)
bind(
    name = "jopt_simple",
    actual = "@net_sf_jopt_simple//jar",
)

maven_jar(
    name = "ch_qos_cal10n_cal10n_api",
    artifact = "ch.qos.cal10n:cal10n-api:0.8.1",
)
bind(
    name = "cal10n",
    actual = "@ch_qos_cal10n_cal10n_api//jar",
)

maven_jar(
    name = "org_openjdk_jmh_jmh_core",
    artifact = "org.openjdk.jmh:jmh-core:1.18",
)
bind(
    name = "jmh_core",
    actual = "@org_openjdk_jmh_jmh_core//jar",
)
maven_jar(
    name = "org_openjdk_jmh_jmh_generator_annprocess",
    artifact = "org.openjdk.jmh:jmh-generator-annprocess:1.18",
)
bind(
    name = "jmh_generator_annprocess",
    actual = "@org_openjdk_jmh_jmh_generator_annprocess//jar",
)
maven_jar(
    name = "org_apache_commons_commons_math3",
    artifact = "org.apache.commons:commons-math3:3.6.1",
)
bind(
    name = "commons_math3",
    actual = "@org_apache_commons_commons_math3//jar",
)

maven_jar(
    name = "com_google_dagger_dagger",
    artifact = "com.google.dagger:dagger:2.10",
)
bind(
    name = "dagger",
    actual = "@com_google_dagger_dagger//jar",
)
maven_jar(
    name = "com_google_dagger_dagger_compiler",
    artifact = "com.google.dagger:dagger-compiler:2.10",
)
bind(
    name = "dagger-compiler",
    actual = "@com_google_dagger_dagger_compiler//jar",
)

maven_jar(name = "dagger_producers", artifact = "com.google.dagger:dagger-producers:2.10")
maven_jar(name = "google_java_format",     artifact = "com.google.googlejavaformat:google-java-format:1.3")
maven_jar(name = "errorprone_javac",       artifact = "com.google.errorprone:javac:9-dev-r3297-1-shaded")
maven_jar(name = "dagger_javapoet",        artifact = "com.squareup:javapoet:1.7.0")
maven_jar(name = "guava", artifact = "com.google.guava:guava:21.0")


maven_jar(
    name = "com_h2database_h2",
    artifact = "com.h2database:h2:1.4.194",
)
bind(
    name = "h2",
    actual = "@com_h2database_h2//jar",
)
maven_jar(
    name = "org_postgresql_postgresql",
    artifact = "org.postgresql:postgresql:42.0.0",
)
bind(
    name = "postgresql",
    actual = "@org_postgresql_postgresql//jar",
)
maven_jar(
    name = "mysql_mysql_connector_java",
    artifact = "mysql:mysql-connector-java:6.0.6",
)
bind(
    name = "mysql",
    actual = "@mysql_mysql_connector_java//jar",
)
maven_jar(
    name = "com_zaxxer_hikaricp",
    artifact = "com.zaxxer:HikariCP:2.6.1",
)
bind(
    name = "hikaricp",
    actual = "@com_zaxxer_hikaricp//jar",
)

maven_jar(
    name = "de_vandermeer_asciitable",
    artifact = "de.vandermeer:asciitable:0.3.2",
)
bind(
    name = "asciitable",
    actual = "@de_vandermeer_asciitable//jar",
)
maven_jar(
    name = "de_vandermeer_skb_interfaces",
    artifact = "de.vandermeer:skb-interfaces:0.0.2",
)
bind(
    name = "skb_interfaces",
    actual = "@de_vandermeer_skb_interfaces//jar",
)
maven_jar(
    name = "de_vandermeer_ascii_utf_themes",
    artifact = "de.vandermeer:ascii-utf-themes:0.0.1",
)
bind(
    name = "ascii_utf_themes",
    actual = "@de_vandermeer_ascii_utf_themes//jar",
)
maven_jar(
    name = "org_apache_commons_commons_lang3",
    artifact = "org.apache.commons:commons-lang3:3.5",
)
bind(
    name = "commons_lang3",
    actual = "@org_apache_commons_commons_lang3//jar",
)
maven_jar(
    name = "org_antlr_st4",
    artifact = "org.antlr:ST4:4.0.8",
)
bind(
    name = "st4",
    actual = "@org_antlr_st4//jar",
)

