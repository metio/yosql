/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.gradle.jdbc.java11.converter;

import wtf.metio.yosql.example.gradle.jdbc.java11.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ToItemConverter {

    public Item asUserType(final ResultSet resultSet) throws SQLException {
        final var pojo = new Item();
        pojo.setId(resultSet.getInt("id"));
        pojo.setName(resultSet.getString("name"));
        return pojo;
    }

}
