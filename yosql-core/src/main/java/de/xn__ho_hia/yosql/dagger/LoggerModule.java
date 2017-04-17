package de.xn__ho_hia.yosql.dagger;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.slf4j.Logger;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;

/**
 * Dagger2 module for application internal {@link Logger}s.
 */
@Module
@SuppressWarnings("static-method")
public class LoggerModule {

    @Provides
    LocLoggerFactory provideLocLoggerFactory(final IMessageConveyor messages) {
        return new LocLoggerFactory(messages);
    }

    @Writer
    @Provides
    LocLogger provideWriterLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger("yosql.writer"); //$NON-NLS-1$
    }

    @Parser
    @Provides
    LocLogger provideParserLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger("yosql.parser"); //$NON-NLS-1$
    }

    @Generator
    @Provides
    LocLogger provideGeneratorLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger("yosql.generator"); //$NON-NLS-1$
    }

    @Timer
    @Provides
    LocLogger provideTimerLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger("yosql.timer"); //$NON-NLS-1$
    }

    @Utilities
    @Provides
    LocLogger provideUtilitiesLocLogger(final LocLoggerFactory factory) {
        return factory.getLocLogger("yosql.utilities"); //$NON-NLS-1$
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
