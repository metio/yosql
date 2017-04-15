/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.utilities;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import de.xn__ho_hia.yosql.generator.api.UtilitiesGenerator;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.model.SqlType;

final class DefaultUtilitiesGenerator implements UtilitiesGenerator {

    private final FlowStateGenerator            flowStateGenerator;
    private final ResultStateGenerator          resultStateGenerator;
    private final ToResultRowConverterGenerator toResultRowConverterGenerator;
    private final ResultRowGenerator            resultRowGenerator;

    DefaultUtilitiesGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator) {
        this.flowStateGenerator = flowStateGenerator;
        this.resultStateGenerator = resultStateGenerator;
        this.toResultRowConverterGenerator = toResultRowConverterGenerator;
        this.resultRowGenerator = resultRowGenerator;
    }

    @Override
    public void generateUtilities(final List<SqlStatement> allStatements) {
        if (allStatements.parallelStream()
                .anyMatch(SqlStatement::isReading)) {
            resultStateGenerator.generateResultStateClass();
        }
        if (allStatements.parallelStream()
                .anyMatch(SqlStatement::shouldGenerateRxJavaAPI)) {
            flowStateGenerator.generateFlowStateClass();
        }
        if (resultConverters(allStatements)
                .anyMatch(converter -> converter.getConverterType().endsWith(
                        ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME))) {
            toResultRowConverterGenerator.generateToResultRowConverterClass();
            resultRowGenerator.generateResultRowClass();
        }
    }

    private static Stream<ResultRowConverter> resultConverters(final List<SqlStatement> sqlStatements) {
        return sqlStatements.parallelStream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.getType())
                .map(SqlConfiguration::getResultRowConverter)
                .filter(Objects::nonNull)
                .distinct();
    }

}
