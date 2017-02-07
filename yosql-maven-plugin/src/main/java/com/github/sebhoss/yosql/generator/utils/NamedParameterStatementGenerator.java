package com.github.sebhoss.yosql.generator.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;

import com.github.sebhoss.yosql.generator.CommonGenerator;
import com.github.sebhoss.yosql.generator.TypeWriter;
import com.github.sebhoss.yosql.helpers.TypicalModifiers;
import com.github.sebhoss.yosql.helpers.TypicalNames;
import com.github.sebhoss.yosql.helpers.TypicalTypes;
import com.github.sebhoss.yosql.plugin.PluginRuntimeConfig;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class NamedParameterStatementGenerator {

    public static final String        NAMED_PARAMETER_STATEMENT_CLASS_NAME = "NamedParameterStatement";

    private static final String       PARAMETER_INDEX_MAPPING              = "paramToIndexMapping";

    private final CommonGenerator     commonGenerator;
    private final TypeWriter          typeWriter;
    private final PluginRuntimeConfig runtimeConfig;

    @Inject
    public NamedParameterStatementGenerator(
            final CommonGenerator commonGenerator,
            final TypeWriter typeWriter,
            final PluginRuntimeConfig runtimeConfig) {
        this.commonGenerator = commonGenerator;
        this.typeWriter = typeWriter;
        this.runtimeConfig = runtimeConfig;
    }

    public void generateNamedParameterStatementClass() {
        final TypeSpec namedParamStatement = TypeSpec.classBuilder(NAMED_PARAMETER_STATEMENT_CLASS_NAME)
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .addFields(namedParamFields())
                .addMethods(namedParamMethods())
                .addAnnotation(commonGenerator.generatedAnnotation(NamedParameterStatementGenerator.class))
                .build();
        typeWriter.writeType(runtimeConfig.getOutputBaseDirectory().toPath(), runtimeConfig.getUtilityPackageName(),
                namedParamStatement);
    }

    private Iterable<FieldSpec> namedParamFields() {
        return Stream.of(preparedStatementField(), fieldToIndexMappingField())
                .collect(Collectors.toList());
    }

    private FieldSpec preparedStatementField() {
        return FieldSpec.builder(PreparedStatement.class, TypicalNames.PREPARED_STATEMENT)
                .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                .build();
    }

    private FieldSpec fieldToIndexMappingField() {
        return FieldSpec.builder(TypicalTypes.MAP_OF_STRING_AND_NUMBERS, PARAMETER_INDEX_MAPPING)
                .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                .build();
    }

    private Iterable<MethodSpec> namedParamMethods() {
        final Stream<MethodSpec> constructors = Stream.of(namedParamConstructor());
        final Stream<MethodSpec> methods = Stream.of(executeQuery(), setObject());
        return Stream.concat(constructors, methods)
                .collect(Collectors.toList());
    }

    private static MethodSpec namedParamConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(PreparedStatement.class, TypicalNames.PREPARED_STATEMENT, TypicalModifiers.PARAMETER)
                .addParameter(TypicalTypes.MAP_OF_STRING_AND_NUMBERS, PARAMETER_INDEX_MAPPING,
                        TypicalModifiers.PARAMETER)
                .addStatement("this.$N = $N", TypicalNames.PREPARED_STATEMENT, TypicalNames.PREPARED_STATEMENT)
                .addStatement("this.$N = $N", PARAMETER_INDEX_MAPPING, PARAMETER_INDEX_MAPPING)
                .build();
    }

    private static MethodSpec executeQuery() {
        return MethodSpec.methodBuilder("executeQuery")
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .addException(SQLException.class)
                .returns(ResultSet.class)
                .addStatement("return $N.executeQuery()", TypicalNames.PREPARED_STATEMENT)
                .build();
    }

    private static MethodSpec setObject() {
        return MethodSpec.methodBuilder("setObject")
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .addParameter(String.class, TypicalNames.NAME, TypicalModifiers.PARAMETER)
                .addParameter(Object.class, TypicalNames.VALUE, TypicalModifiers.PARAMETER)
                .addException(SQLException.class)
                .returns(void.class)
                .beginControlFlow("if (!$N.containsKey($N))", PARAMETER_INDEX_MAPPING, TypicalNames.NAME)
                .addStatement("throw new $T($T.format($S, $N))", IllegalArgumentException.class, String.class,
                        "SQL statement doesn't contain the parameter '%s'", TypicalNames.NAME)
                .endControlFlow()
                .beginControlFlow("for (int $N : $N.get($N))", TypicalNames.JDBC_INDEX, PARAMETER_INDEX_MAPPING,
                        TypicalNames.NAME)
                .addStatement("$N.setObject($N, $N)", TypicalNames.PREPARED_STATEMENT, TypicalNames.JDBC_INDEX,
                        TypicalNames.VALUE)
                .endControlFlow()
                .build();
    }

}
