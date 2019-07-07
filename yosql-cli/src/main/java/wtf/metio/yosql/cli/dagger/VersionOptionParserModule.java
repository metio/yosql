/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.cli.i18n.Commands;
import wtf.metio.yosql.cli.parser.YoSqlOptionParser;
import joptsimple.HelpFormatter;
import joptsimple.OptionParser;

@Module
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
