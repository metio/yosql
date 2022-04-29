/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.example.maven.jdbc.java16.converter;

import wtf.metio.yosql.example.maven.jdbc.java16.model.Item;
import wtf.metio.yosql.example.maven.jdbc.java16.persistence.converter.ResultState;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ToItemConverter {

    public Item asUserType(final ResultState result) throws SQLException {
        final ResultSet resultSet = result.getResultSet();
        final Item pojo = new Item();
        pojo.setId(resultSet.getInt("id"));
        pojo.setName(resultSet.getString("name"));
        return pojo;
    }

}
