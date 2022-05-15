/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.12.0"
    id("wtf.metio.yosql.models.gradle")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

gradlePlugin {
    val yoSql by plugins.creating {
        id = "wtf.metio.yosql"
        displayName = "YoSQL"
        description = "Code generator that translates SQL to Java"
        implementationClass = "${group}.YoSqlPlugin"
    }
}

pluginBundle {
    website = "https://yosql.projects.metio.wtf/"
    vcsUrl = "https://github.com/metio/yosql/"
    tags = listOf("java", "sql", "code-generator", "javapoet", "jdbc")
}

dependencies {
    implementation("wtf.metio.yosql.tooling:yosql-tooling-dagger:${version}")
    implementation("wtf.metio.yosql.internals:yosql-internals-jdk-utils:${version}")
    testImplementation(gradleTestKit())
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
}
