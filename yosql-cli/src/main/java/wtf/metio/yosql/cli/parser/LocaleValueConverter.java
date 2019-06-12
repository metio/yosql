/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli.parser;

import java.util.Locale;

import joptsimple.ValueConverter;

/**
 * Convert {@link String} based inputs into {@link Locale}s.
 */
public class LocaleValueConverter implements ValueConverter<Locale> {

    @Override
    public Locale convert(final String input) {
        return Locale.forLanguageTag(input);
    }

    @Override
    public String valuePattern() {
        return "en"; //$NON-NLS-1$
    }

    @Override
    public Class<Locale> valueType() {
        return Locale.class;
    }

}
