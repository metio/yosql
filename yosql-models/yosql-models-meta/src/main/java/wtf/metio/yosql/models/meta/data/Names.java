/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Names extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Names.class.getSimpleName();

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures the names of variables in generated code.")
                .addAllSettings(settings())
                .addImmutableMethods(uniqueValueCount(settings()))
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
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
        final var name = "logger";
        final var description = "The name of the logger instance.";
        final var value = "LOG";
        return setting(GROUP_NAME, name, description, value)
                .build();
    }

    public static ConfigurationSetting query() {
        final var name = "query";
        final var description = "The name of selected SQL statement.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    public static ConfigurationSetting rawQuery() {
        final var name = "rawQuery";
        final var description = "The name of SQL statement before parameter replacement.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    public static ConfigurationSetting executedQuery() {
        final var name = "executedQuery";
        final var description = "The name of SQL statement after parameter replacement.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    public static ConfigurationSetting databaseProductName() {
        final var name = "databaseProductName";
        final var description = "The name of the variable that holds the database product name.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    public static ConfigurationSetting action() {
        final var name = "action";
        final var description = "The name of an action.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    public static ConfigurationSetting exception() {
        final var name = "exception";
        final var description = "The name of an exception.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting rawSuffix() {
        final var name = "rawSuffix";
        final var description = "The name suffix to add for raw SQL statements.";
        final var value = "_RAW";
        return setting(GROUP_NAME, name, description, value)
                .build();
    }

    private static ConfigurationSetting indexSuffix() {
        final var name = "indexSuffix";
        final var description = "The name suffix to add for index lookup tables.";
        final var value = "_INDEX";
        return setting(GROUP_NAME, name, description, value)
                .build();
    }

    private static ConfigurationSetting dataSource() {
        final var name = "dataSource";
        final var description = "The name for a DataSource variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting connection() {
        final var name = "connection";
        final var description = "The name for a Connection variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting statement() {
        final var name = "statement";
        final var description = "The name for a Statement variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting resultSetMetaData() {
        final var name = "resultSetMetaData";
        final var description = "The name for a ResultSetMetaData variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting databaseMetaData() {
        final var name = "databaseMetaData";
        final var description = "The name for a DatabaseMetaData variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting resultSet() {
        final var name = "resultSet";
        final var description = "The name for a ResultSet variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting columnCount() {
        final var name = "columnCount";
        final var description = "The name for a ColumnCount variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting columnLabel() {
        final var name = "columnLabel";
        final var description = "The name for a ColumnLabel variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting batch() {
        final var name = "batch";
        final var description = "The name for a Batch variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting list() {
        final var name = "list";
        final var description = "The name for a List variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private static ConfigurationSetting jdbcIndexVariable() {
        final var name = "jdbcIndexVariable";
        final var description = "The name for a JDBC index variable.";
        final var value = "jdbcIndex";
        return setting(GROUP_NAME, name, description, value)
                .build();
    }

    private static ConfigurationSetting indexVariable() {
        final var name = "indexVariable";
        final var description = "The name for a index variable.";
        final var value = "index";
        return setting(GROUP_NAME, name, description, value)
                .build();
    }

    private static ConfigurationSetting row() {
        final var name = "row";
        final var description = "The name for a row variable.";
        return setting(GROUP_NAME, name, description, name)
                .build();
    }

    private Names() {
        // data class
    }

}
