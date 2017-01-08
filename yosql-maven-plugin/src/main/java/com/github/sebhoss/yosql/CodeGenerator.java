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
import java.util.Collection;
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

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

@Named
@Singleton
public class CodeGenerator {

    private static final ClassName string         = ClassName.get("java.lang", "String");
    private static final ClassName object         = ClassName.get("java.lang", "Object");
    private static final ClassName list           = ClassName.get("java.util", "List");
    private static final ClassName map            = ClassName.get("java.util", "Map");
    private static final ClassName stream         = ClassName.get("java.util.stream", "Stream");
    private static final TypeName  mapOfKeyValues = ParameterizedTypeName.get(map, string, object);
    private static final TypeName  listOfMaps     = ParameterizedTypeName.get(list, mapOfKeyValues);
    private static final TypeName  streamOfMaps   = ParameterizedTypeName.get(stream, mapOfKeyValues);

    private final PluginErrors     pluginErrors;

    @Inject
    public CodeGenerator(final PluginErrors pluginErrors) {
        this.pluginErrors = pluginErrors;
    }

    public void generateUtilities(final Path baseDirectory, final String packageName) {
        final TypeSpec repository = TypeSpec.classBuilder("NamedParameterStatement")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(namedParamFields())
                .addMethods(namedParamMethods())
                .addAnnotation(generatedAnnotation())
                .build();
        final JavaFile javaFile = JavaFile.builder(packageName, repository).build();

        try {
            javaFile.writeTo(baseDirectory);
        } catch (final IOException exception) {
            pluginErrors.add(exception);
        }
    }

    private Iterable<FieldSpec> namedParamFields() {
        return Stream.of(preparedStatementField(), fieldsField())
                .collect(Collectors.toList());
    }

    private static FieldSpec preparedStatementField() {
        return FieldSpec.builder(PreparedStatement.class, "preparedStatement")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    private static FieldSpec fieldsField() {
        final ClassName string = ClassName.get("java.lang", "String");
        final ClassName list = ClassName.get("java.util", "List");
        final ClassName arrayList = ClassName.get("java.util", "ArrayList");
        final TypeName listOfStrings = ParameterizedTypeName.get(list, string);
        return FieldSpec.builder(listOfStrings, "fields")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T<>()", arrayList)
                .build();
    }

    private Iterable<MethodSpec> namedParamMethods() {
        final Stream<MethodSpec> constructors = Stream.of(namedParamConstructor());
        final Stream<MethodSpec> methods = Stream.of(
                getPreparedStatement(), executeQuery(), setInt());
        return Stream.concat(constructors, methods)
                .collect(Collectors.toList());
    }

    private static MethodSpec namedParamConstructor() {
        final ClassName pattern = ClassName.get("java.util.regex", "Pattern");
        final ClassName matcher = ClassName.get("java.util.regex", "Matcher");
        final String regex = "(?<!')(:[\\w]*)(?!')";
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Connection.class, "connection", Modifier.FINAL)
                .addParameter(String.class, "sqlStatement", Modifier.FINAL)
                .addException(SQLException.class)
                .addStatement("$T pattern = $T.compile($S)", pattern, pattern, regex)
                .addStatement("$T matcher = $N.matcher($N)", matcher, "pattern", "sqlStatement")
                .beginControlFlow("while ($N.find())", "matcher")
                .addStatement("$N.add($N.group().substring(1))", "fields", "matcher")
                .endControlFlow()
                .addStatement("$N = $N.prepareStatement($N.replaceAll($N.pattern(), $S))",
                        "preparedStatement", "connection", "sqlStatement", "pattern", "?")
                .build();
    }

    private static MethodSpec getPreparedStatement() {
        return MethodSpec.methodBuilder("getPreparedStatement")
                .addModifiers(Modifier.PUBLIC)
                .returns(PreparedStatement.class)
                .addStatement("return $N", "preparedStatement")
                .build();
    }

    private static MethodSpec executeQuery() {
        return MethodSpec.methodBuilder("executeQuery")
                .addModifiers(Modifier.PUBLIC)
                .addException(SQLException.class)
                .returns(ResultSet.class)
                .addStatement("return $N.executeQuery()", "preparedStatement")
                .build();
    }

