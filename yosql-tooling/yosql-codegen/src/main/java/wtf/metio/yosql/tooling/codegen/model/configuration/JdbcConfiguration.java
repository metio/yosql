/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.model.configuration;

import org.immutables.value.Value;

/**
 * Configures the various JDBC names related options.
 */
@Value.Immutable
public interface JdbcConfiguration {

    static ImmutableJdbcConfiguration.Builder builder() {
        return ImmutableJdbcConfiguration.builder();
    }

    /**
     * @return A JDBC names configuration using default values.
     */
    static ImmutableJdbcConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The field name suffix for the raw SQL statement.
     */
    @Value.Default
    default String rawSuffix() {
        return "_RAW";
    }

    /**
     * @return The field name suffix for the JDBC parameter indexes.
     */
    @Value.Default
    default String indexSuffix() {
        return "_INDEX";
    }

    /**
     * @return The name of the 'dataSource' variable.
     */
    @Value.Default
    default String dataSource() {
        return "dataSource";
    }

    /**
     * @return The name of the 'connection' variable.
     */
    @Value.Default
    default String connection() {
        return "connection";
    }

    /**
     * @return The name of the 'columnCount' variable.
     */
    @Value.Default
    default String columnCount() {
        return "columnCount";
    }

    /**
     * @return The name of the 'columnLabel' variable.
     */
    @Value.Default
    default String columnLabel() {
        return "columnLabel";
    }

    /**
     * @return The name of the 'statement' variable.
     */
    @Value.Default
    default String statement() {
        return "statement";
    }

    /**
     * @return The name of the 'metaData' variable.
     */
    @Value.Default
    default String metaData() {
        return "metaData";
    }

    /**
     * @return The name of the 'resultSet' variable.
     */
    @Value.Default
    default String resultSet() {
        return "resultSet";
    }

    /**
     * @return The name of the 'batch' variable.
     */
    @Value.Default
    default String batch() {
        return "batch";
    }

    /**
     * @return The name of the 'list' variable.
     */
    @Value.Default
    default String list() {
        return "list";
    }

    /**
     * @return The name of the 'jdbcIndex' variable.
     */
    @Value.Default
    default String jdbcIndex() {
        return "jdbcIndex";
    }

    /**
     * @return The name of the 'index' variable.
     */
    @Value.Default
    default String index() {
        return "index";
    }

    /**
     * @return The name of the 'row' variable.
     */
    @Value.Default
    default String row() {
        return "row";
    }

}
