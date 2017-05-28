/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli.parser;

import de.xn__ho_hia.yosql.model.ResultRowConverter;
import joptsimple.ValueConverter;

/**
 * Converts {@link String}s to {@link ResultRowConverter}s.
 */
public class ResultRowConverterValueConverter implements ValueConverter<ResultRowConverter> {

    @Override
    public ResultRowConverter convert(final String input) {
        final String[] segments = input.split(":"); //$NON-NLS-1$
        if (segments != null && segments.length == 3) {
            final ResultRowConverter converter = new ResultRowConverter();
            converter.setAlias(segments[0]);
            converter.setConverterType(segments[1]);
            converter.setResultType(segments[2]);
            return converter;
        }
        throw new IllegalArgumentException("Cannot convert: " + input); //$NON-NLS-1$
    }

    @Override
    public String valuePattern() {
        return "alias:converterType:resultType"; //$NON-NLS-1$
    }

    @Override
    public Class<ResultRowConverter> valueType() {
        return ResultRowConverter.class;
    }

}
