package com.github.sebhoss.yosql.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.PluginRuntimeConfig;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class FlowStateGenerator {

    public static final String        FLOW_STATE_CLASS_NAME = "FlowState";

    private final CommonGenerator     commonGenerator;
    private final TypeWriter          typeWriter;
    private final PluginRuntimeConfig runtimeConfig;

    @Inject
    public FlowStateGenerator(
            final CommonGenerator commonGenerator,
            final TypeWriter typeWriter,
            final PluginRuntimeConfig runtimeConfig) {
        this.commonGenerator = commonGenerator;
        this.typeWriter = typeWriter;
        this.runtimeConfig = runtimeConfig;
    }

    public void generateFlowStateClass() {
        final TypeSpec type = TypeSpec.classBuilder(FLOW_STATE_CLASS_NAME)
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .addFields(fields())
                .addAnnotation(commonGenerator.generatedAnnotation(FlowStateGenerator.class))
                .build();
        typeWriter.writeType(runtimeConfig.getOutputBaseDirectory().toPath(), runtimeConfig.getUtilityPackageName(),
                type);
    }

    private Iterable<FieldSpec> fields() {
        final List<FieldSpec> fields = new ArrayList<>();
        fields.add(connectionField());
        fields.add(preparedStatementField());
        fields.add(resultSetField());
        fields.add(metaDataField());
        fields.add(columnCountField());
        return fields;
    }

    private FieldSpec connectionField() {
        return FieldSpec.builder(Connection.class, TypicalNames.CONNECTION)
                .addModifiers(TypicalModifiers.PUBLIC_FIELD)
                .build();
    }

    private FieldSpec preparedStatementField() {
        return FieldSpec.builder(PreparedStatement.class, TypicalNames.PREPARED_STATEMENT)
                .addModifiers(TypicalModifiers.PUBLIC_FIELD)
                .build();
    }

    private FieldSpec resultSetField() {
        return FieldSpec.builder(ResultSet.class, TypicalNames.RESULT_SET)
                .addModifiers(TypicalModifiers.PUBLIC_FIELD)
                .build();
    }

    private FieldSpec metaDataField() {
        return FieldSpec.builder(ResultSetMetaData.class, TypicalNames.META_DATA)
                .addModifiers(TypicalModifiers.PUBLIC_FIELD)
                .build();
    }

    private FieldSpec columnCountField() {
        return FieldSpec.builder(int.class, TypicalNames.COLUMN_COUNT)
                .addModifiers(TypicalModifiers.PUBLIC_FIELD)
                .build();
    }

}
