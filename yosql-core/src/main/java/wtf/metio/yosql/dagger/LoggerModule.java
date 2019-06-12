/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dagger;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.model.Loggers;
import org.slf4j.Logger;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.model.annotations.*;

import javax.inject.Singleton;

/**
 * Dagger module for application internal {@link Logger}s.
 */
@Module
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

}
