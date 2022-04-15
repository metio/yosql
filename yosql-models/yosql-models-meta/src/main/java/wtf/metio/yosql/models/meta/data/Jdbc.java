/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import javax.lang.model.element.Modifier;
import java.util.List;

public final class Jdbc {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName("Jdbc")
                .setDescription("Configures JDBC related settings.")
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
                .addSettings(utilityPackageName())
                .addSettings(resultStateClassName())
                .addSettings(resultRowClassName())
                .addSettings(flowStateClassName())
                .addSettings(defaultConverter())
                .addSettings(userTypes())
                .setImmutableMethods(derivedMethods())
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

    private static ConfigurationSetting utilityPackageName() {
        return ConfigurationSetting.builder()
                .setName("utilityPackageName")
                .setDescription("The package name for all utility classes.")
                .setType(TypicalTypes.STRING)
                .setValue("com.example.persistence.util")
                .build();
    }

    private static ConfigurationSetting resultStateClassName() {
        return ConfigurationSetting.builder()
                .setName("resultStateClassName")
                .setDescription("The class name of the result-state class")
                .setType(TypicalTypes.STRING)
                .setValue("ResultState")
                .build();
    }

    private static ConfigurationSetting resultRowClassName() {
        return ConfigurationSetting.builder()
                .setName("resultRowClassName")
                .setDescription("The class name of the result-row class")
                .setType(TypicalTypes.STRING)
                .setValue("ResultRow")
                .build();
    }

    private static ConfigurationSetting flowStateClassName() {
        return ConfigurationSetting.builder()
                .setName("flowStateClassName")
                .setDescription("The class name of the flow-state class")
                .setType(TypicalTypes.STRING)
                .setValue("FlowState")
                .build();
    }

    private static List<MethodSpec> derivedMethods() {
        return List.of(resultStateClass(), resultRowClass(), flowStateClass());
    }

    private static MethodSpec resultStateClass() {
        return MethodSpec.methodBuilder("resultStateClass")
                .addAnnotation(Value.Lazy.class)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(ClassName.class)
                .addStatement("return $T.get($L(), $L())", ClassName.class, "utilityPackageName", "resultStateClassName")
                .build();
    }

    private static MethodSpec resultRowClass() {
        return MethodSpec.methodBuilder("resultRowClass")
                .addAnnotation(Value.Lazy.class)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(ClassName.class)
                .addStatement("return $T.get($L(), $L())", ClassName.class, "utilityPackageName", "resultRowClassName")
                .build();
    }

    private static MethodSpec flowStateClass() {
        return MethodSpec.methodBuilder("flowStateClass")
                .addAnnotation(Value.Lazy.class)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(ClassName.class)
                .addStatement("return $T.get($L(), $L())", ClassName.class, "utilityPackageName", "flowStateClassName")
                .build();
    }

    private static ConfigurationSetting defaultConverter() {
        return ConfigurationSetting.builder()
                .setName("defaultConverter")
                .setDescription("The default converter to use, if no other is specified on a query itself.")
                .setType(TypeName.get(ResultRowConverter.class))
                .setCliType(TypicalTypes.STRING)
                .setMavenType(TypicalTypes.STRING)
                .setGradleType(ClassName.bestGuess("wtf.metio.yosql.tooling.gradle.DefaultResultRowConverter"))
                .setCliValue("")
                .setMavenValue("")
                .build();
    }

    private static ConfigurationSetting userTypes() {
        return ConfigurationSetting.builder()
                .setName("userTypes")
                .setDescription("The converters configured by the user.")
                .setType(TypicalTypes.listOf(ResultRowConverter.class))
                .setCliType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setMavenType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setGradleType(TypicalTypes.gradleContainerOf(ClassName.bestGuess("wtf.metio.yosql.tooling.gradle.UserResultRowConverter")))
                .setMavenValue("")
                .setValue(CodeBlock.builder()
                        .add("$T.of()", List.class)
                        .build())
                .build();
    }

    private Jdbc() {
        // data class
    }

}
