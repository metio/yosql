/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

plugins {
    java
    id("wtf.metio.yosql")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11)) // hikari needs at least Java 11
    }
}

yosql {
    files {
        skipLines.set(6)
        inputBaseDirectory.set(project.file("../../yosql-examples-common/src/main/yosql"))
    }
    repositories {
        basePackageName.set("${group}.persistence")
    }
    java {
        apiVersion.set(8)
        useTextBlocks.set(false)
        useVar.set(false)
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
