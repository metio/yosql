/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.generic;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.generator.api.*;
import wtf.metio.yosql.generator.blocks.api.GenericBlocks;
import wtf.metio.yosql.generator.blocks.api.Methods;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcParameters;
import wtf.metio.yosql.model.sql.ResultRowConverter;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlStatement;
import wtf.metio.yosql.model.sql.SqlType;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Generic implementation of a {@link MethodsGenerator}. Delegates most of its work to the injected members.
 */
public final class GenericMethodsGenerator extends AbstractMethodsGenerator {

    private final BatchMethodGenerator batchMethods;
    private final Java8StreamMethodGenerator streamMethods;
    private final RxJavaMethodGenerator rxjavaMethods;
    private final StandardMethodGenerator standardMethods;
    private final GenericBlocks blocks;
    private final AnnotationGenerator annotations;
    private final Methods methods;
    private final JdbcNamesConfiguration jdbcNames;
    private final JdbcParameters jdbcParameters;

    /**
     * @param batchMethods    The batch methods generator to use.
     * @param streamMethods   The Java8 stream methods generator to use.
     * @param rxjavaMethods   The RxJava methods generator to use.
     * @param standardMethods The generic methods generator to use.
     * @param annotations     The annotation gnerator to use.
     * @param blocks          The generic code blocks to use.
     * @param methods         The method code blocks to use.
     * @param jdbcNames       The JDBC names to use.
     * @param jdbcParameters  The JDBC parameters to use.
     */
    public GenericMethodsGenerator(
            final BatchMethodGenerator batchMethods,
            final Java8StreamMethodGenerator streamMethods,
            final RxJavaMethodGenerator rxjavaMethods,
            final StandardMethodGenerator standardMethods,
            final GenericBlocks blocks,
            final AnnotationGenerator annotations,
            final Methods methods,
            final JdbcNamesConfiguration jdbcNames,
            final JdbcParameters jdbcParameters) {
        this.batchMethods = batchMethods;
        this.streamMethods = streamMethods;
        this.rxjavaMethods = rxjavaMethods;
        this.standardMethods = standardMethods;
        this.annotations = annotations;
        this.blocks = blocks;
        this.methods = methods;
        this.jdbcParameters = jdbcParameters;
        this.jdbcNames = jdbcNames;
    }

    @Override
    protected MethodSpec constructor(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder();
        resultConverters(statements)
                .forEach(converter -> builder.add(blocks.initializeConverter(converter)));

        return methods.constructor()
                .addAnnotations(annotations.generatedMethod())
                .addParameter(jdbcParameters.dataSource())
                .addCode(blocks.setFieldToSelf(jdbcNames.dataSource()))
                .addCode(builder.build())
                .build();
    }

    private static Stream<ResultRowConverter> resultConverters(final List<SqlStatement> statements) {
        return statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.getType() || SqlType.CALLING == config.getType())
                .map(SqlConfiguration::getResultRowConverter)
                .filter(Objects::nonNull)
                .distinct();
    }

    @Override
    protected MethodSpec standardReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return standardMethods.standardReadMethod(configuration, statements);
    }

    @Override
    protected MethodSpec standardWriteMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return standardMethods.standardWriteMethod(configuration, statements);
    }

    @Override
    protected MethodSpec standardCallMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return standardMethods.standardCallMethod(configuration, statements);
    }

    @Override
    protected MethodSpec batchWriteMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return batchMethods.batchWriteMethod(configuration, statements);
    }

    @Override
    protected MethodSpec streamEagerReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return streamMethods.streamEagerMethod(configuration, statements);
    }

    @Override
    protected MethodSpec streamLazyReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return streamMethods.streamLazyMethod(configuration, statements);
    }

    @Override
    protected MethodSpec rxJavaReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return rxjavaMethods.rxJava2ReadMethod(configuration, statements);
    }

}
