/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import de.xn__ho_hia.yosql.cli.i18n.Commands;
import de.xn__ho_hia.yosql.model.GenerateOptions;
import de.xn__ho_hia.yosql.model.HelpOptions;
import de.xn__ho_hia.yosql.model.VersionOptions;

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
