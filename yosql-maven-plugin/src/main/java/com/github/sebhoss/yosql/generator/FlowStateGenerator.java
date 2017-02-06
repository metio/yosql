package com.github.sebhoss.yosql.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.PluginRuntimeConfig;
import com.github.sebhoss.yosql.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.helpers.TypicalMethods;
import com.github.sebhoss.yosql.helpers.TypicalModifiers;
import com.github.sebhoss.yosql.helpers.TypicalNames;
import com.github.sebhoss.yosql.helpers.TypicalParameters;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
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
        final String packageName = runtimeConfig.getBasePackageName() + "." + runtimeConfig.getUtilityPackageName();
        final ClassName superclass = ClassName.get(packageName, ResultStateGenerator.RESULT_STATE_CLASS_NAME);
        final TypeSpec type = TypeSpec.classBuilder(FLOW_STATE_CLASS_NAME)
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .superclass(superclass)
                .addFields(fields())
                .addMethods(methods())
                .addAnnotation(commonGenerator.generatedAnnotation(FlowStateGenerator.class))
                .build();
        typeWriter.writeType(runtimeConfig.getOutputBaseDirectory().toPath(), packageName, type);
        runtimeConfig.getLogger().info(String.format("Generated [%s.%s]", packageName, FLOW_STATE_CLASS_NAME));
    }

    private Iterable<FieldSpec> fields() {
        final List<FieldSpec> fields = new ArrayList<>();
        fields.add(connectionField());
        fields.add(preparedStatementField());
        return fields;
    }

    private FieldSpec connectionField() {
        return FieldSpec.builder(Connection.class, TypicalNames.CONNECTION)
                .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                .build();
    }

    private FieldSpec preparedStatementField() {
        return FieldSpec.builder(PreparedStatement.class, TypicalNames.PREPARED_STATEMENT)
                .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                .build();
    }

    private Iterable<MethodSpec> methods() {
        final List<MethodSpec> fields = new ArrayList<>();
        fields.add(constructor());
        fields.add(close());
        return fields;
    }

    private MethodSpec constructor() {
        return TypicalMethods.constructor()
                .addParameter(TypicalParameters.connection())
                .addParameter(TypicalParameters.preparedStatement())
                .addParameter(TypicalParameters.resultSet())
                .addParameter(TypicalParameters.metaData())
                .addParameter(TypicalParameters.columnCount())
                .addStatement("super($N, $N, $N)", TypicalNames.RESULT_SET, TypicalNames.META_DATA,
                        TypicalNames.COLUMN_COUNT)
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.CONNECTION))
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.PREPARED_STATEMENT))
                .build();
    }

    private MethodSpec close() {
        return TypicalMethods.publicMethod("close")
                .returns(void.class)
                .addException(SQLException.class)
                .addStatement("$N.close()", TypicalNames.RESULT_SET)
                .addStatement("$N.close()", TypicalNames.PREPARED_STATEMENT)
                .addStatement("$N.close()", TypicalNames.CONNECTION)
                .build();
    }

}
