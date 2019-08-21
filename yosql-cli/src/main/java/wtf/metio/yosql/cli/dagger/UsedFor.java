/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import wtf.metio.yosql.cli.i18n.Commands;
import wtf.metio.yosql.model.GenerateOptions;
import wtf.metio.yosql.model.HelpOptions;
import wtf.metio.yosql.model.VersionOptions;

/**
 * Marker interface used for dagger dependency injection.
 */
@interface UsedFor {

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.PARAMETER })
    @interface CLI {

        // marker annotation

    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.PARAMETER })
    @interface Command {

        Commands value();

    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.PARAMETER })
    @interface GenerateOption {

        GenerateOptions value();

    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.PARAMETER })
    @interface HelpOption {

        HelpOptions value();

    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.PARAMETER })
    @interface VersionOption {

        VersionOptions value();

    }

}
