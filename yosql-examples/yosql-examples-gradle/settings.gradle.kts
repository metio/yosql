rootProject.name = "yosql-examples-gradle"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
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
        gradlePluginPortal()
    }
}

includeBuild("../../yosql-tooling/yosql-tooling-gradle")
include(":yosql-examples-gradle-jdbc-java8")
include(":yosql-examples-gradle-jdbc-java9")
include(":yosql-examples-gradle-jdbc-java11")
include(":yosql-examples-gradle-jdbc-java14")
include(":yosql-examples-gradle-jdbc-java16")
