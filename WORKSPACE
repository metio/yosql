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
    artifact = "net.sf.jopt-simple:jopt-simple:4.9",
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
