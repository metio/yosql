/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmarks.common;

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
     * Read a complex data structure composed of multiple tables.
     */
    void readComplexRelationship();

    /**
     * Read multiple entities but filter them in the database before returning.
     */
    void readMultipleEntitiesBasedOnCondition();

}
