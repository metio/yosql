/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.i18n;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.model.annotations.*;
import wtf.metio.yosql.model.internal.Loggers;

import javax.inject.Singleton;
import java.util.Locale;

/**
 * I18N module
 */
@Module
public class I18nModule {

    @Provides
    @Singleton
    IMessageConveyor provideIMessageConveyor(final Locale locale) {
        return new MessageConveyor(locale);
    }

    @Provides
    @Singleton
    LocLoggerFactory provideLocLoggerFactory(final IMessageConveyor messages) {
        return new LocLoggerFactory(messages);
    }

    @Writer
    @Provides
    @Singleton
    LocLogger provideWriterLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.WRITER.loggerName);
    }

    @Parser
    @Provides
    @Singleton
    LocLogger provideParserLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.PARSER.loggerName);
    }

    @Reader
    @Provides
    @Singleton
    LocLogger provideReaderLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.READER.loggerName);
    }

    @Generator
    @Provides
    @Singleton
    LocLogger provideGeneratorLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.GENERATOR.loggerName);
    }

    @TimeLogger
    @Provides
    @Singleton
    LocLogger provideTimerLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.TIMER.loggerName);
    }

    @Utilities
    @Provides
    @Singleton
    LocLogger provideUtilitiesLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.UTILITIES.loggerName);
    }

}
