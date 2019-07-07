/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.utilities;

import wtf.metio.yosql.model.PackageTypeSpec;
import wtf.metio.yosql.model.SqlStatement;
import wtf.metio.yosql.generator.api.UtilitiesGenerator;

import java.util.List;
import java.util.stream.Stream;

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
    public Stream<PackageTypeSpec> generateUtilities(final List<SqlStatement> allStatements) {
        PackageTypeSpec resultStateClass = null;
        PackageTypeSpec flowStateClass = null;
        PackageTypeSpec toResultRowConverterClass = null;
        PackageTypeSpec resultRowClass = null;
        for (final SqlStatement statement : allStatements) {
            if (resultStateClass == null) {
                resultStateClass = resultStateGenerator.generateResultStateClass();
            }
            if (flowStateClass == null) {
                flowStateClass = flowStateGenerator.generateFlowStateClass();
            }
            if (toResultRowConverterClass == null && resultRowClass == null
                    && statement.getConfiguration().getResultRowConverter().getConverterType()
                            .endsWith(ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME)) {
                toResultRowConverterClass = toResultRowConverterGenerator.generateToResultRowConverterClass();
                resultRowClass = resultRowGenerator.generateResultRowClass();
            }
            if (toResultRowConverterClass != null && resultRowClass != null) {
                break;
            }
        }
        return Stream.of(resultStateClass, flowStateClass, toResultRowConverterClass, resultRowClass);
    }

}
