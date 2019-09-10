/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli.dagger;

import joptsimple.HelpFormatter;
import joptsimple.OptionParser;
import joptsimple.OptionSpec;
import wtf.metio.yosql.i18n.Translator;

import java.util.Locale;

import static wtf.metio.yosql.model.descriptions.GeneralOptionDescriptions.LOCALE_DESCRIPTION;
import static wtf.metio.yosql.model.descriptions.GenerateOptionDescriptions.LOG_LEVEL_DESCRIPTION;
import static wtf.metio.yosql.model.options.GeneralOptions.LOCALE;
import static wtf.metio.yosql.model.options.GenerateOptions.LOG_LEVEL;
import static wtf.metio.yosql.model.options.GenerateOptions.LOG_LEVEL_DEFAULT;

abstract class AbstractOptionParserModule {

    protected static OptionParser createParser(final HelpFormatter helpFormatter) {
        final OptionParser parser = new OptionParser();
        parser.formatHelpWith(helpFormatter);
        return parser;
    }

    protected static OptionSpec<Locale> createLocaleOption(
            final OptionParser parser,
            final Translator translator,
            final Locale locale) {
        return parser.accepts(translator.nonLocalized(LOCALE))
                .withRequiredArg()
                .ofType(Locale.class)
                .defaultsTo(locale)
                .describedAs(translator.localized(LOCALE_DESCRIPTION));
    }

    protected static OptionSpec<String> createLogLevelOption(
            final OptionParser parser,
            final Translator translator) {
        return parser.accepts(translator.nonLocalized(LOG_LEVEL))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(translator.nonLocalized(LOG_LEVEL_DEFAULT))
                .describedAs(translator.localized(LOG_LEVEL_DESCRIPTION));
    }

}
