/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
    val version: String by settings
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

includeBuild("../../yosql-tooling/yosql-tooling-gradle")
include(":yosql-examples-gradle-jdbc-java8")
include(":yosql-examples-gradle-jdbc-java11")
include(":yosql-examples-gradle-jdbc-java15")
include(":yosql-examples-gradle-jdbc-java16")
