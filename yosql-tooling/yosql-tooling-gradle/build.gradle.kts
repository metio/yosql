/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.2.1"
    id("wtf.metio.yosql.models.gradle")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

gradlePlugin {
    website.set("https://yosql.projects.metio.wtf/")
    vcsUrl.set("https://github.com/metio/yosql.git")
    plugins {
        create("yoSql") {
            id = "wtf.metio.yosql"
            displayName = "YoSQL"
            description = "Code generator that translates SQL to Java"
            tags.set(listOf("java", "sql", "code-generator", "javapoet", "jdbc"))
            implementationClass = "${group}.YoSqlPlugin"
        }
    }
}

dependencies {
    implementation("wtf.metio.yosql.tooling:yosql-tooling-dagger:${version}")
    implementation("wtf.metio.yosql.internals:yosql-internals-jdk-utils:${version}")
    testImplementation(gradleTestKit())
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
}
