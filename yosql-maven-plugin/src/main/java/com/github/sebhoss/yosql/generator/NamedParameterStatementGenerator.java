package com.github.sebhoss.yosql.generator;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class NamedParameterStatementGenerator {

    private static final String   FIELDS          = "fields";

    private static final String   PARAMETER_REGEX = "(?<!')(:[\\w]*)(?!')";

    private final CommonGenerator commonGenerator;
    private final TypeWriter      typeWriter;

    @Inject
    public NamedParameterStatementGenerator(
            final CommonGenerator commonGenerator,
            final TypeWriter typeWriter) {
        this.commonGenerator = commonGenerator;
        this.typeWriter = typeWriter;
    }

    public void generateNamedParameterStatementClass(final Path baseDirectory, final String packageName) {
        final TypeSpec namedParamStatement = TypeSpec.classBuilder("NamedParameterStatement")
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .addFields(namedParamFields())
                .addMethods(namedParamMethods())
                .addAnnotation(commonGenerator.generatedAnnotation(NamedParameterStatementGenerator.class))
                .build();
        typeWriter.writeType(baseDirectory, packageName, namedParamStatement);
    }

    private Iterable<FieldSpec> namedParamFields() {
        return Stream.of(preparedStatementField(), fieldsField())
                .collect(Collectors.toList());
    }

    private FieldSpec preparedStatementField() {
        return FieldSpec.builder(PreparedStatement.class, TypicalNames.PREPARED_STATEMENT)
                .addModifiers(TypicalModifiers.PRIVATE_METHOD)
                .build();
    }

    private FieldSpec fieldsField() {
        return FieldSpec.builder(TypicalTypes.LIST_OF_STRINGS, FIELDS)
                .addModifiers(TypicalModifiers.PRIVATE_METHOD)
                .initializer("new $T<>()", TypicalTypes.ARRAY_LIST)
                .build();
    }

    private Iterable<MethodSpec> namedParamMethods() {
        final Stream<MethodSpec> constructors = Stream.of(namedParamConstructor());
        final Stream<MethodSpec> methods = Stream.of(
                getPreparedStatement(), executeQuery(), setObject());
        return Stream.concat(constructors, methods)
                .collect(Collectors.toList());
    }

    private static MethodSpec namedParamConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Connection.class, TypicalNames.CONNECTION, TypicalModifiers.PARAMETER)
                .addParameter(String.class, TypicalNames.SQL_STATEMENT, TypicalModifiers.PARAMETER)
                .addException(SQLException.class)
                .addStatement("final $T $N = $T.compile($S)", TypicalTypes.PATTERN, TypicalNames.PATTERN,
                        TypicalTypes.PATTERN, PARAMETER_REGEX)
                .addStatement("final $T $N = $N.matcher($N)", TypicalTypes.MATCHER, TypicalNames.MATCHER,
                        TypicalNames.PATTERN, TypicalNames.SQL_STATEMENT)
                .beginControlFlow("while ($N.find())", TypicalNames.MATCHER)
                .addStatement("$N.add($N.group().substring(1))", FIELDS, TypicalNames.MATCHER)
                .endControlFlow()
                .addStatement("$N = $N.prepareStatement($N.replaceAll($N.pattern(), $S))",
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION, TypicalNames.SQL_STATEMENT,
                        TypicalNames.PATTERN, "?")
                .build();
    }

    private static MethodSpec getPreparedStatement() {
        return MethodSpec.methodBuilder("getPreparedStatement")
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(PreparedStatement.class)
                .addStatement("return $N", TypicalNames.PREPARED_STATEMENT)
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
        final String name = "name";
        final String value = "value";
        final String index = "index";
        final String indeces = "indeces";
        final String jdbcIndex = "jdbcIndex";
        return MethodSpec.methodBuilder("setObject")
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .addParameter(String.class, name, TypicalModifiers.PARAMETER)
                .addParameter(Object.class, value, TypicalModifiers.PARAMETER)
                .addException(SQLException.class)
                .returns(void.class)
                .addStatement("$T $N = new $T<>();", TypicalTypes.COLLECTION_OF_INTEGERS, indeces, ArrayList.class)
                .beginControlFlow("for (int $N = 0; $N < $N.size(); $N++)", index, index, FIELDS, index)
                .beginControlFlow("if ($N.get($N).equals($N))", FIELDS, index, name)
                .addStatement("$N.add($N + 1)", indeces, index)
                .endControlFlow()
                .endControlFlow()
                .beginControlFlow("if ($N.isEmpty())", indeces)
                .addStatement("throw new $T($T.format($S, $N))", IllegalArgumentException.class, String.class,
                        "SQL statement doesn't contain the parameter '%s'", name)
                .endControlFlow()
                .beginControlFlow("for (Integer $N : $N)", jdbcIndex, indeces)
                .addStatement("$N.setObject($N, $N)", TypicalNames.PREPARED_STATEMENT, jdbcIndex, value)
                .endControlFlow()
                .build();
    }

}
