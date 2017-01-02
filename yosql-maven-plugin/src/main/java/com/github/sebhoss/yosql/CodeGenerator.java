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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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
                getPreparedStatement(), executeQuery(), setInt(), getIndex());
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
        return MethodSpec.methodBuilder("setInt")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "name")
                .addParameter(int.class, "value")
                .addException(SQLException.class)
                .returns(void.class)
                .addStatement("$N.setInt(getIndex($N), $N)", "preparedStatement", "name", "value")
                .build();
    }

    private static MethodSpec getIndex() {
        return MethodSpec.methodBuilder("getIndex")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(String.class, "name")
                .returns(int.class)
                .addStatement("return $N.indexOf($N) + 1", "fields", "name")
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
        final Stream<MethodSpec> streamQueries = sqlStatements.stream()
                .map(CodeGenerator::streamQuery);
        final Stream<MethodSpec> utilities = Stream.of(resultSetToList());
        return Stream.concat(singleQueries, Stream.concat(batchQueries, Stream.concat(streamQueries, utilities)))
                .collect(Collectors.toList());
    }

    private static MethodSpec singleQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return MethodSpec.methodBuilder(configuration.getName())
                .addModifiers(Modifier.PUBLIC)
                .returns(ResultSet.class)
                .addParameters(asParameters(configuration.getParameters()))
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
                .addParameters(asParameters(configuration.getParameters()))
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

    /**
     * <pre>
     * public Stream<Map<String, Object>> streamQueryWithoutFrontMatter() {
     *     try (Connection connection = dataSource.getConnection();
     *             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_WITHOUT_FRONT_MATTER)) {
     *         final ResultSet resultSet = preparedStatement.executeQuery();
     * 
     *         StreamSupport
     *                 .stream(new Spliterators.AbstractSpliterator<Map<String, Object>>(
     *                         Long.MAX_VALUE, Spliterator.ORDERED) {
     *                     &#64;Override
     *                     public boolean tryAdvance(final Consumer<? super Map<String, Object>> action) {
     *                         try {
     *                             if (!resultSet.next()) {
     *                                 return false;
     *                             }
     *                             action.accept(resultSetToMap(resultSet));
     *                             return true;
     *                         } catch (final SQLException exception) {
     *                             throw new RuntimeException(exception);
     *                         }
     *                     }
     *                 }, false);
     * 
     *         final List<Map<String, Object>> resultList = resultSetToList(resultSet);
     *         return resultList.stream();
     *     } catch (final SQLException exception) {
     *         throw new RuntimeException(exception);
     *     }
     * }
     * 
     * private static List<Map<String, Object>> resultSetToList(final ResultSet resultSet) throws SQLException {
     *     final List<Map<String, Object>> list = new ArrayList<>();
     *     while (resultSet.next()) {
     *         final Map<String, Object> row = resultSetToMap(resultSet);
     *         list.add(row);
     *     }
     *     return list;
     * }
     * 
     * private static Map<String, Object> resultSetToMap(final ResultSet resultSet) throws SQLException {
     *     final ResultSetMetaData metaData = resultSet.getMetaData();
     *     final int columnCount = metaData.getColumnCount();
     *     final Map<String, Object> row = new HashMap<>(columnCount);
     *     for (int index = 1; index <= columnCount; index++) {
     *         row.put(metaData.getColumnName(index), resultSet.getObject(index));
     *     }
     *     return row;
     * }
     * </pre>
     */

    private static MethodSpec streamQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final String methodName = prefixedName(configuration.getStreamPrefix(), configuration.getName());
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(streamOfMaps)
                .addParameters(asParameters(configuration.getParameters()))
                .beginControlFlow(
                        "try ($T connection = $N.getConnection(); $T preparedStatement = $N.prepareStatement($N))",
                        Connection.class, "dataSource", PreparedStatement.class, "connection",
                        constantSqlStatementField(configuration))
                .addStatement("$T resultSet = $N.executeQuery()", ResultSet.class, "preparedStatement")
                .addStatement("$T resultList = resultSetToList($N)", listOfMaps, "resultSet")
                .addStatement("return $N.stream()", "resultList")
                .endControlFlow()
                .beginControlFlow("catch ($T exception)", SQLException.class)
                .addStatement("throw new $T(exception)", RuntimeException.class)
                .endControlFlow()
                .build();
    }

    private static Iterable<ParameterSpec> asParameters(final List<SqlParameter> parameters) {
        return Optional.ofNullable(parameters)
                .map(params -> params.stream()
                        .map(param -> {
                            final TypeName type = ClassName.bestGuess(param.getType());
                            return ParameterSpec.builder(type, param.getName(), Modifier.FINAL).build();
                        })
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private static MethodSpec resultSetToList() {
        return MethodSpec.methodBuilder("resultSetToList")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ResultSet.class, "resultSet")
                .addException(SQLException.class)
                .returns(listOfMaps)
                .addStatement("$T metaData = $N.getMetaData()", ResultSetMetaData.class, "resultSet")
                .addStatement("int columnCount = $N.getColumnCount()", "metaData")
                .addStatement("$T list = new $T<>()", listOfMaps, ArrayList.class)
                .beginControlFlow("while ($N.next())", "resultSet")
                .addStatement("$T row = new $T<>($N)", mapOfKeyValues, HashMap.class, "columnCount")
                .beginControlFlow("for (int index = 1; index <= $N; index++)", "columnCount")
                .addStatement("$N.put($N.getColumnName($N), $N.getObject($N))", "row", "metaData", "index", "resultSet",
                        "index")
                .endControlFlow()
                .addStatement("$N.add($N)", "list", "row")
                .endControlFlow()
                .addStatement("return $N", "list")
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
