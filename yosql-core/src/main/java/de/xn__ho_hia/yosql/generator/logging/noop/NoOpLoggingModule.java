package de.xn__ho_hia.yosql.generator.logging.noop;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.generator.api.LoggingGenerator;

/**
 * Dagger2 module for no-op logging generators.
 */
@Module
@SuppressWarnings("static-method")
public class NoOpLoggingModule {

    @NoOp
    @Provides
    LoggingGenerator provideNoOpLoggingGenerator() {
        return new NoOpLoggingGenerator();
    }

}
