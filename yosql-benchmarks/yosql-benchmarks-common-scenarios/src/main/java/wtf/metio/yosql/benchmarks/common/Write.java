/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmarks.common;

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
    void updateManyToOneRelation();

    /**
     * Deletes an entity by its primary key.
     */
    void deleteSingleEntityByPrimaryKey();

}
