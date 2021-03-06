/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.ConstructorGenerator;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.blocks.GenericBlocks;
import wtf.metio.yosql.models.constants.sql.SqlType;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * JDBC-specific generator for constructors.
 */
public final class JdbcConstructorGenerator implements ConstructorGenerator {

    private final GenericBlocks blocks;
    private final Methods methods;
    private final JdbcConfiguration jdbcNames;
    private final JdbcParameters jdbcParameters;

    public JdbcConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods,
            final JdbcConfiguration jdbcNames,
            final JdbcParameters jdbcParameters) {
        this.blocks = blocks;
        this.methods = methods;
        this.jdbcNames = jdbcNames;
        this.jdbcParameters = jdbcParameters;
    }

    @Override
    public MethodSpec forRepository(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder();
        resultConverters(statements).forEach(converter -> builder.add(blocks.initializeConverter(converter)));
        return methods.constructor()
                .addParameter(jdbcParameters.dataSource())
                .addCode(blocks.initializeFieldToSelf(jdbcNames.dataSource()))
                .addCode(builder.build())
                .build();
    }

    private static Stream<ResultRowConverter> resultConverters(final List<SqlStatement> statements) {
        return statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.type() || SqlType.CALLING == config.type())
                .flatMap(config -> config.resultRowConverter().stream())
                .filter(Objects::nonNull)
                .distinct();
    }

}
