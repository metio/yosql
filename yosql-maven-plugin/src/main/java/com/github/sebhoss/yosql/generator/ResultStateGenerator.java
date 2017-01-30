package com.github.sebhoss.yosql.generator;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.PluginRuntimeConfig;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class ResultStateGenerator {

    public static final String        RESULT_STATE_CLASS_NAME = "ResultState";

    private final CommonGenerator     commonGenerator;
    private final TypeWriter          typeWriter;
    private final PluginRuntimeConfig runtimeConfig;

    @Inject
    public ResultStateGenerator(
            final CommonGenerator commonGenerator,
            final TypeWriter typeWriter,
            final PluginRuntimeConfig runtimeConfig) {
        this.commonGenerator = commonGenerator;
        this.typeWriter = typeWriter;
        this.runtimeConfig = runtimeConfig;
    }

    public void generateResultStateClass() {
        final TypeSpec type = TypeSpec.classBuilder(RESULT_STATE_CLASS_NAME)
                .addModifiers(TypicalModifiers.OPEN_CLASS)
                .addFields(fields())
                .addMethods(methods())
                .addAnnotation(commonGenerator.generatedAnnotation(ResultStateGenerator.class))
                .build();
        final String packageName = runtimeConfig.getBasePackageName() + "." + runtimeConfig.getUtilityPackageName();
        typeWriter.writeType(runtimeConfig.getOutputBaseDirectory().toPath(), packageName, type);
        runtimeConfig.getLogger().info(String.format("Generated [%s.%s]", packageName, RESULT_STATE_CLASS_NAME));
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
        final List<MethodSpec> fields = new ArrayList<>();
        fields.add(constructor());
        fields.add(next());
        fields.add(getColumnName());
        fields.add(getObject());
        fields.add(getColumnCount());
        return fields;
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

    private MethodSpec getObject() {
        return TypicalMethods.publicMethod("getObject")
                .returns(Object.class)
                .addParameter(TypicalParameters.index())
                .addException(SQLException.class)
                .addStatement("return $N.getObject($N)", TypicalNames.RESULT_SET, TypicalNames.INDEX)
                .build();
    }

    private MethodSpec getColumnCount() {
        return TypicalMethods.publicMethod("getColumnCount")
                .returns(int.class)
                .addStatement("return $N", TypicalNames.COLUMN_COUNT)
                .build();
    }

}
