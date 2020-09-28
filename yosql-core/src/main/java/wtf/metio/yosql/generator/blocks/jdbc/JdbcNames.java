/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

// TODO: remove? JdbcNamesConfiguration exists as well
public interface JdbcNames {

    String dataSource();
    String connection();
    String statement();
    String metaData();
    String resultSet();
    String columnCount();
    String columnLabel();
    String batch();
    String list();
    String jdbcIndex();
    String index();
    String row();

}
