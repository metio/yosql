/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.example.maven.jdbc.java16;

import wtf.metio.yosql.example.common.*;
import wtf.metio.yosql.example.maven.jdbc.java16.converter.ToItemConverter;
import wtf.metio.yosql.example.maven.jdbc.java16.persistence.*;
import wtf.metio.yosql.example.maven.jdbc.java16.persistence.converter.ToMapConverter;

public final class ExampleApp {

    public static void main(final String[] arguments) {
        try (final var dataSource = DataSourceCreator.createDataSource("java16")) {
            final var schemaRepository = new SchemaRepository(dataSource);
            final var toMapConverter = new ToMapConverter();
            final var toItemConverter = new ToItemConverter();
            final var companyRepository = new CompanyRepository(dataSource, toMapConverter);
            final var personRepository = new PersonRepository(dataSource, toMapConverter);
            final var itemRepository = new ItemRepository(dataSource, toItemConverter, toMapConverter);
            final var userRepository = new UserRepository(dataSource, toMapConverter);
            final var adminRepository = new AdminRepository(dataSource, toMapConverter);
            final var callcenterRepository = new CallcenterRepository(dataSource, toMapConverter);

            SchemaCreator.builder()
                    .dropCompaniesTable(schemaRepository::dropCompaniesTable)
                    .dropPersonsTable(schemaRepository::dropPersonsTable)
                    .dropItemsTable(schemaRepository::dropItemsTable)
                    .dropUsersTable(schemaRepository::dropUsersTable)
                    .createCompaniesTable(schemaRepository::createCompaniesTable)
                    .createPersonsTable(schemaRepository::createPersonsTable)
                    .createItemsTable(schemaRepository::createItemsTable)
                    .createUsersTable(schemaRepository::createUsersTable)
                    .createNextPrimeFunction(schemaRepository::createNextPrimeFunction)
                    .createRandomNumberFunction(schemaRepository::createRandomNumberFunction)
                    .createNamesFunction(schemaRepository::createNamesFunction)
                    .build()
                    .createDatabaseSchema();

            WritingTests.builder()
                    .insertCompany(companyRepository::insertCompany)
                    .insertPerson(personRepository::insertPerson)
                    .insertCompanyBatch(companyRepository::insertCompanyBatch)
                    .insertPersonBatch(personRepository::insertPersonBatch)
                    .updateUser(adminRepository::updateUser)
                    .insertUser(userRepository::insertUser)
                    .insertUserBatch(userRepository::insertUserBatch)
                    .insertItem(itemRepository::insertItem)
                    .insertItemBatch(itemRepository::insertItemBatch)
                    .build()
                    .runWritingTests();

            ReadingTests.builder()
                    .queryAllCompanies(companyRepository::queryAllCompanies)
                    .findCompanyByName(companyRepository::findCompanyByName)
                    .findCompanies(companyRepository::findCompanies)
                    .findPerson(personRepository::findPerson)
                    .findPersons(personRepository::findPersons)
                    .findItemByAllNames(itemRepository::findItemByAllNames)
                    .findItemByName(itemRepository::findItemByName)
                    .queryAllUsers(userRepository::queryAllUsers)
                    .querySpecialUserWithConstantId(userRepository::querySpecialUserWithConstantId)
                    .queryAdminUser(adminRepository::queryAdminUser)
                    .build()
                    .runReadingTests();

            CallingTests.builder()
                    .callRandomNumber(callcenterRepository::callRandomNumber)
                    .callNextPrime(callcenterRepository::callNextPrime)
                    .callNames(callcenterRepository::callNames)
                    .build()
                    .runCallingTests();
        }
    }

    private ExampleApp() {
        // application class
    }

}
