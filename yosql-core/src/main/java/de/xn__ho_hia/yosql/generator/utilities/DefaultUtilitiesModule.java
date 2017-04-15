package de.xn__ho_hia.yosql.generator.utilities;

import dagger.Module;
import dagger.Provides;
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
            final ResultRowGenerator resultRowGenerator) {
        return new DefaultUtilitiesGenerator(flowStateGenerator, resultStateGenerator, toResultRowConverterGenerator,
                resultRowGenerator);
    }

}
