git_repository(
    name = "bazel_build_process",
    remote = "https://github.com/sebhoss/bazel-build-process.git",
    tag = "0.0.3",
)

load("@bazel_build_process//dependencies:sebhoss.bzl", "javapoet_type_guesser")
load("@bazel_build_process//dependencies:square.bzl", "javapoet_dependencies")
load("@bazel_build_process//dependencies:logging.bzl", "logging_dependencies")
load("@bazel_build_process//dependencies:junit.bzl", "junit_dependencies")
load("@bazel_build_process//dependencies:yaml.bzl", "snakeyaml_dependencies")

maven_server(
  name = "metio",
  url = "https://repository.metio.wtf/repository/maven-public/",
)

javapoet_type_guesser()
javapoet_dependencies()
logging_dependencies()
junit_dependencies()
snakeyaml_dependencies()

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
