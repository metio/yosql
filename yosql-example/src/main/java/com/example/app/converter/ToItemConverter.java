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
