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
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

application {
    mainClass.set("${group}.ExampleApp")
}

yosql {
    files {
        skipLines.set(4)
        inputBaseDirectory.set(project.file("../../yosql-examples-common/src/main/yosql"))
    }
    repositories {
        basePackageName.set("${group}.persistence")
    }
    java {
        apiVersion.set(11)
        useTextBlocks.set(false)
    }
    converter {
        mapConverterClass.set("${group}.converter.ToMapConverter")
        rowConverters {
            register("itemConverter") {
                converterType.set("${group}.converter.ToItemConverter")
                methodName.set("asUserType")
                resultType.set("${group}.model.Item")
            }
        }
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
