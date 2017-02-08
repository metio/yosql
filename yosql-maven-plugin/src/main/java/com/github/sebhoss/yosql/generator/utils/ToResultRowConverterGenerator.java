package com.github.sebhoss.yosql.generator.utils;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.generator.AnnotationGenerator;
import com.github.sebhoss.yosql.generator.TypeWriter;
import com.github.sebhoss.yosql.generator.helpers.TypicalMethods;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalParameters;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.plugin.PluginConfig;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class ToResultRowConverterGenerator {

    public static final String        TO_RESULT_ROW_CONVERTER_CLASS_NAME = "ToResultRowConverter";

    private final AnnotationGenerator annotations;
    private final TypeWriter          typeWriter;
    private final PluginConfig        runtimeConfig;

    @Inject
    public ToResultRowConverterGenerator(
            final AnnotationGenerator annotations,
            final TypeWriter typeWriter,
            final PluginConfig runtimeConfig) {
        this.annotations = annotations;
        this.typeWriter = typeWriter;
        this.runtimeConfig = runtimeConfig;
    }

    public void generateToResultRowConverterClass() {
        final String packageName = runtimeConfig.getBasePackageName() + "." + runtimeConfig.getUtilityPackageName();
        final TypeSpec type = TypicalTypes.publicClass(TO_RESULT_ROW_CONVERTER_CLASS_NAME)
                .addMethod(asUserType())
                .addAnnotations(annotations.generatedClass(ToResultRowConverterGenerator.class))
                .build();
        typeWriter.writeType(runtimeConfig.getOutputBaseDirectory().toPath(), packageName, type);
    }

    private MethodSpec asUserType() {
        final ClassName resultRow = ClassName.get(
                runtimeConfig.getBasePackageName() + "." + runtimeConfig.getUtilityPackageName(),
                ResultRowGenerator.RESULT_ROW_CLASS_NAME);
        return TypicalMethods.publicMethod("asUserType")
                .addParameters(TypicalParameters.resultState(runtimeConfig.getResultStateClass()))
                .addException(SQLException.class)
                .returns(resultRow)
                .addStatement("final $T $N = new $T($N.getColumnCount())", resultRow,
                        TypicalNames.ROW, resultRow, TypicalNames.RESULT)
                .beginControlFlow("for (int $N = 1; $N <= $N.getColumnCount(); $N++)",
                        TypicalNames.INDEX, TypicalNames.INDEX, TypicalNames.RESULT, TypicalNames.INDEX)
                .addStatement("$N.setColumnValue($N.getColumnName($N), $N.getResultSet().getObject($N))",
                        TypicalNames.ROW, TypicalNames.RESULT, TypicalNames.INDEX, TypicalNames.RESULT,
                        TypicalNames.INDEX)
                .endControlFlow()
                .addStatement("return $N", TypicalNames.ROW)
                .build();
    }

}
