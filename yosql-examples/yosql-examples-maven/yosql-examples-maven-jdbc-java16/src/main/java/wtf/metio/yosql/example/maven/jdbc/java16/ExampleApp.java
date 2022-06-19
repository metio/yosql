/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.example.maven.jdbc.java16;

import wtf.metio.yosql.example.common.DataSourceCreator;
import wtf.metio.yosql.example.common.ReadingTests;
import wtf.metio.yosql.example.common.SchemaCreator;
import wtf.metio.yosql.example.common.WritingTests;
import wtf.metio.yosql.example.maven.jdbc.java16.converter.ToItemConverter;
import wtf.metio.yosql.example.maven.jdbc.java16.persistence.*;
import wtf.metio.yosql.example.maven.jdbc.java16.persistence.converter.ToMapConverter;

public final class ExampleApp {

    public static void main(final String[] arguments) {
        try (final var dataSource = DataSourceCreator.createDataSource();) {
            final var schemaRepository = new SchemaRepository(dataSource);
            final var toMapConverter = new ToMapConverter();
            final var toItemConverter = new ToItemConverter();
            final var companyRepository = new CompanyRepository(dataSource, toMapConverter);
            final var personRepository = new PersonRepository(dataSource, toMapConverter);
            final var itemRepository = new ItemRepository(dataSource, toItemConverter, toMapConverter);
            final var userRepository = new UserRepository(dataSource, toMapConverter);
            final var adminRepository = new AdminRepository(dataSource, toMapConverter);

            SchemaCreator.builder()
                    .dropCompaniesTable(schemaRepository::dropCompaniesTable)
                    .dropPersonsTable(schemaRepository::dropPersonsTable)
                    .dropItemsTable(schemaRepository::dropItemsTable)
                    .dropUsersTable(schemaRepository::dropUsersTable)
                    .createCompaniesTable(schemaRepository::createCompaniesTable)
                    .createPersonsTable(schemaRepository::createPersonsTable)
                    .createItemsTable(schemaRepository::createItemsTable)
                    .createUsersTable(schemaRepository::createUsersTable)
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
        }
    }

    private ExampleApp() {
        // application class
    }

}
