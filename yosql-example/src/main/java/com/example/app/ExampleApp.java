package com.example.app;

import java.util.Map;
import java.util.stream.Stream;

import org.h2.jdbcx.JdbcDataSource;

import com.example.persistence.CompanyRepository;

public class ExampleApp {

    public static final void main(final String[] arguments) {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1");

        final CompanyRepository companyRepository = new CompanyRepository(ds);
        companyRepository.createCompaniesTable();
        companyRepository.insertCompany(123, "test");
        companyRepository.queryAllCompanies().forEach(System.out::println);
        System.out.println("----------------------");
        companyRepository.batchInsertCompany(new int[] { 456, 789 }, new String[] { "two", "three" });
        companyRepository.streamEagerQueryAllCompanies().forEach(System.out::println);
        System.out.println("----------------------");
        companyRepository.batchInsertCompany(new int[] { 91, 32 }, new String[] { "more", "other" });
        try (Stream<Map<String, Object>> companies = companyRepository.streamLazyQueryAllCompanies()) {
            companies.forEach(System.out::println);
        }
        System.out.println("----------------------");
        companyRepository.insertCompany(47, "test");
        companyRepository.findCompanyByName("test").forEach(System.out::println);
        System.out.println("----------------------");
        companyRepository.batchInsertCompany(new int[] { 56, 78 }, new String[] { "abc", "def" });
        companyRepository.findCompaniesWithinRange(30, 95).forEach(System.out::println);
    }

}
