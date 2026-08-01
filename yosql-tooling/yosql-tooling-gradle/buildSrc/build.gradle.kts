/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

plugins {
    id("java-gradle-plugin")
}

gradlePlugin {
    plugins.create("gradleModel") {
        id = "wtf.metio.yosql.models.gradle"
        displayName = "GradleModel"
        description = "Create the Gradle configuration model"
        implementationClass = "wtf.metio.yosql.models.gradle.GradleModelPlugin"
    }
}

dependencies {
    implementation("wtf.metio.yosql.models:yosql-models-generator:${version}")
}
