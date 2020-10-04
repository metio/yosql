/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.sql.ParameterConverter;
import wtf.metio.yosql.model.sql.ResultRowConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Configures converters.
 */
public class Converter {

    /**
     * Optional list of converters that are applied to input parameters.
     */
    private final List<ParameterConverter> parameterConverters = new ArrayList<>();

    /**
     * Optional list of converters that are applied to input parameters.
     */
    private final List<ResultRowConverter> resultRowConverters = new ArrayList<>();

    /**
     * The default row converter which is being used if no custom converter is specified for a statement. Can be either
     * the alias or fully-qualified name of a converter. Default 'resultRow'.
     */
    private final String defaultRowConverter = "ToResultRowConverter";

    //        final RuntimeConfiguration.Builder builder = RuntimeConfiguration.builder()
//                .setDefaultRowConverter(defaultRowConverter);
//
//        final ResultRowConverter toResultRow = new ResultRowConverter();
//        toResultRow.setAlias("resultRow");
//        toResultRow.setResultType(utilPackage + "resultRow");
//        toResultRow.setConverterType(
//                basePackageName
//                        + ""
//                        + converterPackageName
//                        + "ToResultRowConverter");
//        resultRowConverters.add(toResultRow);
//        builder.setResultRowConverters(resultRowConverters);

}
