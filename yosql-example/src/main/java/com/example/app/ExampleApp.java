package com.example.app;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import org.h2.jdbcx.JdbcDataSource;

import com.example.persistence.CompanyRepository;
import com.example.persistence.SchemaRepository;

import io.reactivex.Flowable;

public class ExampleApp {

    public static final void main(final String[] arguments) {
        if (Arrays.stream(arguments).anyMatch("h2"::equals)) {
            final JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1");

            final SchemaRepository schemaRepository = new SchemaRepository(dataSource);
            final CompanyRepository companyRepository = new CompanyRepository(dataSource);

            initializeDatabase(schemaRepository, companyRepository);

            standardTests(companyRepository);
            streamTests(companyRepository);
            rxJavaTests(companyRepository);
        } else if (Arrays.stream(arguments).anyMatch("psql"::equals)) {
            // start psql first
        }
    }

    private static void initializeDatabase(final SchemaRepository schemaRepository,
            final CompanyRepository companyRepository) {
        schemaRepository.createCompaniesTable();
        companyRepository.insertCompany(123, "test");
        companyRepository.insertCompanyBatch(new int[] { 456, 789 }, new String[] { "two", "three" });
        companyRepository.insertCompanyBatch(new int[] { 91, 32 }, new String[] { "more", "other" });
        companyRepository.insertCompany(47, "test");
        companyRepository.insertCompanyBatch(new int[] { 56, 78 }, new String[] { "abc", "def" });
        companyRepository.insertCompanyBatch(new int[] { 612, 768 }, new String[] { "123", "234" });
    }

    private static void standardTests(final CompanyRepository companyRepository) {
        System.out.println("queryAllCompanies----------------------");
        timed(() -> companyRepository.queryAllCompanies().forEach(System.out::println));

        System.out.println("findCompanyByName----------------------");
        timed(() -> companyRepository.findCompanyByName("test").forEach(System.out::println));

        System.out.println("findCompanies----------------------");
        timed(() -> companyRepository.findCompanies(30, 95).forEach(System.out::println));
    }

    private static void streamTests(final CompanyRepository companyRepository) {
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
    }

    private static void rxJavaTests(final CompanyRepository companyRepository) {
        System.out.println("findCompanies-fromIterable----------------------");
        timed(() -> Flowable.fromIterable(companyRepository.findCompanies(30, 50))
                .forEach(System.out::println));

        System.out.println("flowQueryAllCompanies----------------------");
        timed(() -> companyRepository.queryAllCompaniesFlow()
                .forEach(System.out::println));

        System.out.println("flowFindCompanies----------------------");
        timed(() -> companyRepository.findCompaniesFlow(20, 120)
                .forEach(System.out::println));

        System.out.println("flowFindCompanyByName----------------------");
        timed(() -> companyRepository.findCompanyByNameFlow("test")
                .forEach(System.out::println));
    }

    private static void timed(final Runnable runnable) {
        final Instant starts = Instant.now();
        runnable.run();
        final Instant ends = Instant.now();
        System.out.println(Duration.between(starts, ends).toMillis() + " ms");
    }

}
