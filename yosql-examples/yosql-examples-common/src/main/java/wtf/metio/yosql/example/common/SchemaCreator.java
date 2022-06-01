/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
        } catch (final RuntimeException exception) {
            LOG.log(System.Logger.Level.ERROR, "Error while creating database schema", exception);
            System.exit(1);
        }
    }

}
