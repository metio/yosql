package com.github.sebhoss.yosql.generator.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.generator.AnnotationGenerator;
import com.github.sebhoss.yosql.generator.TypeWriter;
import com.github.sebhoss.yosql.generator.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.generator.helpers.TypicalMethods;
import com.github.sebhoss.yosql.generator.helpers.TypicalModifiers;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalParameters;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.plugin.PluginConfig;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class ResultStateGenerator {

    public static final String        RESULT_STATE_CLASS_NAME = "ResultState";

    private final AnnotationGenerator annotations;
    private final TypeWriter          typeWriter;
    private final PluginConfig        pluginConfig;

    @Inject
    public ResultStateGenerator(
            final AnnotationGenerator annotations,
            final TypeWriter typeWriter,
            final PluginConfig pluginConfig) {
        this.annotations = annotations;
        this.typeWriter = typeWriter;
        this.pluginConfig = pluginConfig;
    }

    public void generateResultStateClass() {
        final TypeSpec type = TypicalTypes.openClass(RESULT_STATE_CLASS_NAME)
                .addFields(fields())
                .addMethods(methods())
                .addAnnotations(annotations.generatedClass(ResultStateGenerator.class))
                .build();
        final String packageName = pluginConfig.getBasePackageName() + "." + pluginConfig.getUtilityPackageName();
        typeWriter.writeType(pluginConfig.getOutputBaseDirectory().toPath(), packageName, type);
    }

    private Iterable<FieldSpec> fields() {
        final List<FieldSpec> fields = new ArrayList<>();
        fields.add(resultSetField());
        fields.add(metaDataField());
        fields.add(columnCountField());
        return fields;
    }

    private FieldSpec resultSetField() {
        return FieldSpec.builder(ResultSet.class, TypicalNames.RESULT_SET)
                .addModifiers(TypicalModifiers.PROTECTED_FIELD)
                .build();
    }

    private FieldSpec metaDataField() {
        return FieldSpec.builder(ResultSetMetaData.class, TypicalNames.META_DATA)
                .addModifiers(TypicalModifiers.PROTECTED_FIELD)
                .build();
    }

    private FieldSpec columnCountField() {
        return FieldSpec.builder(int.class, TypicalNames.COLUMN_COUNT)
                .addModifiers(TypicalModifiers.PROTECTED_FIELD)
                .build();
    }

    private Iterable<MethodSpec> methods() {
        final List<MethodSpec> methods = new ArrayList<>();
        methods.add(constructor());
        methods.add(next());
        methods.add(getColumnName());
        methods.add(getResultSet());
        methods.add(getColumnCount());
        return methods;
    }

    private MethodSpec constructor() {
        return TypicalMethods.constructor()
                .addParameter(TypicalParameters.resultSet())
                .addParameter(TypicalParameters.metaData())
                .addParameter(TypicalParameters.columnCount())
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.RESULT_SET))
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.META_DATA))
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.COLUMN_COUNT))
                .build();
    }

    private MethodSpec next() {
        return TypicalMethods.publicMethod("next")
                .returns(boolean.class)
                .addException(SQLException.class)
                .addStatement("return $N.next()", TypicalNames.RESULT_SET)
                .build();
    }

    private MethodSpec getColumnName() {
        return TypicalMethods.publicMethod("getColumnName")
                .returns(String.class)
                .addParameter(TypicalParameters.index())
                .addException(SQLException.class)
                .addStatement("return $N.getColumnName($N)", TypicalNames.META_DATA, TypicalNames.INDEX)
                .build();
    }

    private MethodSpec getResultSet() {
        return TypicalMethods.publicMethod("getResultSet")
                .returns(ResultSet.class)
                .addStatement("return $N", TypicalNames.RESULT_SET)
                .build();
    }

    private MethodSpec getColumnCount() {
        return TypicalMethods.publicMethod("getColumnCount")
                .returns(int.class)
                .addStatement("return $N", TypicalNames.COLUMN_COUNT)
                .build();
    }

}
