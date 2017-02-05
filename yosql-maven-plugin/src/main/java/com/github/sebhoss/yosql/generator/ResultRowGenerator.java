package com.github.sebhoss.yosql.generator;

import java.util.LinkedHashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.PluginRuntimeConfig;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class ResultRowGenerator {

    public static final String        RESULT_ROW_CLASS_NAME = "ResultRow";

    private final CommonGenerator     commonGenerator;
    private final TypeWriter          typeWriter;
    private final PluginRuntimeConfig runtimeConfig;

    @Inject
    public ResultRowGenerator(
            final CommonGenerator commonGenerator,
            final TypeWriter typeWriter,
            final PluginRuntimeConfig runtimeConfig) {
        this.commonGenerator = commonGenerator;
        this.typeWriter = typeWriter;
        this.runtimeConfig = runtimeConfig;
    }

    public void generateResultRowClass() {
        final String packageName = runtimeConfig.getBasePackageName() + "." + runtimeConfig.getUtilityPackageName();
        final TypeSpec type = TypeSpec.classBuilder(RESULT_ROW_CLASS_NAME)
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .addField(row())
                .addMethod(constructor())
                .addMethod(setColumnValue())
                .addMethod(toStringMethod())
                .addAnnotation(commonGenerator.generatedAnnotation(ResultRowGenerator.class))
                .build();
        typeWriter.writeType(runtimeConfig.getOutputBaseDirectory().toPath(), packageName, type);
        runtimeConfig.getLogger()
                .info(String.format("Generated [%s.%s]", packageName, RESULT_ROW_CLASS_NAME));
    }

    private FieldSpec row() {
        return TypicalFields.privateField(TypicalTypes.MAP_OF_STRING_AND_OBJECTS, TypicalNames.ROW);
    }

    private MethodSpec constructor() {
        return TypicalMethods.constructor()
                .addParameter(TypicalParameters.columnCount())
                .addStatement("$N = new $T<>($N)", TypicalNames.ROW, LinkedHashMap.class, TypicalNames.COLUMN_COUNT)
                .build();
    }

    private MethodSpec setColumnValue() {
        return TypicalMethods.publicMethod("setColumnValue")
                .addParameter(TypicalParameters.parameter(String.class, TypicalNames.NAME))
                .addParameter(TypicalParameters.parameter(Object.class, TypicalNames.VALUE))
                .returns(void.class)
                .addStatement("$N.put($N, $N)", TypicalNames.ROW, TypicalNames.NAME, TypicalNames.VALUE)
                .build();
    }

    private MethodSpec toStringMethod() {
        return TypicalMethods.implementation("toString")
                .returns(String.class)
                .addStatement("return $N.toString()", TypicalNames.ROW)
                .build();
    }

}
