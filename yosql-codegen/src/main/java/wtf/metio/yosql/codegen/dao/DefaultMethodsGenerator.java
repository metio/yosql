/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Default implementation of a {@link MethodsGenerator} that delegates most of its work to other interfaces/classes.
 */
public final class DefaultMethodsGenerator implements MethodsGenerator {

    private final ConstructorGenerator constructor;
    private final ReadMethodGenerator readMethods;
    private final WriteMethodGenerator writeMethods;
    private final CallMethodGenerator callingMethods;

    /**
     * @param constructor    The constructor generator to use.
     * @param readMethods    The read methods generator to use.
     * @param writeMethods   The write methods generator to use.
     * @param callingMethods The call methods generator to use.
     */
    public DefaultMethodsGenerator(
            final ConstructorGenerator constructor,
            final ReadMethodGenerator readMethods,
            final WriteMethodGenerator writeMethods,
            final CallMethodGenerator callingMethods) {
        this.constructor = constructor;
        this.readMethods = readMethods;
        this.writeMethods = writeMethods;
        this.callingMethods = callingMethods;
    }

    @Override
    public Iterable<MethodSpec> asMethodsDeclarations(final List<SqlStatement> statements) {
        final var methods = new ArrayList<MethodSpec>(statements.size());

        methods.addAll(asMethods(statements, SqlStatement::generateStandardReadAPI,
                readMethods::readMethodDeclaration));
        methods.addAll(asMethods(statements, SqlStatement::generateStandardCallAPI,
                callingMethods::callMethodDeclaration));
        methods.addAll(asMethods(statements, SqlStatement::generateStandardWriteAPI,
                writeMethods::writeMethodDeclaration));
        methods.addAll(asMethods(statements, SqlStatement::generateBatchWriteAPI,
                writeMethods::batchWriteMethodDeclaration));

        return methods;
    }

    @Override
    public Iterable<MethodSpec> asMethods(final List<SqlStatement> statements) {
        final var methods = new ArrayList<MethodSpec>(statements.size());

        methods.add(constructor.repository(statements));

        methods.addAll(asMethods(statements, SqlStatement::generateStandardReadAPI, readMethods::readMethod));
        methods.addAll(asMethods(statements, SqlStatement::generateStandardCallAPI, callingMethods::callMethod));
        methods.addAll(asMethods(statements, SqlStatement::generateStandardWriteAPI, writeMethods::writeMethod));
        methods.addAll(asMethods(statements, SqlStatement::generateBatchWriteAPI, writeMethods::batchWriteMethod));

        return methods;
    }

    private static List<MethodSpec> asMethods(
            final List<SqlStatement> statements,
            final Predicate<SqlStatement> filter,
            final BiFunction<SqlConfiguration, List<SqlStatement>, MethodSpec> generator) {
        return statements.stream()
                .filter(filter)
                .collect(Collectors.groupingBy(SqlStatement::getName))
                .values()
                .stream()
                .map(statementsInRepository -> generator.apply(
                        SqlConfiguration.fromStatements(statementsInRepository), statementsInRepository))
                .filter(Objects::nonNull)
                .toList();
    }

}
