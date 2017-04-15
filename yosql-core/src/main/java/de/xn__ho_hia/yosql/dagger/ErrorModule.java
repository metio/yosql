package de.xn__ho_hia.yosql.dagger;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * Dagger2 module for everything related to errors.
 */
@Module
@SuppressWarnings("static-method")
public class ErrorModule {

    @Provides
    ExecutionErrors provideExecutionErrors() {
        return new ExecutionErrors();
    }

}
