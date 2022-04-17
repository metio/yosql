/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

public final class Names {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName("Names")
                .setDescription("Configures the names of variables in generated code.")
                .addSettings(logger())
                .addSettings(query())
                .addSettings(rawQuery())
                .addSettings(executedQuery())
                .addSettings(databaseProductName())
                .addSettings(action())
                .addSettings(result())
                .addSettings(value())
                .addSettings(emitter())
                .addSettings(name())
                .addSettings(state())
                .addSettings(exception())
                .addSettings(rawSuffix())
                .addSettings(indexSuffix())
                .addSettings(dataSource())
                .addSettings(connection())
                .addSettings(statement())
                .addSettings(databaseMetaData())
                .addSettings(resultSetMetaData())
                .addSettings(resultSet())
                .addSettings(columnCount())
                .addSettings(columnLabel())
                .addSettings(batch())
                .addSettings(list())
                .addSettings(jdbcIndexVariable())
                .addSettings(indexVariable())
                .addSettings(row())
                .build();
    }

    public static ConfigurationSetting logger() {
        return ConfigurationSetting.builder()
                .setName("logger")
                .setType(TypicalTypes.STRING)
                .setValue("LOG")
                .setDescription("The name of the logger instance.")
                .build();
    }

    public static ConfigurationSetting query() {
        return ConfigurationSetting.builder()
                .setName("query")
                .setType(TypicalTypes.STRING)
                .setValue("query")
                .setDescription("The name of selected SQL statement.")
                .build();
    }

    public static ConfigurationSetting rawQuery() {
        return ConfigurationSetting.builder()
                .setName("rawQuery")
                .setType(TypicalTypes.STRING)
                .setValue("rawQuery")
                .setDescription("The name of SQL statement before parameter replacement.")
                .build();
    }

    public static ConfigurationSetting executedQuery() {
        return ConfigurationSetting.builder()
                .setName("executedQuery")
                .setType(TypicalTypes.STRING)
                .setValue("executedQuery")
                .setDescription("The name of SQL statement after parameter replacement.")
                .build();
    }

    public static ConfigurationSetting databaseProductName() {
        return ConfigurationSetting.builder()
                .setName("databaseProductName")
                .setType(TypicalTypes.STRING)
                .setValue("databaseProductName")
                .setDescription("The name of the variable that holds the database product name.")
                .build();
    }

    public static ConfigurationSetting action() {
        return ConfigurationSetting.builder()
                .setName("action")
                .setType(TypicalTypes.STRING)
                .setValue("action")
                .setDescription("The name of an action.")
                .build();
    }

    public static ConfigurationSetting result() {
        return ConfigurationSetting.builder()
                .setName("result")
                .setType(TypicalTypes.STRING)
                .setValue("result")
                .setDescription("The name of a result.")
                .build();
    }

    public static ConfigurationSetting value() {
        return ConfigurationSetting.builder()
                .setName("value")
                .setType(TypicalTypes.STRING)
                .setValue("value")
                .setDescription("The name of a value.")
                .build();
    }

    public static ConfigurationSetting emitter() {
        return ConfigurationSetting.builder()
                .setName("emitter")
                .setType(TypicalTypes.STRING)
                .setValue("emitter")
                .setDescription("The name of an emitter.")
                .build();
    }

    public static ConfigurationSetting name() {
        return ConfigurationSetting.builder()
                .setName("name")
                .setType(TypicalTypes.STRING)
                .setValue("name")
                .setDescription("The name of a name variable.")
                .build();
    }

    public static ConfigurationSetting state() {
        return ConfigurationSetting.builder()
                .setName("state")
                .setType(TypicalTypes.STRING)
                .setValue("state")
                .setDescription("The name of a state.")
                .build();
    }

    public static ConfigurationSetting exception() {
        return ConfigurationSetting.builder()
                .setName("exception")
                .setType(TypicalTypes.STRING)
                .setValue("exception")
                .setDescription("The name of an exception.")
                .build();
    }

    private static ConfigurationSetting rawSuffix() {
        return ConfigurationSetting.builder()
                .setName("rawSuffix")
                .setType(TypicalTypes.STRING)
                .setValue("_RAW")
                .setDescription("The name suffix to add for raw SQL statements.")
                .build();
    }

    private static ConfigurationSetting indexSuffix() {
        return ConfigurationSetting.builder()
                .setName("indexSuffix")
                .setType(TypicalTypes.STRING)
                .setValue("_INDEX")
                .setDescription("The name suffix to add for index lookup tables.")
                .build();
    }

    private static ConfigurationSetting dataSource() {
        return ConfigurationSetting.builder()
                .setName("dataSource")
                .setType(TypicalTypes.STRING)
                .setValue("dataSource")
                .setDescription("The name for a DataSource variable.")
                .build();
    }

    private static ConfigurationSetting connection() {
        return ConfigurationSetting.builder()
                .setName("connection")
                .setType(TypicalTypes.STRING)
                .setValue("connection")
                .setDescription("The name for a Connection variable.")
                .build();
    }

    private static ConfigurationSetting statement() {
        return ConfigurationSetting.builder()
                .setName("statement")
                .setType(TypicalTypes.STRING)
                .setValue("statement")
                .setDescription("The name for a Statement variable.")
                .build();
    }

    private static ConfigurationSetting resultSetMetaData() {
        return ConfigurationSetting.builder()
                .setName("resultSetMetaData")
                .setType(TypicalTypes.STRING)
                .setValue("resultSetMetaData")
                .setDescription("The name for a ResultSetMetaData variable.")
                .build();
    }

    private static ConfigurationSetting databaseMetaData() {
        return ConfigurationSetting.builder()
                .setName("databaseMetaData")
                .setType(TypicalTypes.STRING)
                .setValue("databaseMetaData")
                .setDescription("The name for a DatabaseMetaData variable.")
                .build();
    }

    private static ConfigurationSetting resultSet() {
        return ConfigurationSetting.builder()
                .setName("resultSet")
                .setType(TypicalTypes.STRING)
                .setValue("resultSet")
                .setDescription("The name for a ResultSet variable.")
                .build();
    }

    private static ConfigurationSetting columnCount() {
        return ConfigurationSetting.builder()
                .setName("columnCount")
                .setType(TypicalTypes.STRING)
                .setValue("columnCount")
                .setDescription("The name for a ColumnCount variable.")
                .build();
    }

    private static ConfigurationSetting columnLabel() {
        return ConfigurationSetting.builder()
                .setName("columnLabel")
                .setType(TypicalTypes.STRING)
                .setValue("columnLabel")
                .setDescription("The name for a ColumnLabel variable.")
                .build();
    }

    private static ConfigurationSetting batch() {
        return ConfigurationSetting.builder()
                .setName("batch")
                .setType(TypicalTypes.STRING)
                .setValue("batch")
                .setDescription("The name for a Batch variable.")
                .build();
    }

    private static ConfigurationSetting list() {
        return ConfigurationSetting.builder()
                .setName("list")
                .setType(TypicalTypes.STRING)
                .setValue("list")
                .setDescription("The name for a List variable.")
                .build();
    }

    private static ConfigurationSetting jdbcIndexVariable() {
        return ConfigurationSetting.builder()
                .setName("jdbcIndexVariable")
                .setType(TypicalTypes.STRING)
                .setValue("jdbcIndex")
                .setDescription("The name for a JDBC index variable.")
                .build();
    }

    private static ConfigurationSetting indexVariable() {
        return ConfigurationSetting.builder()
                .setName("indexVariable")
                .setType(TypicalTypes.STRING)
                .setValue("index")
                .setDescription("The name for a index variable.")
                .build();
    }

    private static ConfigurationSetting row() {
        return ConfigurationSetting.builder()
                .setName("row")
                .setType(TypicalTypes.STRING)
                .setValue("row")
                .setDescription("The name for a row variable.")
                .build();
    }

    private Names() {
        // data class
    }

}
