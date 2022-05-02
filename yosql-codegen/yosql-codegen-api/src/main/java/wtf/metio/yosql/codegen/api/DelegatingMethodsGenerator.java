/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.api;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Delegating implementation of a {@link MethodsGenerator}.
 */
public final class DelegatingMethodsGenerator implements MethodsGenerator {

    private final ConstructorGenerator constructor;
    private final BlockingMethodGenerator blockingMethods;
    private final BatchMethodGenerator batchMethods;
    private final Java8StreamMethodGenerator streamMethods;
    private final RxJavaMethodGenerator rxjavaMethods;
    private final ReactorMethodGenerator reactorMethods;
    private final MutinyMethodGenerator mutinyMethods;

    /**
     * @param constructor     The constructor generator to use.
     * @param blockingMethods The blocking methods generator to use.
     * @param batchMethods    The batch methods generator to use.
     * @param streamMethods   The Java8 stream methods generator to use.
     * @param rxjavaMethods   The RxJava methods generator to use.
     * @param reactorMethods  The Reactor methods generator to use.
     * @param mutinyMethods   The Mutiny methods generator to use.
     */
    public DelegatingMethodsGenerator(
            final ConstructorGenerator constructor,
            final BlockingMethodGenerator blockingMethods,
            final BatchMethodGenerator batchMethods,
            final Java8StreamMethodGenerator streamMethods,
            final RxJavaMethodGenerator rxjavaMethods,
            final ReactorMethodGenerator reactorMethods,
            final MutinyMethodGenerator mutinyMethods) {
        this.constructor = constructor;
        this.blockingMethods = blockingMethods;
        this.batchMethods = batchMethods;
        this.streamMethods = streamMethods;
        this.rxjavaMethods = rxjavaMethods;
        this.reactorMethods = reactorMethods;
        this.mutinyMethods = mutinyMethods;
    }

    @Override
    public Iterable<MethodSpec> asMethods(final List<SqlStatement> statements) {
        final var methods = new ArrayList<MethodSpec>(statements.size());

        methods.add(constructor.forRepository(statements));

        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateBatchWriteAPI, batchMethods::batchWriteMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateBlockingCallAPI, blockingMethods::blockingCallMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateBlockingReadAPI, blockingMethods::blockingReadMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateBlockingWriteAPI, blockingMethods::blockingWriteMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateMutinyCallAPI, mutinyMethods::mutinyCallMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateMutinyReadAPI, mutinyMethods::mutinyReadMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateMutinyWriteAPI, mutinyMethods::mutinyWriteMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateRxJavaCallAPI, rxjavaMethods::rxJavaCallMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateRxJavaReadAPI, rxjavaMethods::rxJavaReadMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateRxJavaWriteAPI, rxjavaMethods::rxJavaWriteMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateReactorCallAPI, reactorMethods::reactorCallMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateReactorReadAPI, reactorMethods::reactorReadMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateReactorWriteAPI, reactorMethods::reactorWriteMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateStreamEagerReadAPI, streamMethods::streamEagerReadMethod));
        methods.addAll(asMethods(statements, SqlStatement::shouldGenerateStreamLazyReadAPI, streamMethods::streamLazyReadMethod));

        return methods;
    }

    private static List<MethodSpec> asMethods(
            final List<SqlStatement> statements,
            final Predicate<SqlStatement> filter,
            final BiFunction<SqlConfiguration, List<SqlStatement>, MethodSpec> generator) {
        return statements.stream()
                .filter(filter)
                .collect(SqlStatement.groupByName())
                .values()
                .stream()
                .map(statementsInRepository -> generator.apply(
                        SqlConfiguration.fromStatements(statementsInRepository), statementsInRepository))
                .filter(Objects::nonNull)
                .toList();
    }

}