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
