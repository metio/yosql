/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.example.common;

import org.immutables.value.Value;

import java.util.function.IntSupplier;

@Value.Immutable
public interface SchemaCreator {

    System.Logger LOG = System.getLogger(SchemaCreator.class.getName());

    static ImmutableSchemaCreator.DropCompaniesTableBuildStage builder() {
        return ImmutableSchemaCreator.builder();
    }

    IntSupplier dropCompaniesTable();

    IntSupplier dropPersonsTable();

    IntSupplier dropItemsTable();

    IntSupplier dropUsersTable();

    IntSupplier createCompaniesTable();

    IntSupplier createPersonsTable();

    IntSupplier createItemsTable();

    IntSupplier createUsersTable();

    IntSupplier createNextPrimeFunction();

    IntSupplier createRandomNumberFunction();

    IntSupplier createNamesFunction();

    @Value.Lazy
    default void createDatabaseSchema() {
        try {
            dropCompaniesTable().getAsInt();
            dropPersonsTable().getAsInt();
            dropItemsTable().getAsInt();
            dropUsersTable().getAsInt();

            createCompaniesTable().getAsInt();
            createPersonsTable().getAsInt();
            createItemsTable().getAsInt();
            createUsersTable().getAsInt();
            createNextPrimeFunction().getAsInt();
            createRandomNumberFunction().getAsInt();
            createNamesFunction().getAsInt();
        } catch (final RuntimeException exception) {
            LOG.log(System.Logger.Level.ERROR, "Error while creating database schema", exception);
            System.exit(1);
        }
    }

}
