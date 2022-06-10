/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DefaultCodeGenerator implements CodeGenerator {

    private final RepositoryGenerator repositoryGenerator;
    private final ConverterGenerator converterGenerator;
    private final RepositoriesConfiguration repositories;

    public DefaultCodeGenerator(
            final RepositoryGenerator repositoryGenerator,
            final ConverterGenerator converterGenerator,
            final RepositoriesConfiguration repositories) {
        this.repositoryGenerator = repositoryGenerator;
        this.converterGenerator = converterGenerator;
        this.repositories = repositories;
    }

    @Override
    public Stream<PackagedTypeSpec> generateCode(final List<SqlStatement> statements) {
        return Stream.concat(generateRepositories(statements), converterGenerator.generateConverterClasses(statements));
    }

    private Stream<PackagedTypeSpec> generateRepositories(final List<SqlStatement> statements) {
        final var classes = generateRepositoryClasses(statements);
        final var interfaces = repositories.generateInterfaces()
                ? generateRepositoryInterfaces(statements)
                : Stream.<PackagedTypeSpec>empty();
        return Stream.concat(classes, interfaces);
    }

    private Stream<PackagedTypeSpec> generateRepositoryClasses(final List<SqlStatement> statements) {
        return generate(statements, Collectors.groupingBy(SqlStatement::getRepositoryClass), repositoryGenerator::generateRepositoryClass);
    }

    private Stream<PackagedTypeSpec> generateRepositoryInterfaces(final List<SqlStatement> statements) {
        return generate(statements, Collectors.groupingBy(SqlStatement::getRepositoryInterface), repositoryGenerator::generateRepositoryInterface);
    }

    private static Stream<PackagedTypeSpec> generate(
            final List<SqlStatement> statements,
            final Collector<SqlStatement, ?, Map<String, List<SqlStatement>>> collector,
            final BiFunction<String, List<SqlStatement>, PackagedTypeSpec> generator) {
        return statements.parallelStream()
                .collect(collector)
                .entrySet()
                .parallelStream()
                .map(repository -> generator.apply(repository.getKey(), repository.getValue()));
    }

}
