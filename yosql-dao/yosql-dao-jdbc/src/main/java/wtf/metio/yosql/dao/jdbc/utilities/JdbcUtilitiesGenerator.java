/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc.utilities;

import wtf.metio.yosql.codegen.api.UtilitiesGenerator;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

public final class JdbcUtilitiesGenerator implements UtilitiesGenerator {

    private final FlowStateGenerator flowStateGenerator;
    private final ResultStateGenerator resultStateGenerator;
    private final ToResultRowConverterGenerator toResultRowConverterGenerator;
    private final ResultRowGenerator resultRowGenerator;
    private final JdbcConfiguration jdbc;

    public JdbcUtilitiesGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator,
            final JdbcConfiguration jdbc) {
        this.flowStateGenerator = flowStateGenerator;
        this.resultStateGenerator = resultStateGenerator;
        this.toResultRowConverterGenerator = toResultRowConverterGenerator;
        this.resultRowGenerator = resultRowGenerator;
        this.jdbc = jdbc;
    }

    @Override
    public Stream<PackagedTypeSpec> generateUtilities(final List<SqlStatement> allStatements) {
        PackagedTypeSpec resultStateClass = null;
        PackagedTypeSpec flowStateClass = null;
        PackagedTypeSpec toResultRowConverterClass = null;
        PackagedTypeSpec resultRowClass = null;

        for (final SqlStatement statement : allStatements) {
            if (resultStateClass == null) {
                resultStateClass = resultStateGenerator.generateResultStateClass();
            }
            if (flowStateClass == null) {
                flowStateClass = flowStateGenerator.generateFlowStateClass();
            }
            if (statement.getConfiguration().resultRowConverter().orElse(jdbc.defaultConverter().orElseThrow()).converterType()
                    .endsWith(ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME)) {
                toResultRowConverterClass = toResultRowConverterGenerator.generateToResultRowConverterClass();
                resultRowClass = resultRowGenerator.generateResultRowClass();
            }
            if (toResultRowConverterClass != null) {
                break;
            }
        }

        return Stream.of(resultStateClass, flowStateClass, toResultRowConverterClass, resultRowClass);
    }

}
