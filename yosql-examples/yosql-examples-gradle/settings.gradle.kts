/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

rootProject.name = "yosql-examples-gradle"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri(System.getProperty("user.home") + "/.cache/maven/repository")
        }
    }
}

pluginManagement {
    val version = providers.gradleProperty("version").get()
    plugins {
        id("wtf.metio.yosql") version version
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "wtf.metio.yosql") {
                useModule(":yosql-tooling-gradle")
            }
        }
    }
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri(System.getProperty("user.home") + "/.cache/maven/repository")
        }
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

includeBuild("../../yosql-tooling/yosql-tooling-gradle")
include(":yosql-examples-gradle-jdbc")
