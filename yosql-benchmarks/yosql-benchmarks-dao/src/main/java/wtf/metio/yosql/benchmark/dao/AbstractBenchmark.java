/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.openjdk.jmh.annotations.*;
import wtf.metio.yosql.benchmark.dao.noop.persistence.SchemaRepository;

import java.time.Instant;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Encapsulates common benchmark functionality.
 */
@State(Scope.Benchmark)
public abstract class AbstractBenchmark {

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
    protected SchemaRepository schemaRepository;

    @Setup(Level.Trial)
    public void setup() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:benchmarks-jdbc;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        schemaRepository = new SchemaRepository(dataSource);
        schemaRepository.createCompaniesTable();
        schemaRepository.createProjectsTable();
        schemaRepository.createDepartmentsTable();
        schemaRepository.createEmployeesTable();
        schemaRepository.createProjectEmployeesTable();
        schemaRepository.createVersionAlias();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
        schemaRepository = null;
    }

}
