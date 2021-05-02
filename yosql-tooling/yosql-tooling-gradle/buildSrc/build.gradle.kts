/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
