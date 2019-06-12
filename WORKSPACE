load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "2.3"

RULES_JVM_EXTERNAL_SHA = "375b1592e3f4e0a46e6236e19fc30c8020c438803d4d49b13b40aaacd2703c30"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        # std
        "javax.inject:javax.inject:1",
        "javax.annotation:javax.annotation-api:1.3.2",

        # i18n
        "ch.qos.cal10n:cal10n-api:0.8.1",
        "org.slf4j:slf4j-ext:1.7.26",

        # logging
        "org.slf4j:slf4j-api:1.7.26",
        "org.apache.logging.log4j:log4j-api:2.11.2",
        "ch.qos.logback:logback-classic:1.2.3",
        "ch.qos.logback:logback-core:1.2.3",

        # auto-value
        "org.inferred:freebuilder:2.4.1",
        "com.google.auto.value:auto-value:1.6.5",
        "com.google.auto.value:auto-value-annotations:1.6.5",

        # reactive
        "io.reactivex.rxjava2:rxjava:2.2.9",
        "org.reactivestreams:reactive-streams:1.0.2",

        # testing
        "org.testcontainers:testcontainers:1.11.3",
        "org.testcontainers:postgresql:1.11.3",
        "io.cucumber:cucumber-java8:4.3.1",
        "org.junit.jupiter:junit-jupiter-api:5.4.2",

        # cli
        "net.sf.jopt-simple:jopt-simple:5.0.4",
        "de.vandermeer:asciitable:0.3.2",
        "de.vandermeer:skb-interfaces:0.0.2",
        "de.vandermeer:ascii-utf-themes:0.0.1",

        # benchmarks
        "org.openjdk.jmh:jmh-core:1.21",
        "org.openjdk.jmh:jmh-generator-annprocess:1.21",
        "org.apache.commons:commons-math3:3.6.1",

        # dagger
        "com.google.dagger:dagger:2.23.1",
        "com.google.dagger:dagger-compiler:2.23.1",
        "com.google.dagger:dagger-producers:2.23.1",

        # javapoet
        "com.squareup:javapoet:1.11.1",
        "de.xn--ho-hia.javapoet:javapoet-type-guesser:2017.03.19-185842",

        # maven
        "org.apache.maven:maven-model:3.6.1",
        "org.apache.maven.shared:maven-shared-utils:3.2.1",
        "org.apache.maven:maven-plugin-api:3.6.1",
        "org.apache.maven.plugin-tools:maven-plugin-annotations:3.6.0",

        # databases
        "com.h2database:h2:1.4.199",
        "org.postgresql:postgresql:42.2.5",
        "mysql:mysql-connector-java:8.0.16",
        "com.zaxxer:HikariCP:3.3.1",

        # yaml
        "org.yaml:snakeyaml:1.24",

        # misc
        "com.google.googlejavaformat:google-java-format:1.7",
        "com.google.errorprone:javac:9-dev-r3297-1-shaded",
        "com.google.guava:guava:27.1-jre",
        "com.google.code.findbugs:jsr305:3.0.0",
        "org.apache.commons:commons-lang3:3.9",
        "org.antlr:ST4:4.1",
    ],
    fetch_sources = True,
    maven_install_json = "//:maven_install.json",
    repositories = [
        "https://jcenter.bintray.com/",
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()
