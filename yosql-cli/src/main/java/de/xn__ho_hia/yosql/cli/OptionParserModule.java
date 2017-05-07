package de.xn__ho_hia.yosql.cli;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import joptsimple.HelpFormatter;
import joptsimple.OptionParser;

/**
 * Configures all used {@link OptionParser}s.
 */
@Module
@SuppressWarnings("static-method")
public class OptionParserModule {

    @Provides
    @Singleton
    HelpFormatter provideHelpFormatter() {
        return new YoSqlHelpFormatter();
    }

}