    private static MethodSpec setInt() {
        final ParameterizedTypeName indexType = ParameterizedTypeName.get(Collection.class, Integer.class);
        return MethodSpec.methodBuilder("setInt")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "name")
                .addParameter(int.class, "value")
                .addException(SQLException.class)
                .returns(void.class)
                .addStatement("$T indexes = new $T<>();", indexType, ArrayList.class)
                .beginControlFlow("for (int index = 0; index < $N.size(); index++)", "fields")
                .beginControlFlow("if ($N.get($N).equals($N))", "fields", "index", "name")
                .addStatement("$N.add($N + 1)", "indexes", "index")
                .endControlFlow()
                .endControlFlow()
                .beginControlFlow("if ($N.isEmpty())", "indexes")
                .addStatement("throw new $T($T.format($S, $N))", IllegalArgumentException.class, String.class,
                        "SQL statement doesn't contain the parameter '%s'", "name")
                .endControlFlow()
                .beginControlFlow("for (Integer jdbcIndex : $N)", "indexes")
                .addStatement("$N.setInt($N, $N)", "preparedStatement", "jdbcIndex", "value")
                .endControlFlow()
                .build();
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
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
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
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .initializer("$S", sqlStatement.getStatement())
                .build();
    }

    private static FieldSpec asDataSourceField() {
        return FieldSpec.builder(DataSource.class, "dataSource")
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
                .beginControlFlow("try ($T connection = $N.getConnection())", Connection.class, "dataSource")
                .addStatement("$T preparedStatement = $N.prepareStatement($N)", PreparedStatement.class, "connection",
                        constantSqlStatementField(configuration))
                .addStatement("return $N.executeQuery()", "preparedStatement")
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
                .beginControlFlow("try ($T connection = $N.getConnection())", Connection.class, "dataSource")
                .addStatement("$T preparedStatement = $N.prepareStatement($N)", PreparedStatement.class, "connection",
                        constantSqlStatementField(configuration))
                .addStatement("return $N.executeQuery()", "preparedStatement")
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
                        "try ($T connection = $N.getConnection(); $T preparedStatement = $N.prepareStatement($N))",
                        Connection.class, "dataSource", PreparedStatement.class, "connection",
                        constantSqlStatementField(configuration))
                .addStatement("final $T resultSet = $N.executeQuery()", ResultSet.class, "preparedStatement")
                .addStatement("final $T resultList = resultSetToList($N)", listOfMaps, "resultSet")
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
                        .beginControlFlow("if ($N.next())", "resultSet")
                        .addStatement("$N.accept(resultSetToMap($N, $N, $N))", "action", "resultSet", "metaData",
                                "columnCount")
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
                        .addStatement("$N.close()", "resultSet")
                        .addStatement("$N.close()", "preparedStatement")
                        .addStatement("$N.close()", "connection")
                        .endControlFlow()
                        .beginControlFlow("catch ($T exception)", SQLException.class)
                        .addStatement("throw new $T($N)", RuntimeException.class, "exception")
                        .endControlFlow()
                        .build())
                .build();
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(streamOfMaps)
                .addParameters(configuration.getParameterSpecs())
                .beginControlFlow("try")
                .addStatement("final $T connection = $N.getConnection()", Connection.class, "dataSource")
                .addStatement("final $T preparedStatement = $N.prepareStatement($N)", PreparedStatement.class,
                        "connection",
                        constantSqlStatementField(configuration))
                .addStatement("final $T resultSet = $N.executeQuery()", ResultSet.class, "preparedStatement")
                .addStatement("final $T metaData = $N.getMetaData()", ResultSetMetaData.class, "resultSet")
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
                .addParameter(ResultSet.class, "resultSet")
                .addException(SQLException.class)
                .returns(listOfMaps)
                .addStatement("final $T metaData = $N.getMetaData()", ResultSetMetaData.class, "resultSet")
                .addStatement("final int columnCount = $N.getColumnCount()", "metaData")
                .addStatement("$T list = new $T<>()", listOfMaps, ArrayList.class)
                .beginControlFlow("while ($N.next())", "resultSet")
                .addStatement("$N.add($N($N, $N, $N))", "list", "resultSetToMap", "resultSet", "metaData",
                        "columnCount")
                .endControlFlow()
                .addStatement("return $N", "list")
                .build();
    }

    private static MethodSpec resultSetToMap() {
        return MethodSpec.methodBuilder("resultSetToMap")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ResultSet.class, "resultSet")
                .addParameter(ResultSetMetaData.class, "metaData")
                .addParameter(int.class, "columnCount")
                .addException(SQLException.class)
                .returns(mapOfKeyValues)
                .addStatement("$T row = new $T<>($N)", mapOfKeyValues, HashMap.class, "columnCount")
                .beginControlFlow("for (int index = 1; index <= $N; index++)", "columnCount")
                .addStatement("$N.put($N.getColumnName($N), $N.getObject($N))", "row", "metaData", "index", "resultSet",
                        "index")
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
