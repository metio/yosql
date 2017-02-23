/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.example.app.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.app.model.Item;
import com.example.persistence.util.ResultState;

public final class ToItemConverter {

    public final Item asUserType(final ResultState result) throws SQLException {
        final ResultSet resultSet = result.getResultSet();
        final Item pojo = new Item();
        pojo.setId(resultSet.getInt("id"));
        pojo.setName(resultSet.getString("name"));
        return pojo;
    }

}
