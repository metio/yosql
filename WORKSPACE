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
    name = "io_reactivex_rxjava2_rxjava2",
    artifact = "io.reactivex.rxjava2:rxjava:2.0.6",
)
bind(
    name = "rxjava2",
    actual = "@io_reactivex_rxjava2_rxjava2//jar",
)
