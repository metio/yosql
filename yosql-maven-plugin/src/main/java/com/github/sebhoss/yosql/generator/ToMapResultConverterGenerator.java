package com.github.sebhoss.yosql.generator;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.PluginRuntimeConfig;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class ToMapResultConverterGenerator {

    public static final String        TO_MAP_RESULT_CONVERTER_CLASS_NAME = "ToMapResultConverter";

    private final CommonGenerator     commonGenerator;
    private final TypeWriter          typeWriter;
    private final PluginRuntimeConfig runtimeConfig;

    @Inject
    public ToMapResultConverterGenerator(
            final CommonGenerator commonGenerator,
            final TypeWriter typeWriter,
            final PluginRuntimeConfig runtimeConfig) {
        this.commonGenerator = commonGenerator;
        this.typeWriter = typeWriter;
        this.runtimeConfig = runtimeConfig;
    }

    public void generateToMapResultConverterClass() {
        final String packageName = runtimeConfig.getBasePackageName() + "." + runtimeConfig.getUtilityPackageName();
        final TypeSpec type = TypeSpec.classBuilder(TO_MAP_RESULT_CONVERTER_CLASS_NAME)
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .addMethod(asUserType())
                .addAnnotation(commonGenerator.generatedAnnotation(ToMapResultConverterGenerator.class))
                .build();
        typeWriter.writeType(runtimeConfig.getOutputBaseDirectory().toPath(), packageName, type);
        runtimeConfig.getLogger()
                .info(String.format("Generated [%s.%s]", packageName, TO_MAP_RESULT_CONVERTER_CLASS_NAME));
    }

    private MethodSpec asUserType() {
        return TypicalMethods.publicMethod("asUserType")
                .addParameters(TypicalParameters.resultState(runtimeConfig.getResultStateClass()))
                .addException(SQLException.class)
                .returns(TypicalTypes.MAP_OF_STRING_AND_OBJECTS)
                .addStatement("final $T $N = new $T<>($N.getColumnCount())", TypicalTypes.MAP_OF_STRING_AND_OBJECTS,
                        TypicalNames.ROW, LinkedHashMap.class, TypicalNames.RESULT)
                .beginControlFlow("for (int $N = 1; $N <= $N.getColumnCount(); $N++)",
                        TypicalNames.INDEX, TypicalNames.INDEX, TypicalNames.RESULT, TypicalNames.INDEX)
                .addStatement("$N.put($N.getColumnName($N), $N.getResultSet().getObject($N))", TypicalNames.ROW,
                        TypicalNames.RESULT, TypicalNames.INDEX, TypicalNames.RESULT, TypicalNames.INDEX)
                .endControlFlow()
                .addStatement("return $N", TypicalNames.ROW)
                .build();
    }

}
