package de.xn__ho_hia.yosql.cli;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import joptsimple.HelpFormatter;
import joptsimple.OptionParser;

@Module
@SuppressWarnings("static-method")
class VersionOptionParserModule extends AbstractOptionParserModule {

    @Provides
    @Singleton
    @UsedFor.Command(Commands.VERSION)
    OptionParser provideParser(final HelpFormatter helpFormatter) {
        return createParser(helpFormatter);
    }

    @Provides
    @Singleton
    @UsedFor.Command(Commands.VERSION)
    YoSqlOptionParser provideYoSqlOptionParser(
            @UsedFor.Command(Commands.VERSION) final OptionParser parser) {
        return new YoSqlOptionParser(parser);
    }

}
