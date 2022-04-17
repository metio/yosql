/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
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
    private final NamesConfiguration names;
    private final JdbcParameters jdbcParameters;
    private final RepositoriesConfiguration repositories;

    public JdbcConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods,
            final NamesConfiguration names,
            final JdbcParameters jdbcParameters, 
            final RepositoriesConfiguration repositories) {
        this.blocks = blocks;
        this.methods = methods;
        this.names = names;
        this.jdbcParameters = jdbcParameters;
        this.repositories = repositories;
    }

    @Override
    public MethodSpec forRepository(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder();
        if (repositories.injectConverters()) {
            final var constructor = methods.constructor().addParameter(jdbcParameters.dataSource());
            resultConverters(statements).forEach(converter -> {
                constructor.addParameter(jdbcParameters.converter(converter));
                builder.add(blocks.initializeFieldToSelf(converter.alias()));
            });
            return constructor
                    .addCode(blocks.initializeFieldToSelf(names.dataSource()))
                    .addCode(builder.build())
                    .build();
        }
        resultConverters(statements).forEach(converter -> builder.add(blocks.initializeConverter(converter)));
        return methods.constructor()
                .addParameter(jdbcParameters.dataSource())
                .addCode(blocks.initializeFieldToSelf(names.dataSource()))
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
