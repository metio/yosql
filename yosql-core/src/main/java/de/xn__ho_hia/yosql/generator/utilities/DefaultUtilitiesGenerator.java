/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.utilities;

import java.util.List;
import java.util.stream.Stream;

import org.slf4j.cal10n.LocLogger;

import de.xn__ho_hia.yosql.generator.api.UtilitiesGenerator;
import de.xn__ho_hia.yosql.model.ApplicationEvents;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;
import de.xn__ho_hia.yosql.model.SqlStatement;

final class DefaultUtilitiesGenerator implements UtilitiesGenerator {

    private final FlowStateGenerator            flowStateGenerator;
    private final ResultStateGenerator          resultStateGenerator;
    private final ToResultRowConverterGenerator toResultRowConverterGenerator;
    private final ResultRowGenerator            resultRowGenerator;
    private final LocLogger                     logger;

    DefaultUtilitiesGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator,
            final LocLogger logger) {
        this.flowStateGenerator = flowStateGenerator;
        this.resultStateGenerator = resultStateGenerator;
        this.toResultRowConverterGenerator = toResultRowConverterGenerator;
        this.resultRowGenerator = resultRowGenerator;
        this.logger = logger;
    }

    @Override
    public Stream<PackageTypeSpec> generateUtilities(final List<SqlStatement> allStatements) {
        PackageTypeSpec resultStateClass = null;
        PackageTypeSpec flowStateClass = null;
        PackageTypeSpec toResultRowConverterClass = null;
        PackageTypeSpec resultRowClass = null;
        for (final SqlStatement statement : allStatements) {
            if (resultStateClass == null && statement.isReading()) {
                resultStateClass = resultStateGenerator.generateResultStateClass();
                logger.debug(ApplicationEvents.TYPE_GENERATED, resultStateClass.getPackageName(),
                        resultStateClass.getType().name);
            }
            if (flowStateClass == null && statement.shouldGenerateRxJavaAPI()) {
                flowStateClass = flowStateGenerator.generateFlowStateClass();
                logger.debug(ApplicationEvents.TYPE_GENERATED, flowStateClass.getPackageName(),
                        flowStateClass.getType().name);
            }
            if (toResultRowConverterClass == null && resultRowClass == null
                    && statement.getConfiguration().getResultRowConverter().getConverterType()
                            .endsWith(ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME)) {
                toResultRowConverterClass = toResultRowConverterGenerator.generateToResultRowConverterClass();
                logger.debug(ApplicationEvents.TYPE_GENERATED, toResultRowConverterClass.getPackageName(),
                        toResultRowConverterClass.getType().name);
                resultRowClass = resultRowGenerator.generateResultRowClass();
                logger.debug(ApplicationEvents.TYPE_GENERATED, resultRowClass.getPackageName(),
                        resultRowClass.getType().name);
            }
            if (resultStateClass != null
                    && flowStateClass != null
                    && toResultRowConverterClass != null
                    && resultRowClass != null) {
                break;
            }
        }
        return Stream.of(resultStateClass, flowStateClass, toResultRowConverterClass, resultRowClass);
    }

}
