package com.github.sebhoss.yosql;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Generated;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.sql.DataSource;

import com.github.sebhoss.yosql.generator.NamedParameterStatementGenerator;
import com.github.sebhoss.yosql.generator.TypicalModifiers;
import com.github.sebhoss.yosql.generator.TypicalNames;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

@Named
@Singleton
public class CodeGenerator {

    private static final ClassName                 string         = ClassName.get("java.lang", "String");
    private static final ClassName                 object         = ClassName.get("java.lang", "Object");
    private static final ClassName                 list           = ClassName.get("java.util", "List");
    private static final ClassName                 map            = ClassName.get("java.util", "Map");
    private static final ClassName                 stream         = ClassName.get("java.util.stream", "Stream");
    private static final TypeName                  mapOfKeyValues = ParameterizedTypeName.get(map, string, object);
    private static final TypeName                  listOfMaps     = ParameterizedTypeName.get(list, mapOfKeyValues);
    private static final TypeName                  streamOfMaps   = ParameterizedTypeName.get(stream, mapOfKeyValues);

    private final PluginErrors                     pluginErrors;
    private final NamedParameterStatementGenerator namedParamStatementGenerator;

    @Inject
    public CodeGenerator(
            final NamedParameterStatementGenerator namedParamStatementGenerator,
            final PluginErrors pluginErrors) {
        this.namedParamStatementGenerator = namedParamStatementGenerator;
        this.pluginErrors = pluginErrors;
    }

    public void generateUtilities(final Path baseDirectory, final String packageName) {
        namedParamStatementGenerator.generateNamedParameterStatementClass(baseDirectory, packageName);
    }

    /**
     * Generates a single repository.
     *
     * @param baseDirectory
     *            The target path to the generated repository.
     * @param name
     * @param sqlStatements
     *            The SQL statements to be included in the repository.
     */
    public void generateRepository(final Path baseDirectory, final String fqnOfRepository,
            final List<SqlStatement> sqlStatements) {
        final String className = getClassName(fqnOfRepository);
        final String packageName = getPackageName(fqnOfRepository);
        final TypeSpec repository = TypeSpec.classBuilder(className)
                .addModifiers(TypicalModifiers.PUBLIC_CLASS)
                .addFields(asFields(sqlStatements))
                .addMethods(asMethods(sqlStatements))
                .addAnnotation(generatedAnnotation())
                .build();
        final JavaFile javaFile = JavaFile.builder(packageName, repository).build();

        try {
            javaFile.writeTo(baseDirectory);
        } catch (final IOException exception) {
            pluginErrors.add(exception);
        }
    }

    private static AnnotationSpec generatedAnnotation() {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", CodeGenerator.class.getName())
                .addMember("date", "$S", ZonedDateTime.now().toString())
                .addMember("comments", "$S", "DO NOT EDIT")
                .build();
    }

    private String getPackageName(final String fqnOfRepository) {
        return fqnOfRepository.substring(0, fqnOfRepository.lastIndexOf("."));
    }

    private String getClassName(final String fqnOfRepository) {
        return fqnOfRepository.substring(fqnOfRepository.lastIndexOf(".") + 1,
                fqnOfRepository.length());
    }

    private Iterable<FieldSpec> asFields(final List<SqlStatement> sqlStatements) {
        final Stream<FieldSpec> constants = sqlStatements.stream()
                .map(CodeGenerator::asConstantSqlField);
        final Stream<FieldSpec> fields = Stream.of(asDataSourceField());
        return Stream.concat(constants, fields)
                .collect(Collectors.toList());
    }

