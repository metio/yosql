/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.example.app;

import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.sql.DataSource;

import com.example.persistence.CompanyRepository;
import com.example.persistence.PersonRepository;
import com.example.persistence.SchemaRepository;
import com.example.persistence.util.ResultRow;
import com.zaxxer.hikari.HikariDataSource;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

public class ExampleApp {

    private static final PrintStream   OUT     = System.out;
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static void main(final String[] arguments) throws Exception {
        if (match(arguments, "h2")) {
            final HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:h2:mem:example;DB_CLOSE_DELAY=-1");
            dataSource.setUsername("sa");
            runTests(arguments, dataSource);
        } else if (match(arguments, "psql")) {
            // start psql first with 'docker-compose up -d postgres'
            final HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:postgresql://localhost:50000/example");
            dataSource.setUsername("example");
            dataSource.setPassword("example");
            runTests(arguments, dataSource);
            // stop psql with 'docker-compose stop postgres'
        } else if (match(arguments, "mysql")) {
            // start mysql first with 'docker-compose up -d mysql'
            final HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:mysql://localhost:51000/example?useSSL=false");
            dataSource.setUsername("example");
            dataSource.setPassword("example");
            runTests(arguments, dataSource);
            // stop mysql with 'docker-compose stop mysql'
        }
    }

    private static void runTests(final String[] arguments, final DataSource dataSource) {
        final SchemaRepository schemaRepository = new SchemaRepository(dataSource);
        final CompanyRepository companyRepository = new CompanyRepository(dataSource);
        final PersonRepository personRepository = new PersonRepository(dataSource);

        if (match(arguments, "standard", "stream", "rxjava")) {
            initializeDatabase(schemaRepository, companyRepository, personRepository);
        }

        if (match(arguments, "standard")) {
            standardTests(companyRepository, personRepository);
        }
        if (match(arguments, "stream")) {
            streamTests(companyRepository, personRepository);
        }
        if (match(arguments, "rxjava")) {
            rxJavaTests(companyRepository, personRepository);
        }
    }

    private static boolean match(final String[] arguments, final String... values) {
        return Arrays.stream(arguments).anyMatch(argument -> Arrays.asList(values).contains(argument));
    }

    private static void initializeDatabase(
            final SchemaRepository schemaRepository,
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        createSchema(schemaRepository);
        writeCompanies(companyRepository);
    }

    private static void createSchema(final SchemaRepository schemaRepository) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 30, 70, 20 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow("Create Schema", "", "");
        at.addRule();
        at.addRow("Id", "Name", "Time (ms)");
        at.addRule();
        final Duration dropCompaniesTable = time(schemaRepository::dropCompaniesTable);
        at.addRow(COUNTER.incrementAndGet(), "dropCompaniesTable", dropCompaniesTable.toMillis());
        at.addRule();
        final Duration dropPersonsTable = time(schemaRepository::dropPersonsTable);
        at.addRow(COUNTER.incrementAndGet(), "dropPersonsTable", dropPersonsTable.toMillis());
        at.addRule();
        final Duration createCompaniesTable = time(schemaRepository::createCompaniesTable);
        at.addRow(COUNTER.incrementAndGet(), "createCompaniesTable", createCompaniesTable.toMillis());
        at.addRule();
        final Duration createPersonsTable = time(schemaRepository::createPersonsTable);
        at.addRow(COUNTER.incrementAndGet(), "createPersonsTable", createPersonsTable.toMillis());
        at.addRule();
        at.addRule();

