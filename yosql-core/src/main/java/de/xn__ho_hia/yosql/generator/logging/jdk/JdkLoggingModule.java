package de.xn__ho_hia.yosql.generator.logging.jdk;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.generator.api.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;

/**
 * Dagger2 module for java.util.logging based logging generators.
 */
@Module
@SuppressWarnings("static-method")
public class JdkLoggingModule {

    @JDK
    @Provides
    LoggingGenerator provideJdkLoggingGenerator(final TypicalFields fields) {
        return new JdkLoggingGenerator(fields);
    }

}
