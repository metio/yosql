package de.xn__ho_hia.yosql.benchmark;

import java.io.OutputStream;
import java.io.PrintStream;

import dagger.Module;
import dagger.Provides;

/**
 * Module for {@link PrintStream}.
 */
@Module
public class NoOpPrintStreamModule {

    @Provides
    @SuppressWarnings({ "static-method" })
    PrintStream providePrintStream() {
        return new PrintStream(new OutputStream() {

            @Override
            public void write(final int b) {
                // no-op
            }

        });
    }

}
