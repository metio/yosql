package de.xn__ho_hia.yosql.cli;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

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
