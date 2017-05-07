package de.xn__ho_hia.yosql.cli;

import static de.xn__ho_hia.yosql.model.GenerateOptions.LOG_LEVEL;
import static de.xn__ho_hia.yosql.model.GenerateOptions.LOG_LEVEL_DEFAULT;
import static de.xn__ho_hia.yosql.model.GeneralOptionDescriptions.LOCALE_DESCRIPTION;
import static de.xn__ho_hia.yosql.model.GeneralOptions.LOCALE;
import static de.xn__ho_hia.yosql.model.GenerateOptionDescriptions.LOG_LEVEL_DESCRIPTION;

import java.util.Locale;

import de.xn__ho_hia.yosql.model.Translator;
import joptsimple.HelpFormatter;
import joptsimple.OptionParser;
import joptsimple.OptionSpec;

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
