package de.xn__ho_hia.yosql.generator.dao;

import java.io.PrintStream;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.dagger.Delegating;
import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.dao.jdbc.DaoJdbcModule;
import de.xn__ho_hia.yosql.generator.dao.jdbc.JDBC;

/**
 * Dagger2 module for the DAO API.
 */
@Module(includes = DaoJdbcModule.class)
@SuppressWarnings("static-method")
public class DaoModule {

    @Delegating
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @JDBC RepositoryGenerator repositoryGenerator,
            final PrintStream out) {
        return new DelegatingRepositoryGenerator(repositoryGenerator, out);
    }

}
