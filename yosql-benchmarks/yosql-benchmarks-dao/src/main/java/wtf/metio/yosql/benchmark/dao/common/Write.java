/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.benchmark.dao.common;

/**
 * Describes all benchmark scenarios that write data into a database.
 */
public interface Write {

    /**
     * Writes multiple entities in a batch.
     */
    void writeMultipleEntities();

    /**
     * Writes a single entity.
     */
    void writeSingleEntity();

    /**
     * Updates a one-to-many relationship.
     */
    void updateOneToManyRelation();

    /**
     * Updates a many-to-one relationship.
     */
    void updateSingleEntity();

    /**
     * Deletes an entity by its primary key.
     */
    void deleteSingleEntityByPrimaryKey();

}
