package de.xn__ho_hia.yosql.generator.logging.slf4j;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.generator.api.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;

/**
 * Dagger2 module for slf4j based logging generators.
 */
@Module
@SuppressWarnings("static-method")
public class Slf4jLoggingModule {

    @Slf4j
    @Provides
    LoggingGenerator provideSlf4jLoggingGenerator(final TypicalFields fields) {
        return new Slf4jLoggingGenerator(fields);
    }

}
