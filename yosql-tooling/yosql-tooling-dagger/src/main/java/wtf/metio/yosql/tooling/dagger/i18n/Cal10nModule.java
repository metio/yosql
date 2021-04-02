/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.i18n;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.codegen.logging.*;

import javax.inject.Singleton;
import java.util.Locale;

/**
 * I18N module
 */
@Module
public class Cal10nModule {

    @Provides
    @Singleton
    public IMessageConveyor provideIMessageConveyor(final Locale locale) {
        return new MessageConveyor(locale);
    }

    @Provides
    @Singleton
    public LocLoggerFactory provideLocLoggerFactory(final IMessageConveyor messages) {
        return new LocLoggerFactory(messages);
    }

    @Writer
    @Provides
    @Singleton
    public LocLogger provideWriterLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.WRITER.loggerName);
    }

    @Parser
    @Provides
    @Singleton
    public LocLogger provideParserLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.PARSER.loggerName);
    }

    @Reader
    @Provides
    @Singleton
    public LocLogger provideReaderLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.READER.loggerName);
    }

    @Generator
    @Provides
    @Singleton
    public LocLogger provideGeneratorLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.GENERATOR.loggerName);
    }

    @TimeLogger
    @Provides
    @Singleton
    public LocLogger provideTimerLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.TIMER.loggerName);
    }

    @Utilities
    @Provides
    @Singleton
    public LocLogger provideUtilitiesLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger(Loggers.UTILITIES.loggerName);
    }

}
