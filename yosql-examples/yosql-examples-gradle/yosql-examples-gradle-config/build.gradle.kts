/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

import java.nio.charset.StandardCharsets
import wtf.metio.yosql.models.configuration.LoggingApis
import wtf.metio.yosql.models.configuration.SchemaValidation

plugins {
    java
    id("wtf.metio.yosql")
}

java {
    toolchain {
        // The baseline generated code is built and run against.
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

// Every configuration group, set to something other than its default, so that the DSL the
// meta-model generates for Gradle is exercised rather than merely compiled. Each frontend gets its
// own generated surface with its own initializers: a setting that reaches the generator through
// Maven says nothing about the same setting through Gradle, and the Maven reactor never builds
// this. What the values are matters less than that the tests below can see them arrive.
yosql {
    files {
        sqlFilesCharset.set(StandardCharsets.UTF_8)
    }
    repositories {
        basePackageName.set("wtf.metio.yosql.example.gradle.config.persistence")
    }
    converter {
        // Left where they land, which is beside the repositories, so that the default the
        // repositories decide is checked rather than a package named twice.
        recordConverterPrefix.set("Build")
        recordConverterSuffix.set("Mapper")
        recordConverterMethod.set("asRecord")
    }
    schema {
        sqlStatementsDirectory.set("src/main/schema")
        // Without this the DDL's jsonb column is described and untyped, and the record below
        // cannot be written — so the build failing is what this setting not arriving looks like.
        vendor.set("PostgreSQL")
        validation.set(SchemaValidation.ERROR)
    }
    annotations {
        // A container rather than a property, which is a shape only the Gradle DSL has.
        repositoryAnnotations {
            register("java.lang.SuppressWarnings") {
                members {
                    register("value") {
                        value.set("gradle-config-example")
                    }
                }
            }
        }
    }
    logging {
        api.set(LoggingApis.JUL)
    }
    resources {
        maxThreads.set(1)
    }
}

dependencies {
    testImplementation(libs.junit)
    testRuntimeOnly(libs.junitLauncher)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    // The tests read what the generator wrote, so they are only meaningful after it has run. The
    // compile task already depends on it; saying so here keeps that true if the sources move.
    dependsOn("generateJavaCode")
    systemProperty("yosql.generated", layout.buildDirectory.dir("generated/sources/yosql").get().asFile.path)
}
