package wtf.metio.yosql.generator;

import wtf.metio.yosql.generator.api.CodeGenerator;
import wtf.metio.yosql.generator.api.RepositoryGenerator;
import wtf.metio.yosql.generator.api.UtilitiesGenerator;
import wtf.metio.yosql.model.sql.PackageTypeSpec;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

// TODO: move class to 'api' package?
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
    public Stream<PackageTypeSpec> generateCode(final List<SqlStatement> statements) {
        return Stream.concat(generateRepositories(statements), utilitiesGenerator.generateUtilities(statements));
    }

    private Stream<PackageTypeSpec> generateRepositories(final List<SqlStatement> statements) {
        return statements.parallelStream()
                .collect(groupingBy(SqlStatement::getRepository))
                .entrySet()
                .parallelStream()
                .map(repository -> repositoryGenerator.generateRepository(
                        repository.getKey(), repository.getValue()));
    }

}
