/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli;

import java.util.Locale;

import joptsimple.ValueConverter;

/**
 * Convert {@link String} based inputs into {@link Locale}s.
 */
public class LocaleValueConverter implements ValueConverter<Locale> {

    @Override
    public Locale convert(final String arg0) {
        return Locale.forLanguageTag(arg0);
    }

    @Override
    public String valuePattern() {
        return "/path/to/your/files"; //$NON-NLS-1$
    }

    @Override
    public Class<Locale> valueType() {
        return Locale.class;
    }

}
