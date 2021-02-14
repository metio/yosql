/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.ConverterConfiguration;
import wtf.metio.yosql.model.sql.ParameterConverter;
import wtf.metio.yosql.model.sql.ResultRowConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Configures converters.
 */
public class Converters {

    /**
     * Optional list of converters that are applied to input parameters.
     */
    private final List<ParameterConverter> parameterConverters = new ArrayList<>();

    /**
     * Optional list of converters that are applied to input parameters.
     */
    private final List<Converter> resultRowConverters = new ArrayList<>();

    /**
     * The default row converter which is being used if no custom converter is specified for a statement. Can be either
     * the alias or fully-qualified name of a converter. Default 'resultRow'.
     */
    private final String defaultRowConverter = "resultRow";

    public ConverterConfiguration asConfiguration() {
        final var toResultRow = ResultRowConverter.builder()
                .setAlias(defaultRowConverter)
                .setResultType("com.example.persistence.util.ResultRow")
                .setConverterType("ToResultRowConverter")
                .build();
        return ConverterConfiguration.builder()
                .setBasePackageName("com.example.persistence.converter")
                .setDefaultConverter(toResultRow)
                .setConverters(Set.of(toResultRow))
                .build();
    }

}
