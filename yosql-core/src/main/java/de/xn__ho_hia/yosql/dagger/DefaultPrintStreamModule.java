package de.xn__ho_hia.yosql.dagger;

import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import dagger.Module;
import dagger.Provides;

/**
 * Module for {@link PrintStream}.
 */
@Module
public class DefaultPrintStreamModule {

    @Provides
    @SuppressWarnings({ "static-method", "nls" })
    PrintStream providePrintStream() {
        try {
            return new PrintStream(new BufferedOutputStream(System.out, 8192 * 4), true, "UTF-8");
        } catch (final UnsupportedEncodingException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

}
