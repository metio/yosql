/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator;

import wtf.metio.yosql.generator.api.CodeGenerator;
import wtf.metio.yosql.generator.api.RepositoryGenerator;
import wtf.metio.yosql.generator.api.UtilitiesGenerator;
import wtf.metio.yosql.model.sql.PackagedTypeSpec;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

final class DefaultCodeGenerator implements CodeGenerator {

    private final RepositoryGenerator repositoryGenerator;
    private final UtilitiesGenerator utilitiesGenerator;

    DefaultCodeGenerator(
            final RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilitiesGenerator) {
        this.repositoryGenerator = repositoryGenerator;
        this.utilitiesGenerator = utilitiesGenerator;
    }

    @Override
    public Stream<PackagedTypeSpec> generateCode(final List<SqlStatement> statements) {
        return Stream.concat(generateRepositories(statements), utilitiesGenerator.generateUtilities(statements));
    }

    private Stream<PackagedTypeSpec> generateRepositories(final List<SqlStatement> statements) {
        return statements.parallelStream()
                .collect(groupingBy(SqlStatement::getRepository))
                .entrySet()
                .parallelStream()
                .map(repository -> repositoryGenerator.generateRepository(
                        repository.getKey(), repository.getValue()));
    }

}
