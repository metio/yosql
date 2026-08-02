/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Where a repository's connections come from.
 *
 * <p>The field and the constructor parameter holding a {@code DataSource} are written by two
 * different generators, and both have to agree about whether there is one at all — a field nothing
 * assigns, or a parameter assigned to no field, is code that does not compile.</p>
 */
final class RepositoryConnections {

    private RepositoryConnections() {
        // utility class, call #needsDataSource() directly
    }

    /**
     * Whether the repository has a method that opens its own connection.
     *
     * <p>With overloads generated, every statement has one however it is configured, so the
     * repository always takes a {@code DataSource}. Without them, a repository whose statements all
     * take a connection from the caller has nothing to do with one and is not given one.</p>
     */
    static boolean needsDataSource(
            final RepositoriesConfiguration repositories,
            final List<SqlStatement> statements) {
        return repositories.generateConnectionOverloads() || statements.stream()
                .map(SqlStatement::getConfiguration)
                .flatMap(configuration -> configuration.createConnection().stream())
                .anyMatch(Boolean.TRUE::equals);
    }

}
