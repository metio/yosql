/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.dao.generic;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;
import com.squareup.javapoet.MethodSpec;

import de.xn__ho_hia.yosql.generator.api.AbstractMethodsGenerator;
import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.api.BatchMethodGenerator;
import de.xn__ho_hia.yosql.generator.api.Java8StreamMethodGenerator;
import de.xn__ho_hia.yosql.generator.api.MethodsGenerator;
import de.xn__ho_hia.yosql.generator.api.RxJavaMethodGenerator;
import de.xn__ho_hia.yosql.generator.api.StandardMethodGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.model.SqlType;

/**
 * Generic implementation of a {@link MethodsGenerator}. Delegates most of its work to the injected members.
 */
public final class GenericMethodsGenerator extends AbstractMethodsGenerator {

    private final BatchMethodGenerator       batchMethods;
    private final Java8StreamMethodGenerator streamMethods;
    private final RxJavaMethodGenerator      rxjavaMethods;
    private final StandardMethodGenerator    standardMethods;
    private final AnnotationGenerator        annotations;

    /**
     * @param batchMethods
     *            The batch methods generator to use.
     * @param streamMethods
     *            The Java8 stream methods generator to use.
     * @param rxjavaMethods
     *            The RxJava methods generator to use.
     * @param standardMethods
     *            The standard methods generator to use.
     * @param annotations
     *            The annotation generator to use.
     */
    public GenericMethodsGenerator(
            final BatchMethodGenerator batchMethods,
            final Java8StreamMethodGenerator streamMethods,
            final RxJavaMethodGenerator rxjavaMethods,
            final StandardMethodGenerator standardMethods,
            final AnnotationGenerator annotations) {
        this.batchMethods = batchMethods;
        this.streamMethods = streamMethods;
        this.rxjavaMethods = rxjavaMethods;
        this.standardMethods = standardMethods;
        this.annotations = annotations;
    }

    @Override
    protected MethodSpec constructor(final List<SqlStatement> sqlStatementsInRepository) {
        final Builder builder = CodeBlock.builder();
        resultConverters(sqlStatementsInRepository).forEach(converter -> {
            builder.add(TypicalCodeBlocks.initializeConverter(converter));
        });

        return TypicalMethods.constructor()
                .addAnnotations(annotations.generatedMethod(getClass()))
                .addParameter(TypicalParameters.dataSource())
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.DATA_SOURCE))
                .addCode(builder.build())
                .build();
    }

    private static Stream<ResultRowConverter> resultConverters(final List<SqlStatement> sqlStatements) {
        return sqlStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.getType() || SqlType.CALLING == config.getType())
                .map(SqlConfiguration::getResultRowConverter)
                .filter(Objects::nonNull)
                .distinct();
    }

    @Override
    protected MethodSpec standardReadMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return standardMethods.standardReadMethod(mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec standardWriteMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return standardMethods.standardWriteMethod(mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec standardCallMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return standardMethods.standardCallMethod(mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec batchWriteMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return batchMethods.batchWriteMethod(mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec streamEagerReadMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return streamMethods.streamEagerMethod(mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec streamLazyReadMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return streamMethods.streamLazyMethod(mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec rxJavaReadMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return rxjavaMethods.rxJava2ReadMethod(mergedConfiguration, vendorStatements);
    }

}
