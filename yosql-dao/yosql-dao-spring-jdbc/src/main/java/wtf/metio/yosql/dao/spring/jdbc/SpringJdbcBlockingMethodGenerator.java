/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.spring.jdbc;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.api.BlockingMethodGenerator;
import wtf.metio.yosql.codegen.api.ControlFlows;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.function.BiFunction;

public final class SpringJdbcBlockingMethodGenerator implements BlockingMethodGenerator {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final SpringJdbcBlocks blocks;

    public SpringJdbcBlockingMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final SpringJdbcBlocks blocks) {
        this.controlFlows = controlFlows;
        this.methods = methods;
        this.parameters = parameters;
        this.logging = logging;
        this.blocks = blocks;
    }

    @Override
    public MethodSpec blockingReadMethod(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var converter = configuration.resultRowConverter().orElseThrow();
        // TODO: catch resultType is blank && throw CodegenException?
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        return switch (configuration.returningMode()) {
            // TODO: support NONE?
            case ONE -> read(configuration, statements, resultType, blocks::returnAsOne);
            case FIRST -> read(configuration, statements, resultType, blocks::returnAsFirst);
            default -> read(configuration, statements, TypicalTypes.listOf(resultType), blocks::returnAsList);
        };
    }

    private <T extends TypeName> MethodSpec read(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements,
            final T resultType,
            final BiFunction<T, String, CodeBlock> returner) {
        final var converter = configuration.resultRowConverter().orElseThrow();
        return methods.blockingMethod(configuration.blockingName(), statements)
                .returns(resultType)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addCode(logging.entering(configuration.repository(), configuration.blockingName()))
                .addCode(returner.apply(resultType, converter.alias()))
                .build();
    }

    @Override
    public MethodSpec blockingWriteMethod(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        return methods.blockingMethod(configuration.blockingName(), statements).build();
    }

    @Override
    public MethodSpec blockingCallMethod(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        return methods.blockingMethod(configuration.blockingName(), statements).build();
    }

}
