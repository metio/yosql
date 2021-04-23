rootProject.name = "yosql-examples-gradle-jdbc-java16"

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

includeBuild("../../../yosql-tooling/yosql-tooling-gradle")
