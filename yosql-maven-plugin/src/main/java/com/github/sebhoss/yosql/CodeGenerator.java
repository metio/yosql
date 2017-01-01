package com.github.sebhoss.yosql;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.sql.DataSource;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class CodeGenerator {

    private final PluginErrors pluginErrors;

    @Inject
    public CodeGenerator(final PluginErrors pluginErrors) {
        this.pluginErrors = pluginErrors;
    }

    public void generateUtilities(final Path baseDirectory, final String packageName) {
        final TypeSpec repository = TypeSpec.classBuilder("NamedParameterStatement")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(namedParamFields())
                .addMethods(namedParamMethods())
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
        final ClassName pattern = ClassName.get("java.util.regex", "Pattern");
        final ClassName matcher = ClassName.get("java.util.regex", "Matcher");
        final String regex = "(?<!')(:[\\w]*)(?!')";
        return Stream.of(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Connection.class, "connection")
                .addParameter(String.class, "sqlStatement")
                .addException(SQLException.class)
                .addStatement("$T pattern = $T.compile($S)", pattern, pattern, regex)
                .addStatement("$T matcher = $N.matcher($N)", matcher, "pattern", "sqlStatement")
                .beginControlFlow("while ($N.find())", "matcher")
                .addStatement("$N.add($N.group().substring(1))", "fields", "matcher")
                .endControlFlow()
                .addStatement("$N = $N.prepareStatement($N.replaceAll($N.pattern(), $S))",
                        "preparedStatement", "connection", "sqlStatement", "pattern", "?")
                .build())
                .collect(Collectors.toList());
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
                .build();
        final JavaFile javaFile = JavaFile.builder(packageName, repository).build();

        try {
            javaFile.writeTo(baseDirectory);
        } catch (final IOException exception) {
            pluginErrors.add(exception);
        }
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
        return Stream.concat(singleQueries, Stream.concat(batchQueries, streamQueries))
                .collect(Collectors.toList());
    }

    private static MethodSpec singleQuery(final SqlStatement sqlStatement) {
        /**
         * <pre>
         * try (Connection connection = dataSource.getConnection()) {
         *     final PreparedStatement prepareStatement = connection.prepareStatement(QUERY_ALL_COMPANIES);
         *     final ResultSet resultSet = prepareStatement.executeQuery();
         *     resultSet.next();
         * } catch (final SQLException exception) {
         *     throw new RuntimeException(exception);
         * }
         * </pre>
         */
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return MethodSpec.methodBuilder(configuration.getName())
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(String[].class, "arguments")
                .addStatement("$T.out.println($N)", System.class, constantSqlStatementField(configuration))
                .build();
    }

    private static MethodSpec batchQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final String methodName = prefixedName(configuration.getBatchPrefix(), configuration.getName());
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(String[].class, "arguments")
                .addStatement("$T.out.println($N)", System.class, constantSqlStatementField(configuration))
                .build();
    }

    private static MethodSpec streamQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final String methodName = prefixedName(configuration.getStreamPrefix(), configuration.getName());
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(String[].class, "arguments")
                .addStatement("$T.out.println($N)", System.class, constantSqlStatementField(configuration))
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
