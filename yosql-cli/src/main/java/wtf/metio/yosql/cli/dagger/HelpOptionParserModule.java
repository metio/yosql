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
import wtf.metio.yosql.model.HelpOptionDescriptions;
import wtf.metio.yosql.model.HelpOptions;
import wtf.metio.yosql.model.Translator;
import joptsimple.HelpFormatter;
import joptsimple.OptionParser;
import joptsimple.OptionSpec;

@Module
class HelpOptionParserModule extends AbstractOptionParserModule {

    @Provides
    @Singleton
    @UsedFor.Command(Commands.HELP)
    OptionParser provideParser(final HelpFormatter helpFormatter) {
        return createParser(helpFormatter);
    }

    @Provides
    @Singleton
    @UsedFor.HelpOption(HelpOptions.COMMAND)
    OptionSpec<String> provideHelpCommandOption(
            @UsedFor.Command(Commands.HELP) final OptionParser parser,
            final Translator translator) {
        return parser.accepts(translator.nonLocalized(HelpOptions.COMMAND))
                .withOptionalArg()
                .describedAs(translator.localized(HelpOptionDescriptions.COMMAND_DESCRIPTION))
                .forHelp();
    }

    @Provides
    @Singleton
    @UsedFor.Command(Commands.HELP)
    YoSqlOptionParser provideYoSqlOptionParser(
            @UsedFor.Command(Commands.HELP) final OptionParser parser,
            @UsedFor.HelpOption(HelpOptions.COMMAND) final OptionSpec<String> command) {
        return new YoSqlOptionParser(parser, command);
    }

}
