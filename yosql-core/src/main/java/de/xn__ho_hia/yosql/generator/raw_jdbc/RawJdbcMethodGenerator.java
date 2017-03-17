/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.raw_jdbc;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;

import de.xn__ho_hia.yosql.generator.AbstractMethodsGenerator;
import de.xn__ho_hia.yosql.generator.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.model.SqlType;

import com.squareup.javapoet.MethodSpec;

@SuppressWarnings({ "nls", "javadoc" })
public class RawJdbcMethodGenerator extends AbstractMethodsGenerator {

    private final RawJdbcRxJavaMethodGenerator      rxJavaMethodGenerator;
    private final RawJdbcJava8StreamMethodGenerator java8StreamMethodGenerator;
    private final RawJdbcBatchMethodGenerator       batchMethodGenerator;
    private final RawJdbcStandardMethodGenerator    standardMethodGenerator;
    private final AnnotationGenerator               annotations;

    @Inject
    public RawJdbcMethodGenerator(
            final RawJdbcRxJavaMethodGenerator rxJavaMethodGenerator,
            final RawJdbcJava8StreamMethodGenerator java8StreamMethodGenerator,
            final RawJdbcBatchMethodGenerator batchMethodGenerator,
            final RawJdbcStandardMethodGenerator standardMethodGenerator,
            final AnnotationGenerator annotations) {
        this.rxJavaMethodGenerator = rxJavaMethodGenerator;
        this.java8StreamMethodGenerator = java8StreamMethodGenerator;
        this.batchMethodGenerator = batchMethodGenerator;
        this.standardMethodGenerator = standardMethodGenerator;
        this.annotations = annotations;
    }

    @Override
    protected MethodSpec constructor(final List<SqlStatement> sqlStatementsInRepository) {
        final Builder builder = CodeBlock.builder();
        resultConverters(sqlStatementsInRepository).forEach(converter -> {
            final ClassName converterClass = ClassName.bestGuess(converter.getConverterType());
            builder.addStatement("this.$N = new $T()", converter.getAlias(), converterClass);
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
            final String methodName,
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return standardMethodGenerator.standardReadMethod(methodName, mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec standardWriteMethod(
            final String methodName,
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return standardMethodGenerator.standardWriteMethod(methodName, mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec standardCallMethod(
            final String methodName,
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return standardMethodGenerator.standardCallMethod(methodName, mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec batchWriteMethod(
            final String methodName,
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return batchMethodGenerator.batchMethod(mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec streamEagerReadMethod(
            final String methodName,
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return java8StreamMethodGenerator.streamEagerMethod(mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec streamLazyReadMethod(
            final String methodName,
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return java8StreamMethodGenerator.streamLazyMethod(mergedConfiguration, vendorStatements);
    }

    @Override
    protected MethodSpec rxJavaReadMethod(
            final String methodName,
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return rxJavaMethodGenerator.rxJava2ReadMethod(mergedConfiguration, vendorStatements);
    }

}
