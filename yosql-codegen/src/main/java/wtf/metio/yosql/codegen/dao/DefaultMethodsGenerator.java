/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.MethodSpec;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.blocks.Javadoc;
import wtf.metio.yosql.codegen.lifecycle.ApplicationWarnings;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation of a {@link MethodsGenerator} that delegates most of its work to other interfaces/classes.
 */
public final class DefaultMethodsGenerator implements MethodsGenerator {

    private final Javadoc javadoc;
    private final ConstructorGenerator constructor;
    private final ReadMethodGenerator readMethods;
    private final WriteMethodGenerator writeMethods;
    private final CallMethodGenerator callingMethods;
    private final RepositoriesConfiguration repositories;
    private final LocLogger logger;

    /**
     * @param javadoc        The javadoc generator to use.
     * @param constructor    The constructor generator to use.
     * @param readMethods    The read methods generator to use.
     * @param writeMethods   The write methods generator to use.
     * @param callingMethods The call methods generator to use.
     * @param repositories   The repository configuration to use.
     * @param logger         The logger to use.
     */
    public DefaultMethodsGenerator(
            final Javadoc javadoc,
            final ConstructorGenerator constructor,
            final ReadMethodGenerator readMethods,
            final WriteMethodGenerator writeMethods,
            final CallMethodGenerator callingMethods,
            final RepositoriesConfiguration repositories,
            final LocLogger logger) {
        this.javadoc = javadoc;
        this.constructor = constructor;
        this.readMethods = readMethods;
        this.writeMethods = writeMethods;
        this.callingMethods = callingMethods;
        this.repositories = repositories;
        this.logger = logger;
    }

    @Override
    public Iterable<MethodSpec> asMethodsDeclarations(final List<SqlStatement> statements) {
        final var methods = new ArrayList<MethodSpec>(statements.size());

        methods.addAll(asMethods(statements,
                callingMethods::callMethodDeclaration,
                readMethods::readMethodDeclaration,
                writeMethods::writeMethodDeclaration,
                writeMethods::batchWriteMethodDeclaration));

        return documented(methods);
    }

    /**
     * The connection shapes a statement is generated in.
     *
     * <p>Where a statement gets its connection is a property of the call site rather than of the
     * statement: the same query is read on its own by one caller and inside a transaction another
     * caller already opened by the next. Both shapes are therefore generated as overloads of one
     * name, and {@code createConnection} only decides which one exists when that is turned off.</p>
     */
    private List<Boolean> connectionShapes(final SqlConfiguration configuration) {
        if (repositories.generateConnectionOverloads()) {
            return List.of(Boolean.TRUE, Boolean.FALSE);
        }
        return List.of(configuration.createConnection().orElse(Boolean.TRUE));
    }

    @Override
    public Iterable<MethodSpec> asMethods(final List<SqlStatement> statements) {
        final var methods = new ArrayList<MethodSpec>(statements.size());

        methods.add(constructor.repository(statements));
        if (statements.stream().map(SqlStatement::getConfiguration).anyMatch(InLists::anyExpands)) {
            methods.add(InLists.expansion());
        }
        methods.addAll(asMethods(statements,
                callingMethods::callMethod,
                readMethods::readMethod,
                writeMethods::writeMethod,
                writeMethods::batchWriteMethod));
        warnAboutIgnoredStatements(statements);

        return documented(methods);
    }

    /**
     * Every method a repository publishes is documented here rather than by whichever generator
     * built it, because the tags describe the finished signature — which only exists once the
     * generator is done adding to it.
     */
    private List<MethodSpec> documented(final List<MethodSpec> methods) {
        return methods.stream().map(javadoc::withSignatureTags).toList();
    }

    private List<MethodSpec> asMethods(
            final List<SqlStatement> statements,
            final BiFunction<SqlConfiguration, List<SqlStatement>, MethodSpec> call,
            final BiFunction<SqlConfiguration, List<SqlStatement>, MethodSpec> read,
            final BiFunction<SqlConfiguration, List<SqlStatement>, MethodSpec> write,
            final BiFunction<SqlConfiguration, List<SqlStatement>, MethodSpec> batchWrite) {
        return Stream.of(
                        asMethods(statements, SqlStatement::executeCallOnce, call),
                        asMethods(statements, SqlStatement::executeReadOnce, read),
                        asMethods(statements, SqlStatement::executeWriteOnce, write),
                        asMethods(statements, SqlStatement::executeWriteBatch, batchWrite))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<MethodSpec> asMethods(
            final List<SqlStatement> statements,
            final Predicate<SqlStatement> filter,
            final BiFunction<SqlConfiguration, List<SqlStatement>, MethodSpec> generator) {
        return statements.stream()
                .filter(filter)
                // Into a LinkedHashMap, because this order is the order the methods are declared in
                // and a HashMap's is whatever the hashes happened to be. Generated files are read in
                // diffs and compared byte for byte between builds; methods that move around between
                // JVMs make both useless.
                .collect(Collectors.groupingBy(SqlStatement::getName, LinkedHashMap::new, Collectors.toList()))
                .values()
                .stream()
                .flatMap(statementsWithSameName -> {
                    final var configuration = SqlConfiguration.fromStatements(statementsWithSameName);
                    return connectionShapes(configuration).stream()
                            .map(createConnection -> generator.apply(
                                    SqlConfiguration.copyOf(configuration).withCreateConnection(createConnection),
                                    statementsWithSameName));
                })
                .toList();
    }

    private void warnAboutIgnoredStatements(final List<SqlStatement> statements) {
        statements.stream()
                .filter(Predicate.not(SqlStatement::executeCallOnce)
                        .and(Predicate.not(SqlStatement::executeReadOnce))
                        .and(Predicate.not(SqlStatement::executeWriteOnce))
                        .and(Predicate.not(SqlStatement::executeWriteBatch)))
                .forEach(statement -> logger.warn(ApplicationWarnings.SQL_STATEMENT_IGNORED,
                        statement.getName(), statement.getSourcePath()));
    }

}
