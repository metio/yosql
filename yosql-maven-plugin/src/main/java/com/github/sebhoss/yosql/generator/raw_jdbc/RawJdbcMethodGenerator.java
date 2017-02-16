package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.github.sebhoss.yosql.generator.AbstractMethodsGenerator;
import com.github.sebhoss.yosql.generator.AnnotationGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.generator.helpers.TypicalMethods;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalParameters;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.model.SqlType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;
import com.squareup.javapoet.MethodSpec;

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

    private Stream<ResultRowConverter> resultConverters(final List<SqlStatement> sqlStatements) {
        return sqlStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.getType() || SqlType.CALLING == config.getType())
                .map(SqlConfiguration::getResultConverter)
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
