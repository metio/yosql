/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.model.options;

/**
 * Options for the DAO API used in the generated code.
 */
public enum DaoApiOptions {

    /**
     * Uses Ebean in the generated code.
     */
    EBEAN,

    /**
     * Uses the raw JDBC API in generated code.
     */
    JDBC,

    /**
     * Uses JDBI in generated code.
     */
    JDBI,

    /**
     * Uses jOOQ in generated code.
     */
    JOOQ,

    /**
     * Uses JPA in generated code.
     */
    JPA,

    /**
     * Uses Mybatis in generated code.
     */
    MYBATIS,

    /**
     * Uses R2DBC in generated code.
     */
    R2DBC,

    /**
     * Uses Spring-Data-JDBC in generated code.
     */
    SPRING_DATA_JDBC,

    /**
     * Uses Spring-Data-JPA in generated code.
     */
    SPRING_DATA_JPA,

    /**
     * Uses Spring-Data-R2DBC in generated code.
     */
    SPRING_DATA_R2DBC,

    /**
     * Uses Spring-JDBC in generated code.
     */
    SPRING_JDBC

}
