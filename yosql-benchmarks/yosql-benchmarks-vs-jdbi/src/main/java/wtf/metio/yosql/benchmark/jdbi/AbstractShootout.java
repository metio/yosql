/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi;

import com.zaxxer.hikari.HikariDataSource;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import wtf.metio.yosql.benchmark.dao.noop.persistence.SchemaRepository;

import java.time.Instant;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * What both halves of the shootout share: a schema, the rows to read, and the values to write.
 *
 * <p>Each implementation gets a database of its own rather than sharing one, because the write
 * scenarios insert and delete — a shared database would make one implementation's numbers depend on
 * how many times the other had already run. The schema and the seed rows are identical, and are
 * created through generated repositories on both sides: setting up the fixture is not what is being
 * measured, and doing it twice differently would be one more thing to have got wrong.</p>
 */
@State(Scope.Benchmark)
public abstract class AbstractShootout {

    protected static final long NOW = Instant.now().toEpochMilli();
    protected static final int BATCH_SIZE = 10;
    protected static final String[] NAMES_BATCH = IntStream.range(0, BATCH_SIZE)
            .mapToObj(String::valueOf)
            .map("project"::concat)
            .toArray(String[]::new);
    protected static final Long[] TIMESTAMP_BATCH = LongStream.range(0, BATCH_SIZE)
            .mapToObj(index -> NOW + index)
            .toArray(Long[]::new);

    protected HikariDataSource dataSource;

    /**
     * @return the name of this implementation's in-memory database, so that the two do not share one
     */
    protected abstract String databaseName();

    @Setup(Level.Trial)
    public void createSchema() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:" + databaseName() + ";DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");

        final var schema = new SchemaRepository(dataSource);
        schema.createCompaniesTable();
        schema.createProjectsTable();
        schema.createDepartmentsTable();
        schema.createEmployeesTable();
        schema.createVersionAlias();
    }

    @TearDown(Level.Trial)
    public void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

}
