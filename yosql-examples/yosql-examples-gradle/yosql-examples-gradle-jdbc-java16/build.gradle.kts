/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

plugins {
    java
    id("wtf.metio.yosql")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

yosql {
    files {
        skipLines.set(6)
    }
    repositories {
        basePackageName.set("${group}.persistence")
    }
    java {
        apiVersion.set(16)
    }
    jdbc {
        utilityPackageName.set("${group}.persistence.util")
        userTypes {
            register("itemConverter") {
                converterType.set("${group}.converter.ToItemConverter")
                methodName.set("asUserType")
                resultType.set("${group}.model.Item")
            }
        }
    }
}

dependencies {
    implementation("com.zaxxer:HikariCP:latest.release") {
        because("we want to use a connection pool")
    }
    implementation("com.h2database:h2:latest.release") {
        because("we want to use an in-memory database")
    }
    implementation("io.reactivex.rxjava2:rxjava:latest.release") {
        because("we want to show reactive access")
    }
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
}
