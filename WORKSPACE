git_repository(
    name = "bazel_build_process",
    remote = "https://github.com/sebhoss/bazel-build-process.git",
    tag = "0.0.2",
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

maven_jar(
    name = "javax_annotation_javax_annotation_api",
    artifact = "com.google.code.findbugs:jsr305:3.0.0",
)

maven_jar(
    name = "org_inferred_freebuilder",
    artifact = "org.inferred:freebuilder:1.14",
)

maven_jar(
    name = "com_google_auto_value_auto_value",
    artifact = "com.google.auto.value:auto-value:1.2",
)

maven_jar(
    name = "org_yaml_snakeyaml",
    artifact = "org.yaml:snakeyaml:1.17",
)

maven_jar(
    name = "junit_junit",
    artifact = "junit:junit:4.12",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_console",
    artifact = "org.junit.platform:junit-platform-console:1.0.0-M3",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_commons",
    artifact = "org.junit.platform:junit-platform-commons:1.0.0-M3",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_engine",
    artifact = "org.junit.platform:junit-platform-engine:1.0.0-M3",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_launcher",
    artifact = "org.junit.platform:junit-platform-launcher:1.0.0-M3",
)

maven_jar(
    name = "org_junit_platform_junit5_platform_runner",
    artifact = "org.junit.platform:junit-platform-runner:1.0.0-M3",
)

maven_jar(
    name = "org_junit_jupiter_junit5_jupiter_api",
    artifact = "org.junit.jupiter:junit-jupiter-api:5.0.0-M3",
)

maven_jar(
    name = "org_junit_jupiter_junit5_jupiter_engine",
    artifact = "org.junit.jupiter:junit-jupiter-engine:5.0.0-M3",
)

maven_jar(
    name = "org_junit_vintage_junit5_vintage_engine",
    artifact = "org.junit.vintage:junit-vintage-engine:4.12.0-M3",
)

maven_jar(
    name = "org_opentest4j_opentest4j",
    artifact = "org.opentest4j:opentest4j:1.0.0-M1",
)

maven_jar(
    name = "org_testcontainers_testcontainers",
    artifact = "org.testcontainers:testcontainers:1.2.1",
)
maven_jar(
    name = "org_testcontainers_postgresql",
    artifact = "org.testcontainers:postgresql:1.2.1",
)

maven_jar(
    name = "info_cukes_cucumber_java8",
    artifact = "info.cukes:cucumber-java8:1.2.5",
)
maven_jar(
    name = "info_cukes_cucumber_java",
    artifact = "info.cukes:cucumber-java:1.2.5",
)
maven_jar(
    name = "info_cukes_cucumber_core",
    artifact = "info.cukes:cucumber-core:1.2.5",
)
maven_jar(
    name = "info_cukes_gherkin",
    artifact = "info.cukes:gherkin:2.12.2",
)
maven_jar(
    name = "info_cukes_cucumber_jvm_deps",
    artifact = "info.cukes:cucumber-jvm-deps:1.0.5",
)

maven_jar(
    name = "io_reactivex_rxjava2_rxjava2",
    artifact = "io.reactivex.rxjava2:rxjava:2.0.6",
)

maven_jar(
    name = "org_reactivestreams_reactive_streams",
    artifact = "org.reactivestreams:reactive-streams:1.0.0.final",
)

maven_jar(
    name = "net_sf_jopt_simple",
    artifact = "net.sf.jopt-simple:jopt-simple:4.6",
)

maven_jar(
    name = "ch_qos_cal10n_cal10n_api",
    artifact = "ch.qos.cal10n:cal10n-api:0.8.1",
)

maven_jar(
    name = "org_openjdk_jmh_jmh_core",
    artifact = "org.openjdk.jmh:jmh-core:1.18",
)
maven_jar(
    name = "org_openjdk_jmh_jmh_generator_annprocess",
    artifact = "org.openjdk.jmh:jmh-generator-annprocess:1.18",
)
maven_jar(
    name = "org_apache_commons_commons_math3",
    artifact = "org.apache.commons:commons-math3:3.6.1",
)

maven_jar(
    name = "com_google_dagger_dagger",
    artifact = "com.google.dagger:dagger:2.10",
)
maven_jar(
    name = "com_google_dagger_dagger_compiler",
    artifact = "com.google.dagger:dagger-compiler:2.10",
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
maven_jar(
    name = "org_postgresql_postgresql",
    artifact = "org.postgresql:postgresql:42.0.0",
)
maven_jar(
    name = "mysql_mysql_connector_java",
    artifact = "mysql:mysql-connector-java:6.0.6",
)
maven_jar(
    name = "com_zaxxer_hikaricp",
    artifact = "com.zaxxer:HikariCP:2.6.1",
)

maven_jar(
    name = "de_vandermeer_asciitable",
    artifact = "de.vandermeer:asciitable:0.3.2",
)
maven_jar(
    name = "de_vandermeer_skb_interfaces",
    artifact = "de.vandermeer:skb-interfaces:0.0.2",
)
maven_jar(
    name = "de_vandermeer_ascii_utf_themes",
    artifact = "de.vandermeer:ascii-utf-themes:0.0.1",
)
maven_jar(
    name = "org_apache_commons_commons_lang3",
    artifact = "org.apache.commons:commons-lang3:3.5",
)
maven_jar(
    name = "org_antlr_st4",
    artifact = "org.antlr:ST4:4.0.8",
)
