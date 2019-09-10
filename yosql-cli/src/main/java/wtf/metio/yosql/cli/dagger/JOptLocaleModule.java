/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli.dagger;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import dagger.Module;
import dagger.Provides;
import joptsimple.OptionParser;
import joptsimple.OptionSpec;
import wtf.metio.yosql.cli.i18n.Commands;
import wtf.metio.yosql.cli.parser.LocaleValueConverter;
import wtf.metio.yosql.model.descriptions.GeneralOptionDescriptions;
import wtf.metio.yosql.model.annotations.Localized;
import wtf.metio.yosql.model.annotations.NonLocalized;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static wtf.metio.yosql.model.options.GeneralOptions.LOCALE;

@Module
class JOptLocaleModule {

    private static final Locale NON_LOCALIZED_LOCALE = Locale.ENGLISH;

    private static final List<Locale> SUPPORTED_LOCALES = Collections.singletonList(NON_LOCALIZED_LOCALE);

    @Localized
    @Provides
    @Singleton
    Locale provideLocalizedLocale(
            @UsedFor.CLI final String[] arguments,
            @NonLocalized final Locale nonLocalizedLocale,
            @NonLocalized final OptionSpec<Locale> localeOption,
            @NonLocalized final OptionParser parser) {
        var locale = nonLocalizedLocale;
        final var optionSet = parser.parse(arguments);
        if (optionSet.has(localeOption)) {
            final var userLocale = optionSet.valueOf(localeOption);
            if (SUPPORTED_LOCALES.contains(userLocale)) {
                locale = userLocale;
            }
        }
        return locale;
    }

    @NonLocalized
    @Provides
    @Singleton
    OptionParser provideOptionParser() {
        final OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        return parser;
    }

    @NonLocalized
    @Provides
    @Singleton
    OptionSpec<Locale> provideLocaleOption(
            @NonLocalized final Locale nonLocalizedLocale,
            @UsedFor.Command(Commands.GENERATE) final OptionParser generateParser,
            @UsedFor.Command(Commands.HELP) final OptionParser helpParser,
            @UsedFor.Command(Commands.VERSION) final OptionParser versionParser) {
        return configureParsers(nonLocalizedLocale, generateParser, helpParser, versionParser);
    }

    private OptionSpec<Locale> configureParsers(
            final Locale nonLocalizedLocale,
            final OptionParser generateParser,
            final OptionParser helpParser,
            final OptionParser versionParser) {
        final var messages = new MessageConveyor(nonLocalizedLocale);
        configureParser(generateParser, nonLocalizedLocale, messages);
        configureParser(helpParser, nonLocalizedLocale, messages);
        return configureParser(versionParser, nonLocalizedLocale, messages);
    }

    private OptionSpec<Locale> configureParser(
            final OptionParser parser,
            final Locale locale,
            final IMessageConveyor messages) {
        return parser.accepts(messages.getMessage(LOCALE))
                .withRequiredArg()
                .withValuesConvertedBy(new LocaleValueConverter())
                .defaultsTo(locale)
                .describedAs(messages.getMessage(GeneralOptionDescriptions.LOCALE_DESCRIPTION));
    }

    @NonLocalized
    @Provides
    @Singleton
    Locale provideInternalLocale() {
        return NON_LOCALIZED_LOCALE;
    }

}
