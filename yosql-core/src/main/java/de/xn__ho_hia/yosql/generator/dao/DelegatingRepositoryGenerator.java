package de.xn__ho_hia.yosql.generator.dao;

import java.util.List;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.dagger.Delegating;
import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.dao.jdbc.JDBC;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;
import de.xn__ho_hia.yosql.model.SqlStatement;

/**
 * Delegates its work to the configured repository generator.
 */
@Delegating
final class DelegatingRepositoryGenerator implements RepositoryGenerator {

    private final RepositoryGenerator jdbcRepositoryGenerator;

    @Inject
    DelegatingRepositoryGenerator(final @JDBC RepositoryGenerator jdbcRepositoryGenerator) {
        this.jdbcRepositoryGenerator = jdbcRepositoryGenerator;
    }

    @Override
    public PackageTypeSpec generateRepository(final String repositoryName, final List<SqlStatement> statements) {
        final PackageTypeSpec repository = jdbcRepositoryGenerator.generateRepository(repositoryName, statements);
        return repository;
    }

}
