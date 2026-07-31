/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

plugins {
    id("java-gradle-plugin")
}

gradlePlugin {
    val gradleModel by plugins.creating {
        id = "wtf.metio.yosql.models.gradle"
        displayName = "GradleModel"
        description = "Create the Gradle configuration model"
        implementationClass = "wtf.metio.yosql.models.gradle.GradleModelPlugin"
    }
}

dependencies {
    implementation("wtf.metio.yosql.models:yosql-models-generator:${version}")
}
