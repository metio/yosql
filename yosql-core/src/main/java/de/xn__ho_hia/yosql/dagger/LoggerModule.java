/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.dagger;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.Loggers;

/**
 * Dagger2 module for application internal {@link Logger}s.
 */
@Module
@SuppressWarnings("static-method")
public class LoggerModule {

    @Provides
    @Singleton
    LocLoggerFactory provideLocLoggerFactory(@Localized final IMessageConveyor messages) {
        return new LocLoggerFactory(messages);
    }

    @Writer
    @Provides
    @Singleton
    LocLogger provideWriterLocLogger(final LocLoggerFactory factory, @NonLocalized final IMessageConveyor messages) {
        return factory.getLocLogger(messages.getMessage(Loggers.WRITER));
    }

    @Parser
    @Provides
    @Singleton
    LocLogger provideParserLocLogger(final LocLoggerFactory factory, @NonLocalized final IMessageConveyor messages) {
        return factory.getLocLogger(messages.getMessage(Loggers.PARSER));
    }

    @Reader
    @Provides
    @Singleton
    LocLogger provideReaderLocLogger(final LocLoggerFactory factory, @NonLocalized final IMessageConveyor messages) {
        return factory.getLocLogger(messages.getMessage(Loggers.READER));
    }

    @Generator
    @Provides
    @Singleton
    LocLogger provideGeneratorLocLogger(final LocLoggerFactory factory, @NonLocalized final IMessageConveyor messages) {
        return factory.getLocLogger(messages.getMessage(Loggers.GENERATOR));
    }

    @Timer
    @Provides
    @Singleton
    LocLogger provideTimerLocLogger(final LocLoggerFactory factory, @NonLocalized final IMessageConveyor messages) {
        return factory.getLocLogger(messages.getMessage(Loggers.TIMER));
    }

    @Utilities
    @Provides
    @Singleton
    LocLogger provideUtilitiesLocLogger(final LocLoggerFactory factory, @NonLocalized final IMessageConveyor messages) {
        return factory.getLocLogger(messages.getMessage(Loggers.UTILITIES));
    }

    /**
     * Marker annotation for loggers intended for file writers.
     */
    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ TYPE, PARAMETER, METHOD })
    public static @interface Writer {
        // marker annotation
    }

    /**
     * Marker annotation for loggers intended for file generator.
     */
    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ TYPE, PARAMETER, METHOD })
    public static @interface Generator {
        // marker annotation
    }

    /**
     * Marker annotation for loggers intended for file parser.
     */
    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ TYPE, PARAMETER, METHOD })
    public static @interface Parser {
        // marker annotation
    }

    /**
     * Marker annotation for loggers intended for file reader.
     */
    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ TYPE, PARAMETER, METHOD })
    public static @interface Reader {
        // marker annotation
    }

    /**
     * Marker annotation for loggers intended for timers.
     */
    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ TYPE, PARAMETER, METHOD })
    public static @interface Timer {
        // marker annotation
    }

    /**
     * Marker annotation for loggers intended for utilities.
     */
    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ TYPE, PARAMETER, METHOD })
    public static @interface Utilities {
        // marker annotation
    }

}
