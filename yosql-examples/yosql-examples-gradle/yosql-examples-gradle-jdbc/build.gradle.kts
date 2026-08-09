/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

plugins {
    java
    application
    id("wtf.metio.yosql")
}

java {
    toolchain {
        // The baseline generated code is built and run against.
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

application {
    mainClass.set("${group}.ExampleApp")
}

yosql {
    files {
        inputBaseDirectory.set(project.file("../../yosql-examples-common/src/main/yosql"))
        sourceDirectory.set(project.file("../../yosql-examples-common/src/main/java"))
    }
    repositories {
        basePackageName.set("${group}.persistence")
    }
}

dependencies {
    implementation(libs.bundles.database) {
        because("we need database access")
    }
    implementation("wtf.metio.yosql.examples:yosql-examples-common:${version}") {
        because("we want to re-use the same example app across all example projects")
    }
}
