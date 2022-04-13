/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.constants.api.AnnotationApis;
import wtf.metio.yosql.models.constants.api.LoggingApis;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.ApiConfiguration;

public final class Apis {

    public static ApiConfiguration defaults() {
        return ApiConfiguration.usingDefaults().build();
    }

    public static ApiConfiguration jdbc() {
        return ApiConfiguration.copyOf(defaults())
                .withDaoApi(PersistenceApis.JDBC);
    }

    public static ApiConfiguration slf4j() {
        return ApiConfiguration.copyOf(defaults())
                .withLoggingApi(LoggingApis.SLF4J);
    }

    public static ApiConfiguration jul() {
        return ApiConfiguration.copyOf(defaults())
                .withLoggingApi(LoggingApis.JUL);
    }

    public static ApiConfiguration annotationApi() {
        return ApiConfiguration.copyOf(defaults())
                .withAnnotationApi(AnnotationApis.ANNOTATION_API);
    }

    private Apis() {
        // factory class
    }

}
