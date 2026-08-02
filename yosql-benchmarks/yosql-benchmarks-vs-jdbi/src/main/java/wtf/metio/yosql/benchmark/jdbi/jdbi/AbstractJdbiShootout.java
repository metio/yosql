/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi.jdbi;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import wtf.metio.yosql.benchmark.dao.noop.persistence.CompanyRepository;
import wtf.metio.yosql.benchmark.dao.noop.persistence.DepartmentRepository;
import wtf.metio.yosql.benchmark.dao.noop.persistence.EmployeeRepository;
import wtf.metio.yosql.benchmark.dao.noop.persistence.ProjectRepository;
import wtf.metio.yosql.benchmark.jdbi.AbstractShootout;

/**
 * The JDBI half.
 *
 * <p>The seed rows are inserted through the generated repositories, the same ones the other half
 * uses. Setting up the fixture is not measured, and doing it twice in two libraries would be one
 * more way for the two databases to end up holding different data.</p>
 */
abstract class AbstractJdbiShootout extends AbstractShootout {

    protected JdbiDao dao;

    @Override
    protected String databaseName() {
        return "shootout-jdbi";
    }

    @Setup(Level.Trial)
    public void seed() {
        dao = new JdbiDao(dataSource);

        final var companies = new CompanyRepository(dataSource);
        companies.insertCompany("metio.wtf", "JDBI");
        companies.insertCompany("other", "tests");
        new DepartmentRepository(dataSource).insertDepartment(1L, "benchmarks");
        new ProjectRepository(dataSource).insertProject("JDBI", NOW);
        new EmployeeRepository(dataSource).insertEmployee(1L, "bob", "example", "bob@example.com", 100L);
    }

}
