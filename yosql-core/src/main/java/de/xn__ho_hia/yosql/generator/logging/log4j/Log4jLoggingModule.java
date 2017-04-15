package de.xn__ho_hia.yosql.generator.logging.log4j;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.generator.api.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;

/**
 * Dagger2 module for log4j based logging generators.
 */
@Module
@SuppressWarnings("static-method")
public class Log4jLoggingModule {

    @Log4j
    @Provides
    LoggingGenerator provideLog4jLoggingGenerator(final TypicalFields fields) {
        return new Log4jLoggingGenerator(fields);
    }

}
