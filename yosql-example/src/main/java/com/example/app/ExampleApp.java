package com.example.app;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGPoolingDataSource;

import com.example.persistence.CompanyRepository;
import com.example.persistence.PersonRepository;
import com.example.persistence.SchemaRepository;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import io.reactivex.Flowable;

public class ExampleApp {

    public static final void main(final String[] arguments) throws Exception {
        if (match(arguments, "h2")) {
            final JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:mem:example;DB_CLOSE_DELAY=-1");
            runTests(arguments, dataSource);
        } else if (match(arguments, "psql")) {
            // start psql first with 'docker-compose up -d postgres'
            final PGPoolingDataSource dataSource = new PGPoolingDataSource();
            dataSource.setDataSourceName("test");
            dataSource.setServerName("localhost");
            dataSource.setPortNumber(50000);
            dataSource.setDatabaseName("example");
            dataSource.setUser("example");
            dataSource.setPassword("example");
            dataSource.setMaxConnections(1);
            runTests(arguments, dataSource);
            // stop psql with 'docker-compose stop postgres'
        } else if (match(arguments, "mysql")) {
            // start mysql first with 'docker-compose up -d mysql'
            final MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setURL("jdbc:mysql://localhost:51000/example");
            dataSource.setUser("example");
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
        return Arrays.stream(arguments).anyMatch(argument -> Arrays.stream(values)
                .anyMatch(argument::equals));
    }

    private static void initializeDatabase(
            final SchemaRepository schemaRepository,
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        createSchema(schemaRepository);
        writeCompanies(companyRepository);
    }

    private static void createSchema(final SchemaRepository schemaRepository) {
        try {
            schemaRepository.createCompaniesTable();
            schemaRepository.createPersonsTable();
        } catch (final RuntimeException exception) {
            // do nothing
        }
    }

    private static void writeCompanies(final CompanyRepository companyRepository) {
        companyRepository.insertCompany(123, "test");
        companyRepository.insertCompanyBatch(new int[] { 456, 789 }, new String[] { "two", "three" });
        companyRepository.insertCompanyBatch(new int[] { 91, 32 }, new String[] { "more", "other" });
        companyRepository.insertCompany(47, "test");
        companyRepository.insertCompanyBatch(new int[] { 56, 78 }, new String[] { "abc", "def" });
        companyRepository.insertCompanyBatch(new int[] { 612, 768 }, new String[] { "123", "234" });
    }

    private static void standardTests(
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        System.out.println("queryAllCompanies----------------------");
        timed(() -> companyRepository.queryAllCompanies().forEach(System.out::println));

        System.out.println("findCompanyByName----------------------");
        timed(() -> companyRepository.findCompanyByName("test").forEach(System.out::println));

        System.out.println("findCompanies----------------------");
        timed(() -> companyRepository.findCompanies(30, 95).forEach(System.out::println));

        System.out.println("findPerson----------------------");
        timed(() -> personRepository.findPerson("test").forEach(System.out::println));
    }

    private static void streamTests(
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        System.out.println("streamEagerQueryAllCompanies----------------------");
        timed(() -> companyRepository.queryAllCompaniesStreamEager().forEach(System.out::println));

        System.out.println("streamLazyQueryAllCompanies----------------------");
        timed(() -> {
            try (Stream<Map<String, Object>> companies = companyRepository.queryAllCompaniesStreamLazy()) {
                companies.forEach(System.out::println);
            }
        });

        System.out.println("streamLazyFindCompanies----------------------");
        timed(() -> {
            try (Stream<Map<String, Object>> companies = companyRepository.findCompaniesStreamLazy(112, 999)) {
                companies.forEach(System.out::println);
            }
        });

        System.out.println("findPersonStreamLazy----------------------");
        timed(() -> {
            try (Stream<Map<String, Object>> persons = personRepository.findPersonStreamLazy("test")) {
                persons.forEach(System.out::println);
            }
        });
    }

    private static void rxJavaTests(
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        System.out.println("findCompanies-fromIterable----------------------");
        timed(() -> Flowable.fromIterable(companyRepository.findCompanies(30, 50))
                .forEach(System.out::println));

        System.out.println("queryAllCompaniesFlow----------------------");
        timed(() -> companyRepository.queryAllCompaniesFlow()
                .forEach(System.out::println));

        System.out.println("findCompaniesFlow----------------------");
        timed(() -> companyRepository.findCompaniesFlow(20, 120)
                .forEach(System.out::println));

        System.out.println("findCompanyByNameFlow----------------------");
        timed(() -> companyRepository.findCompanyByNameFlow("test")
                .forEach(System.out::println));

        System.out.println("findPersonFlow----------------------");
        timed(() -> personRepository.findPersonFlow("test")
                .forEach(System.out::println));
    }

    private static void timed(final Runnable runnable) {
        final Instant starts = Instant.now();
        runnable.run();
        final Instant ends = Instant.now();
        System.out.println(Duration.between(starts, ends).toMillis() + " ms");
    }

}
