/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Names {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName("Names")
                .setDescription("Configures the names of variables in generated code.")
                .addAllSettings(settings())
                .setImmutableMethods(List.of(uniqueValueCount(settings())))
                .build();
    }

    private static List<ConfigurationSetting> settings() {
        return List.of(
                logger(),
                query(),
                rawQuery(),
                executedQuery(),
                databaseProductName(),
                action(),
                exception(),
                rawSuffix(),
                indexSuffix(),
                dataSource(),
                connection(),
                statement(),
                databaseMetaData(),
                resultSetMetaData(),
                resultSet(),
                columnCount(),
                columnLabel(),
                batch(),
                list(),
                jdbcIndexVariable(),
                indexVariable(),
                row());
    }

    private static MethodSpec uniqueValueCount(final List<ConfigurationSetting> settings) {
        final var values = settings.stream()
                .map(ConfigurationSetting::name)
                .map(name -> name + "()")
                .collect(Collectors.joining(",\n", "\n", ""));

        return MethodSpec.methodBuilder("uniqueValueCount")
                .addAnnotation(AnnotationSpec.builder(Value.Lazy.class).build())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .returns(TypicalTypes.MAP_OF_STRING_AND_LONGS)
                .addStatement("final var values = $T.of($L)", List.class, values)
                .addStatement("return values.stream().collect($T.groupingBy($T.identity(), $T.counting()))",
                        Collectors.class, Function.class, Collectors.class)
                .build();
    }

    public static ConfigurationSetting logger() {
        return ConfigurationSetting.builder()
                .setName("logger")
                .setDescription("The name of the logger instance.")
                .setType(ClassName.get(String.class))
                .setValue("LOG")
                .build();
    }

    public static ConfigurationSetting query() {
        return ConfigurationSetting.builder()
                .setName("query")
                .setDescription("The name of selected SQL statement.")
                .setType(ClassName.get(String.class))
                .setValue("query")
                .build();
    }

    public static ConfigurationSetting rawQuery() {
        return ConfigurationSetting.builder()
                .setName("rawQuery")
                .setDescription("The name of SQL statement before parameter replacement.")
                .setType(ClassName.get(String.class))
                .setValue("rawQuery")
                .build();
    }

    public static ConfigurationSetting executedQuery() {
        return ConfigurationSetting.builder()
                .setName("executedQuery")
                .setDescription("The name of SQL statement after parameter replacement.")
                .setType(ClassName.get(String.class))
                .setValue("executedQuery")
                .build();
    }

    public static ConfigurationSetting databaseProductName() {
        return ConfigurationSetting.builder()
                .setName("databaseProductName")
                .setDescription("The name of the variable that holds the database product name.")
                .setType(ClassName.get(String.class))
                .setValue("databaseProductName")
                .build();
    }

    public static ConfigurationSetting action() {
        return ConfigurationSetting.builder()
                .setName("action")
                .setDescription("The name of an action.")
                .setType(ClassName.get(String.class))
                .setValue("action")
                .build();
    }

    public static ConfigurationSetting exception() {
        return ConfigurationSetting.builder()
                .setName("exception")
                .setDescription("The name of an exception.")
                .setType(ClassName.get(String.class))
                .setValue("exception")
                .build();
    }

    private static ConfigurationSetting rawSuffix() {
        return ConfigurationSetting.builder()
                .setName("rawSuffix")
                .setDescription("The name suffix to add for raw SQL statements.")
                .setType(ClassName.get(String.class))
                .setValue("_RAW")
                .build();
    }

    private static ConfigurationSetting indexSuffix() {
        return ConfigurationSetting.builder()
                .setName("indexSuffix")
                .setDescription("The name suffix to add for index lookup tables.")
                .setType(ClassName.get(String.class))
                .setValue("_INDEX")
                .build();
    }

    private static ConfigurationSetting dataSource() {
        return ConfigurationSetting.builder()
                .setName("dataSource")
                .setDescription("The name for a DataSource variable.")
                .setType(ClassName.get(String.class))
                .setValue("dataSource")
                .build();
    }

    private static ConfigurationSetting connection() {
        return ConfigurationSetting.builder()
                .setName("connection")
                .setDescription("The name for a Connection variable.")
                .setType(ClassName.get(String.class))
                .setValue("connection")
                .build();
    }

    private static ConfigurationSetting statement() {
        return ConfigurationSetting.builder()
                .setName("statement")
                .setDescription("The name for a Statement variable.")
                .setType(ClassName.get(String.class))
                .setValue("statement")
                .build();
    }

    private static ConfigurationSetting resultSetMetaData() {
        return ConfigurationSetting.builder()
                .setName("resultSetMetaData")
                .setDescription("The name for a ResultSetMetaData variable.")
                .setType(ClassName.get(String.class))
                .setValue("resultSetMetaData")
                .build();
    }

    private static ConfigurationSetting databaseMetaData() {
        return ConfigurationSetting.builder()
                .setName("databaseMetaData")
                .setDescription("The name for a DatabaseMetaData variable.")
                .setType(ClassName.get(String.class))
                .setValue("databaseMetaData")
                .build();
    }

    private static ConfigurationSetting resultSet() {
        return ConfigurationSetting.builder()
                .setName("resultSet")
                .setDescription("The name for a ResultSet variable.")
                .setType(ClassName.get(String.class))
                .setValue("resultSet")
                .build();
    }

    private static ConfigurationSetting columnCount() {
        return ConfigurationSetting.builder()
                .setName("columnCount")
                .setDescription("The name for a ColumnCount variable.")
                .setType(ClassName.get(String.class))
                .setValue("columnCount")
                .build();
    }

    private static ConfigurationSetting columnLabel() {
        return ConfigurationSetting.builder()
                .setName("columnLabel")
                .setDescription("The name for a ColumnLabel variable.")
                .setType(ClassName.get(String.class))
                .setValue("columnLabel")
                .build();
    }

    private static ConfigurationSetting batch() {
        return ConfigurationSetting.builder()
                .setName("batch")
                .setDescription("The name for a Batch variable.")
                .setType(ClassName.get(String.class))
                .setValue("batch")
                .build();
    }

    private static ConfigurationSetting list() {
        return ConfigurationSetting.builder()
                .setName("list")
                .setDescription("The name for a List variable.")
                .setType(ClassName.get(String.class))
                .setValue("list")
                .build();
    }

    private static ConfigurationSetting jdbcIndexVariable() {
        return ConfigurationSetting.builder()
                .setName("jdbcIndexVariable")
                .setDescription("The name for a JDBC index variable.")
                .setType(ClassName.get(String.class))
                .setValue("jdbcIndex")
                .build();
    }

    private static ConfigurationSetting indexVariable() {
        return ConfigurationSetting.builder()
                .setName("indexVariable")
                .setDescription("The name for a index variable.")
                .setType(ClassName.get(String.class))
                .setValue("index")
                .build();
    }

    private static ConfigurationSetting row() {
        return ConfigurationSetting.builder()
                .setName("row")
                .setDescription("The name for a row variable.")
                .setType(ClassName.get(String.class))
                .setValue("row")
                .build();
    }

    private Names() {
        // data class
    }

}