        OUT.println(at.render());
    }

    private static void writeCompanies(final CompanyRepository companyRepository) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 30, 70, 20 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow("Write Companies", "", "");
        at.addRule();
        at.addRow("Id", "Name", "Time (ms)");
        at.addRule();
        final Duration insertCompany1 = time(() -> companyRepository.insertCompany(123, "test"));
        at.addRow(COUNTER.incrementAndGet(), "insertCompany1", insertCompany1.toMillis());
        at.addRule();
        final Duration insertCompanyBatch1 = time(
                () -> companyRepository.insertCompanyBatch(new int[] { 456, 789 }, new String[] { "two", "three" }));
        at.addRow(COUNTER.incrementAndGet(), "insertCompanyBatch1", insertCompanyBatch1.toMillis());
        at.addRule();
        final Duration insertCompanyBatch2 = time(
                () -> companyRepository.insertCompanyBatch(new int[] { 91, 32 }, new String[] { "more", "other" }));
        at.addRow(COUNTER.incrementAndGet(), "insertCompanyBatch2", insertCompanyBatch2.toMillis());
        at.addRule();
        final Duration insertCompany2 = time(() -> companyRepository.insertCompany(47, "test"));
        at.addRow(COUNTER.incrementAndGet(), "insertCompany2", insertCompany2.toMillis());
        at.addRule();
        final Duration insertCompanyBatch3 = time(
                () -> companyRepository.insertCompanyBatch(new int[] { 56, 78 }, new String[] { "abc", "def" }));
        at.addRow(COUNTER.incrementAndGet(), "insertCompanyBatch3", insertCompanyBatch3.toMillis());
        at.addRule();
        final Duration insertCompanyBatch4 = time(
                () -> companyRepository.insertCompanyBatch(new int[] { 612, 768 }, new String[] { "123", "234" }));
        at.addRow(COUNTER.incrementAndGet(), "insertCompanyBatch4", insertCompanyBatch4.toMillis());
        at.addRule();
        at.addRule();

        OUT.println(at.render());
    }

    private static void standardTests(
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 30, 70, 20 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow("Standard Tests", "", "");
        at.addRule();
        at.addRow("Id", "Name", "Time (ms)");
        at.addRule();
        final Duration queryAllCompanies = time("queryAllCompanies", () -> companyRepository.queryAllCompanies());
        at.addRow(COUNTER.incrementAndGet(), "queryAllCompanies", queryAllCompanies.toMillis());
        at.addRule();
        final Duration findCompanyByName = time("findCompanyByName", () -> companyRepository.findCompanyByName("test"));
        at.addRow(COUNTER.incrementAndGet(), "findCompanyByName", findCompanyByName.toMillis());
        at.addRule();
        final Duration findCompanies = time("findCompanies", () -> companyRepository.findCompanies(30, 95));
        at.addRow(COUNTER.incrementAndGet(), "findCompanies", findCompanies.toMillis());
        at.addRule();
        final Duration findPerson = time("findPerson", () -> personRepository.findPerson("test"));
        at.addRow(COUNTER.incrementAndGet(), "findPerson", findPerson.toMillis());
        at.addRule();
        at.addRule();

        OUT.println(at.render());
    }

    private static void streamTests(
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 30, 70, 20 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow("Stream Tests", "", "");
        at.addRule();
        at.addRow("Id", "Name", "Time (ms)");
        at.addRule();
        final Duration queryAllCompaniesStreamEager = timeLazyStream("queryAllCompaniesStreamEager",
                companyRepository::queryAllCompaniesStreamEager);
        at.addRow(COUNTER.incrementAndGet(), "queryAllCompaniesStreamEager", queryAllCompaniesStreamEager.toMillis());
        at.addRule();
        final Duration queryAllCompaniesStreamLazy = timeLazyStream("queryAllCompaniesStreamLazy",
                companyRepository::queryAllCompaniesStreamLazy);
        at.addRow(COUNTER.incrementAndGet(), "queryAllCompaniesStreamLazy", queryAllCompaniesStreamLazy.toMillis());
        at.addRule();
        final Duration findCompaniesStreamLazy = timeLazyStream("findCompaniesStreamLazy",
                () -> companyRepository.findCompaniesStreamLazy(112, 999));
        at.addRow(COUNTER.incrementAndGet(), "findCompaniesStreamLazy", findCompaniesStreamLazy.toMillis());
        at.addRule();
        final Duration findPersonStreamLazy = timeLazyStream("findPersonStreamLazy",
                () -> personRepository.findPersonStreamLazy("test"));
        at.addRow(COUNTER.incrementAndGet(), "findPersonStreamLazy", findPersonStreamLazy.toMillis());
        at.addRule();
        at.addRule();

        OUT.println(at.render());
    }

    private static void rxJavaTests(
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 30, 70, 20 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow("RxJava Tests", "", "");
        at.addRule();
        at.addRow("Id", "Name", "Time (ms)");
        at.addRule();
        final Duration queryAllCompaniesFlow = timeLazy("queryAllCompaniesFlow",
                companyRepository::queryAllCompaniesFlow);
        at.addRow(COUNTER.incrementAndGet(), "queryAllCompaniesFlow", queryAllCompaniesFlow.toMillis());
        at.addRule();
        final Duration findCompaniesFlow = timeLazy("findCompaniesFlow",
                () -> companyRepository.findCompaniesFlow(20, 120));
        at.addRow(COUNTER.incrementAndGet(), "findCompaniesFlow", findCompaniesFlow.toMillis());
        at.addRule();
        final Duration findCompanyByNameFlow = timeLazy("findCompanyByNameFlow",
                () -> companyRepository.findCompanyByNameFlow("test"));
        at.addRow(COUNTER.incrementAndGet(), "findCompanyByNameFlow", findCompanyByNameFlow.toMillis());
        at.addRule();
        final Duration findPersonFlow = timeLazy("findPersonFlow", () -> personRepository.findPersonFlow("test"));
        at.addRow(COUNTER.incrementAndGet(), "findPersonFlow", findPersonFlow.toMillis());
        at.addRule();
        at.addRule();
        OUT.println(at.render());
    }

    private static Duration time(final Runnable run) {
        final Instant start = Instant.now();
        run.run();
        final Instant stop = Instant.now();
        return Duration.between(start, stop);
    }

    private static Duration time(final String name, final Supplier<List<ResultRow>> results) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 80 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow(name);
        at.addRule();

        final Instant start = Instant.now();
        results.get().forEach(result -> {
            at.addRow(result);
            at.addRule();
        });
        final Instant stop = Instant.now();
        OUT.println(at.render());
        return Duration.between(start, stop);
    }

    private static Duration timeLazyStream(final String name, final Supplier<Stream<ResultRow>> results) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 80 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow(name);
        at.addRule();

        final Instant start = Instant.now();
        results.get().forEach(result -> {
            at.addRow(result);
            at.addRule();
        });
        final Instant stop = Instant.now();
        OUT.println(at.render());
        return Duration.between(start, stop);
    }

    private static Duration timeLazy(final String name, final Supplier<Flowable<ResultRow>> results) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 80 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow(name);
        at.addRule();

        final Instant start = Instant.now();
        final Disposable disposable = results.get().forEach(result -> {
            at.addRow(result);
            at.addRule();
        });
        disposable.dispose();
        final Instant stop = Instant.now();
        OUT.println(at.render());
        return Duration.between(start, stop);
    }

}
