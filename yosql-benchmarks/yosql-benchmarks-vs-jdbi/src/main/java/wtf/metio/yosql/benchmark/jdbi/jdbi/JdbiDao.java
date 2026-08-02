/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi.jdbi;

import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The same eleven statements as the generated repositories, written with JDBI.
 *
 * <p>The SQL is copied from the DAO benchmark's `.sql` files, named parameters and all, so the two
 * implementations send the database the same text. What differs is only how each gets from a
 * {@code DataSource} to a {@code List<Map<String, Object>>}.</p>
 *
 * <p>Both sides take a connection per call and give it back — {@code withHandle} here, a
 * try-with-resources around {@code dataSource.getConnection()} in the generated code — so neither is
 * measured holding a connection the other had to acquire. Rows become maps on both sides:
 * {@code mapToMap} here, the generated ToMap converter there.</p>
 */
final class JdbiDao {

    private final Jdbi jdbi;

    JdbiDao(final javax.sql.DataSource dataSource) {
        jdbi = Jdbi.create(dataSource);
    }

    Optional<Map<String, Object>> findCompany(final long pid) {
        return jdbi.withHandle(handle -> handle
                .createQuery("SELECT * FROM companies WHERE pid = :pid")
                .bind("pid", pid)
                .mapToMap()
                .findFirst());
    }

    List<Map<String, Object>> findCompanies() {
        return jdbi.withHandle(handle -> handle
                .createQuery("SELECT * FROM companies")
                .mapToMap()
                .list());
    }

    List<Map<String, Object>> findCompanyByDepartment(final long department) {
        return jdbi.withHandle(handle -> handle
                .createQuery("""
                        SELECT c.*
                        FROM companies c
                                 INNER JOIN departments d
                                            ON c.pid = d.company_pid
                                                AND d.pid = :department""")
                .bind("department", department)
                .mapToMap()
                .list());
    }

    List<Map<String, Object>> findDepartmentsByCompany(final long company) {
        return jdbi.withHandle(handle -> handle
                .createQuery("SELECT * FROM departments WHERE company_pid = :company")
                .bind("company", company)
                .mapToMap()
                .list());
    }

    List<Map<String, Object>> findEmployeesWithMinSalary(final long salary) {
        return jdbi.withHandle(handle -> handle
                .createQuery("SELECT * FROM employees WHERE salary >= :salary")
                .bind("salary", salary)
                .mapToMap()
                .list());
    }

    List<Map<String, Object>> getVersion() {
        return jdbi.withHandle(handle -> handle
                .createQuery("CALL getVersion()")
                .mapToMap()
                .list());
    }

    int insertProject(final String name, final Long date) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("INSERT INTO projects (NAME, DATESTARTED) VALUES (:name, :date)")
                .bind("name", name)
                .bind("date", date)
                .execute());
    }

    int[] insertProjectBatch(final String[] names, final Long[] dates) {
        return jdbi.withHandle(handle -> {
            final var batch = handle
                    .prepareBatch("INSERT INTO projects (NAME, DATESTARTED) VALUES (:name, :date)");
            for (var index = 0; index < names.length; index++) {
                batch.bind("name", names[index]).bind("date", dates[index]).add();
            }
            return batch.execute();
        });
    }

    int updateDepartmentsOfCompany(final Long company, final Long oldCompany, final String name) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("""
                        UPDATE departments
                        SET company_pid = :company,
                            name        = :name
                        WHERE company_pid = :oldCompany""")
                .bind("company", company)
                .bind("name", name)
                .bind("oldCompany", oldCompany)
                .execute());
    }

    int updateEmployee(
            final long pid,
            final long department,
            final String name,
            final String surname,
            final String email,
            final long salary) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("""
                        UPDATE employees
                        SET department_pid = :department,
                            name           = :name,
                            surname        = :surname,
                            email          = :email,
                            salary         = :salary
                        WHERE pid = :pid""")
                .bind("pid", pid)
                .bind("department", department)
                .bind("name", name)
                .bind("surname", surname)
                .bind("email", email)
                .bind("salary", salary)
                .execute());
    }

    int deleteEmployee(final long employee) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("DELETE FROM employees WHERE pid = :employee")
                .bind("employee", employee)
                .execute());
    }

}