    private static FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return FieldSpec.builder(String.class, constantSqlStatementField(configuration))
                .addModifiers(TypicalModifiers.CONSTANT_FIELD)
                .initializer("$S", sqlStatement.getStatement())
                .build();
    }

    private static FieldSpec asDataSourceField() {
        return FieldSpec.builder(DataSource.class, TypicalNames.DATA_SOURCE)
                .addModifiers(Modifier.PRIVATE)
                .build();
    }

    private Iterable<MethodSpec> asMethods(final List<SqlStatement> sqlStatements) {
        final Stream<MethodSpec> singleQueries = sqlStatements.stream()
                .map(CodeGenerator::singleQuery);
        final Stream<MethodSpec> batchQueries = sqlStatements.stream()
                .map(CodeGenerator::batchQuery);
        final Stream<MethodSpec> streamQueries = Stream.concat(sqlStatements.stream()
                .map(CodeGenerator::streamQueryEager), sqlStatements.stream()
                        .map(CodeGenerator::streamQueryLazy));
        final Stream<MethodSpec> utilities = Stream.of(resultSetToList(), resultSetToMap());
        return Stream.concat(singleQueries, Stream.concat(batchQueries, Stream.concat(streamQueries, utilities)))
                .collect(Collectors.toList());
    }

    private static MethodSpec singleQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return MethodSpec.methodBuilder(configuration.getName())
                .addModifiers(Modifier.PUBLIC)
                .returns(ResultSet.class)
                .addParameters(configuration.getParameterSpecs())
                .beginControlFlow("try ($T $N = $N.getConnection())", Connection.class, TypicalNames.CONNECTION,
                        TypicalNames.DATA_SOURCE)
                .addStatement("$T $N = $N.prepareStatement($N)", PreparedStatement.class,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                        constantSqlStatementField(configuration))
                .addStatement("return $N.executeQuery()", TypicalNames.PREPARED_STATEMENT)
                .endControlFlow()
                .beginControlFlow("catch ($T exception)", SQLException.class)
                .addStatement("throw new $T(exception)", RuntimeException.class)
                .endControlFlow()
                .build();
    }

    private static MethodSpec batchQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final String methodName = prefixedName(configuration.getBatchPrefix(), configuration.getName());
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ResultSet.class)
                .addParameters(configuration.getParameterSpecs())
                .beginControlFlow("try ($T $N = $N.getConnection())", Connection.class, TypicalNames.CONNECTION,
                        TypicalNames.DATA_SOURCE)
                .addStatement("$T $N = $N.prepareStatement($N)", PreparedStatement.class,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                        constantSqlStatementField(configuration))
                .addStatement("return $N.executeQuery()", TypicalNames.PREPARED_STATEMENT)
                .endControlFlow()
                .beginControlFlow("catch ($T exception)", SQLException.class)
                .addStatement("throw new $T(exception)", RuntimeException.class)
                .endControlFlow()
                .build();
    }

    private static MethodSpec streamQueryEager(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final String methodName = prefixedName(configuration.getStreamPrefix() + "Eager", configuration.getName());
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(streamOfMaps)
                .addParameters(configuration.getParameterSpecs())
                .beginControlFlow(
                        "try ($T $N = $N.getConnection(); $T $N = $N.prepareStatement($N))",
                        Connection.class, TypicalNames.CONNECTION, TypicalNames.DATA_SOURCE, PreparedStatement.class,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                        constantSqlStatementField(configuration))
                .addStatement("final $T $N = $N.executeQuery()", ResultSet.class, TypicalNames.RESULT_SET,
                        TypicalNames.PREPARED_STATEMENT)
                .addStatement("final $T resultList = resultSetToList($N)", listOfMaps, TypicalNames.RESULT_SET)
                .addStatement("return $N.stream()", "resultList")
                .endControlFlow()
                .beginControlFlow("catch (final $T exception)", SQLException.class)
                .addStatement("throw new $T(exception)", RuntimeException.class)
                .endControlFlow()
                .build();
    }

    private static MethodSpec streamQueryLazy(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final String methodName = prefixedName(configuration.getStreamPrefix() + "Lazy", configuration.getName());
        final ParameterizedTypeName map = ParameterizedTypeName.get(Map.class, String.class, Object.class);
        final ClassName spliteratorClass = ClassName.get(Spliterators.AbstractSpliterator.class);
        final ParameterizedTypeName superinterface = ParameterizedTypeName.get(spliteratorClass, map);
        final ClassName consumerClass = ClassName.get(Consumer.class);
        final ParameterizedTypeName consumerType = ParameterizedTypeName.get(consumerClass,
                WildcardTypeName.supertypeOf(map));
        final TypeSpec spliterator = TypeSpec
                .anonymousClassBuilder("$T.MAX_VALUE, $T.ORDERED", Long.class, Spliterator.class)
                .addSuperinterface(superinterface)
                .addMethod(MethodSpec.methodBuilder("tryAdvance")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(consumerType, "action")
                        .returns(boolean.class)
                        .beginControlFlow("try")
                        .beginControlFlow("if ($N.next())", TypicalNames.RESULT_SET)
                        .addStatement("$N.accept(resultSetToMap($N, $N, $N))", "action", TypicalNames.RESULT_SET,
                                "metaData", "columnCount")
                        .addStatement("return true")
                        .endControlFlow()
                        .addStatement("return false")
                        .endControlFlow()
                        .beginControlFlow("catch (final $T exception)", SQLException.class)
                        .addStatement("throw new $T($N)", RuntimeException.class, "exception")
                        .endControlFlow()
                        .build())
                .build();
        final TypeSpec closer = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(MethodSpec.methodBuilder("run")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(void.class)
                        .beginControlFlow("try")
                        .addStatement("$N.close()", TypicalNames.RESULT_SET)
                        .addStatement("$N.close()", TypicalNames.PREPARED_STATEMENT)
                        .addStatement("$N.close()", TypicalNames.CONNECTION)
                        .endControlFlow()
                        .beginControlFlow("catch ($T exception)", SQLException.class)
                        .addStatement("throw new $T($N)", RuntimeException.class, "exception")
                        .endControlFlow()
                        .build())
                .build();
        final Builder queryMethodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(streamOfMaps)
                .addParameters(configuration.getParameterSpecs())
                .beginControlFlow("try")
                .addStatement("final $T $N = $N.getConnection()", Connection.class, TypicalNames.CONNECTION,
                        TypicalNames.DATA_SOURCE)
                .addStatement("final $T $N = $N.prepareStatement($N)", PreparedStatement.class,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                        constantSqlStatementField(configuration));
        if (configuration.getParameters() != null) {
            for (final SqlParameter parameter : configuration.getParameters()) {
                queryMethodBuilder
                        .addStatement("$N.setObject($N, $N)", "preparedStatement", "index", parameter.getName());
            }
        }
        return queryMethodBuilder
                .addStatement("final $T resultSet = $N.executeQuery()", ResultSet.class, "preparedStatement")
                .addStatement("final $T metaData = $N.getMetaData()", ResultSetMetaData.class, TypicalNames.RESULT_SET)
                .addStatement("final int columnCount = $N.getColumnCount()", "metaData")
                .addStatement("return $T.stream($L, false).onClose($L)",
                        StreamSupport.class, spliterator, closer)
                .endControlFlow()
                .beginControlFlow("catch ($T exception)", SQLException.class)
                .addStatement("throw new $T(exception)", RuntimeException.class)
                .endControlFlow()
                .build();
    }

    private static MethodSpec resultSetToList() {
        return MethodSpec.methodBuilder("resultSetToList")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ResultSet.class, TypicalNames.RESULT_SET)
                .addException(SQLException.class)
                .returns(listOfMaps)
                .addStatement("final $T metaData = $N.getMetaData()", ResultSetMetaData.class, TypicalNames.RESULT_SET)
                .addStatement("final int columnCount = $N.getColumnCount()", "metaData")
                .addStatement("$T list = new $T<>()", listOfMaps, ArrayList.class)
                .beginControlFlow("while ($N.next())", TypicalNames.RESULT_SET)
                .addStatement("$N.add($N($N, $N, $N))", "list", "resultSetToMap", TypicalNames.RESULT_SET, "metaData",
                        "columnCount")
                .endControlFlow()
                .addStatement("return $N", "list")
                .build();
    }

    private static MethodSpec resultSetToMap() {
        return MethodSpec.methodBuilder("resultSetToMap")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ResultSet.class, TypicalNames.RESULT_SET)
                .addParameter(ResultSetMetaData.class, "metaData")
                .addParameter(int.class, "columnCount")
                .addException(SQLException.class)
                .returns(mapOfKeyValues)
                .addStatement("$T row = new $T<>($N)", mapOfKeyValues, HashMap.class, "columnCount")
                .beginControlFlow("for (int index = 1; index <= $N; index++)", "columnCount")
                .addStatement("$N.put($N.getColumnName($N), $N.getObject($N))", "row", "metaData", "index",
                        TypicalNames.RESULT_SET, "index")
                .endControlFlow()
                .addStatement("return $N", "row")
                .build();
    }

    private static String constantSqlStatementField(final SqlStatementConfiguration configuration) {
        return configuration.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }

    private static String prefixedName(final String prefix, final String name) {
        return prefix
                + name.substring(0, 1).toUpperCase()
                + name.substring(1, name.length());
    }

}
