/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.benchmark.dao.common;

/**
 * Describes all benchmark scenarios that read data from a database.
 */
public interface Read {

    /**
     * Reads a single entity by its primary key.
     */
    void readSingleEntityByPrimaryKey();

    /**
     * Reads a one-to-many relationship.
     */
    void readOneToManyRelation();

    /**
     * Reads a many-to-one relationship.
     */
    void readManyToOneRelation();

    /**
     * Read multiple entities.
     */
    void readMultipleEntities();

    /**
     * Read multiple entities but filter them in the database before returning.
     */
    void readMultipleEntitiesBasedOnCondition();

}
