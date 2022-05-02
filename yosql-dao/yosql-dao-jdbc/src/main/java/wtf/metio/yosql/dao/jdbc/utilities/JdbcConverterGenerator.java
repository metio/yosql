/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc.utilities;

import wtf.metio.yosql.codegen.api.ConverterGenerator;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

public final class JdbcConverterGenerator implements ConverterGenerator {

    private final FlowStateGenerator flowStateGenerator;
    private final ResultStateGenerator resultStateGenerator;
    private final ToResultRowConverterGenerator toResultRowConverterGenerator;
    private final ResultRowGenerator resultRowGenerator;
    private final ConverterConfiguration converters;

    public JdbcConverterGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator,
            final ConverterConfiguration converters) {
        this.flowStateGenerator = flowStateGenerator;
        this.resultStateGenerator = resultStateGenerator;
        this.toResultRowConverterGenerator = toResultRowConverterGenerator;
        this.resultRowGenerator = resultRowGenerator;
        this.converters = converters;
    }

    @Override
    public Stream<PackagedTypeSpec> generateConverterClasses(final List<SqlStatement> allStatements) {
        PackagedTypeSpec resultStateClass = null;
        PackagedTypeSpec flowStateClass = null;
        PackagedTypeSpec toResultRowConverterClass = null;
        PackagedTypeSpec resultRowClass = null;

        for (final var statement : allStatements) {
            if (resultStateClass == null) {
                resultStateClass = resultStateGenerator.generateResultStateClass();
            }
            if (flowStateClass == null) {
                flowStateClass = flowStateGenerator.generateFlowStateClass();
            }
            // TODO: comparing with the name of a class seems fishy and is not documented
            if (statement.getConfiguration().resultRowConverter().or(converters::defaultConverter).orElseThrow().converterType()
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
