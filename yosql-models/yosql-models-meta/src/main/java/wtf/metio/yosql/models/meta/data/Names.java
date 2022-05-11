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
                .addSettings(action()) // TODO: remove?
                .addSettings(result()) // TODO: remove?
                .addSettings(value())
                .addSettings(emitter()) // TODO: remove?
                .addSettings(name())
                .addSettings(state()) // TODO: remove?
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
                .setDescription("The name of the logger instance.")
                .setType(TypicalTypes.STRING)
                .setValue("LOG")
                .build();
    }

    public static ConfigurationSetting query() {
        return ConfigurationSetting.builder()
                .setName("query")
                .setDescription("The name of selected SQL statement.")
                .setType(TypicalTypes.STRING)
                .setValue("query")
                .build();
    }

    public static ConfigurationSetting rawQuery() {
        return ConfigurationSetting.builder()
                .setName("rawQuery")
                .setDescription("The name of SQL statement before parameter replacement.")
                .setType(TypicalTypes.STRING)
                .setValue("rawQuery")
                .build();
    }

    public static ConfigurationSetting executedQuery() {
        return ConfigurationSetting.builder()
                .setName("executedQuery")
                .setDescription("The name of SQL statement after parameter replacement.")
                .setType(TypicalTypes.STRING)
                .setValue("executedQuery")
                .build();
    }

    public static ConfigurationSetting databaseProductName() {
        return ConfigurationSetting.builder()
                .setName("databaseProductName")
                .setDescription("The name of the variable that holds the database product name.")
                .setType(TypicalTypes.STRING)
                .setValue("databaseProductName")
                .build();
    }

    public static ConfigurationSetting action() {
        return ConfigurationSetting.builder()
                .setName("action")
                .setDescription("The name of an action.")
                .setType(TypicalTypes.STRING)
                .setValue("action")
                .build();
    }

    public static ConfigurationSetting result() {
        return ConfigurationSetting.builder()
                .setName("result")
                .setDescription("The name of a result.")
                .setType(TypicalTypes.STRING)
                .setValue("result")
                .build();
    }

    public static ConfigurationSetting value() {
        return ConfigurationSetting.builder()
                .setName("value")
                .setDescription("The name of a value.")
                .setType(TypicalTypes.STRING)
                .setValue("value")
                .build();
    }

    public static ConfigurationSetting emitter() {
        return ConfigurationSetting.builder()
                .setName("emitter")
                .setDescription("The name of an emitter.")
                .setType(TypicalTypes.STRING)
                .setValue("emitter")
                .build();
    }

    public static ConfigurationSetting name() {
        return ConfigurationSetting.builder()
                .setName("name")
                .setDescription("The name of a name variable.")
                .setType(TypicalTypes.STRING)
                .setValue("name")
                .build();
    }

    public static ConfigurationSetting state() {
        return ConfigurationSetting.builder()
                .setName("state")
                .setDescription("The name of a state.")
                .setType(TypicalTypes.STRING)
                .setValue("state")
                .build();
    }

    public static ConfigurationSetting exception() {
        return ConfigurationSetting.builder()
                .setName("exception")
                .setDescription("The name of an exception.")
                .setType(TypicalTypes.STRING)
                .setValue("exception")
                .build();
    }

    private static ConfigurationSetting rawSuffix() {
        return ConfigurationSetting.builder()
                .setName("rawSuffix")
                .setDescription("The name suffix to add for raw SQL statements.")
                .setType(TypicalTypes.STRING)
                .setValue("_RAW")
                .build();
    }

    private static ConfigurationSetting indexSuffix() {
        return ConfigurationSetting.builder()
                .setName("indexSuffix")
                .setDescription("The name suffix to add for index lookup tables.")
                .setType(TypicalTypes.STRING)
                .setValue("_INDEX")
                .build();
    }

    private static ConfigurationSetting dataSource() {
        return ConfigurationSetting.builder()
                .setName("dataSource")
                .setDescription("The name for a DataSource variable.")
                .setType(TypicalTypes.STRING)
                .setValue("dataSource")
                .build();
    }

    private static ConfigurationSetting connection() {
        return ConfigurationSetting.builder()
                .setName("connection")
                .setDescription("The name for a Connection variable.")
                .setType(TypicalTypes.STRING)
                .setValue("connection")
                .build();
    }

    private static ConfigurationSetting statement() {
        return ConfigurationSetting.builder()
                .setName("statement")
                .setDescription("The name for a Statement variable.")
                .setType(TypicalTypes.STRING)
                .setValue("statement")
                .build();
    }

    private static ConfigurationSetting resultSetMetaData() {
        return ConfigurationSetting.builder()
                .setName("resultSetMetaData")
                .setDescription("The name for a ResultSetMetaData variable.")
                .setType(TypicalTypes.STRING)
                .setValue("resultSetMetaData")
                .build();
    }

    private static ConfigurationSetting databaseMetaData() {
        return ConfigurationSetting.builder()
                .setName("databaseMetaData")
                .setDescription("The name for a DatabaseMetaData variable.")
                .setType(TypicalTypes.STRING)
                .setValue("databaseMetaData")
                .build();
    }

    private static ConfigurationSetting resultSet() {
        return ConfigurationSetting.builder()
                .setName("resultSet")
                .setDescription("The name for a ResultSet variable.")
                .setType(TypicalTypes.STRING)
                .setValue("resultSet")
                .build();
    }

    private static ConfigurationSetting columnCount() {
        return ConfigurationSetting.builder()
                .setName("columnCount")
                .setDescription("The name for a ColumnCount variable.")
                .setType(TypicalTypes.STRING)
                .setValue("columnCount")
                .build();
    }

    private static ConfigurationSetting columnLabel() {
        return ConfigurationSetting.builder()
                .setName("columnLabel")
                .setDescription("The name for a ColumnLabel variable.")
                .setType(TypicalTypes.STRING)
                .setValue("columnLabel")
                .build();
    }

    private static ConfigurationSetting batch() {
        return ConfigurationSetting.builder()
                .setName("batch")
                .setDescription("The name for a Batch variable.")
                .setType(TypicalTypes.STRING)
                .setValue("batch")
                .build();
    }

    private static ConfigurationSetting list() {
        return ConfigurationSetting.builder()
                .setName("list")
                .setDescription("The name for a List variable.")
                .setType(TypicalTypes.STRING)
                .setValue("list")
                .build();
    }

    private static ConfigurationSetting jdbcIndexVariable() {
        return ConfigurationSetting.builder()
                .setName("jdbcIndexVariable")
                .setDescription("The name for a JDBC index variable.")
                .setType(TypicalTypes.STRING)
                .setValue("jdbcIndex")
                .build();
    }

    private static ConfigurationSetting indexVariable() {
        return ConfigurationSetting.builder()
                .setName("indexVariable")
                .setDescription("The name for a index variable.")
                .setType(TypicalTypes.STRING)
                .setValue("index")
                .build();
    }

    private static ConfigurationSetting row() {
        return ConfigurationSetting.builder()
                .setName("row")
                .setDescription("The name for a row variable.")
                .setType(TypicalTypes.STRING)
                .setValue("row")
                .build();
    }

    private Names() {
        // data class
    }

}
