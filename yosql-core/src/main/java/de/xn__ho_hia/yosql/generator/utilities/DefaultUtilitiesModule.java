package de.xn__ho_hia.yosql.generator.utilities;

import org.slf4j.cal10n.LocLogger;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.dagger.LoggerModule.Utilities;
import de.xn__ho_hia.yosql.generator.api.UtilitiesGenerator;

/**
 * Dagger2 module for the default utilities generators.
 */
@Module
@SuppressWarnings("static-method")
public final class DefaultUtilitiesModule {

    @Provides
    UtilitiesGenerator provideUtilitiesGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator,
            final @Utilities LocLogger logger) {
        return new DefaultUtilitiesGenerator(flowStateGenerator, resultStateGenerator, toResultRowConverterGenerator,
                resultRowGenerator, logger);
    }

}
