/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.blocks.CodeBlocks;
import wtf.metio.yosql.codegen.blocks.Methods;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

/**
 * Default implementation of the {@link ConstructorGenerator} interface.
 */
public final class DefaultConstructorGenerator implements ConstructorGenerator {

    private final CodeBlocks blocks;
    private final Methods methods;
    private final NamesConfiguration names;
    private final JdbcParameters jdbcParameters;
    private final RepositoriesConfiguration repositories;
    private final ConverterConfiguration converters;

    public DefaultConstructorGenerator(
            final CodeBlocks blocks,
            final Methods methods,
            final NamesConfiguration names,
            final JdbcParameters jdbcParameters,
            final RepositoriesConfiguration repositories,
            final ConverterConfiguration converters) {
        this.blocks = blocks;
        this.methods = methods;
        this.names = names;
        this.jdbcParameters = jdbcParameters;
        this.repositories = repositories;
        this.converters = converters;
    }

    @Override
    public MethodSpec repository(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder();
        if (repositories.injectConverters()) {
            final var constructor = methods.constructor().addParameter(jdbcParameters.dataSource());
            resultConverters(statements).forEach(converter -> {
                constructor.addParameter(jdbcParameters.converter(converter));
                builder.add(blocks.initializeFieldToSelf(converter.alias().orElseThrow())); // TODO: throw business exception
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

    private Stream<ResultRowConverter> resultConverters(final List<SqlStatement> statements) {
        return SqlStatement.resultConverters(statements, converters.defaultConverter().orElseThrow());
    }

}
