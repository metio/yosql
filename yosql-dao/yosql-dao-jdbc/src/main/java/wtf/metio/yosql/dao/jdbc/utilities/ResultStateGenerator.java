/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc.utilities;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.api.AnnotationGenerator;
import wtf.metio.yosql.codegen.api.Classes;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.blocks.GenericBlocks;
import wtf.metio.yosql.codegen.lifecycle.CodegenLifecycle;
import wtf.metio.yosql.codegen.logging.Utilities;
import wtf.metio.yosql.dao.jdbc.JdbcParameters;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultStateGenerator {

    private final LocLogger logger;
    private final RuntimeConfiguration runtimeConfiguration;
    private final AnnotationGenerator annotations;
    private final GenericBlocks blocks;
    private final Classes classes;
    private final Methods methods;
    private final JdbcParameters jdbcParameters;

    @Inject
    public ResultStateGenerator(
            final @Utilities LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final AnnotationGenerator annotations,
            final GenericBlocks blocks,
            final Classes classes,
            final Methods methods,
            final JdbcParameters jdbcParameters) {
        this.annotations = annotations;
        this.runtimeConfiguration = runtimeConfiguration;
        this.logger = logger;
        this.blocks = blocks;
        this.classes = classes;
        this.methods = methods;
        this.jdbcParameters = jdbcParameters;
    }

    PackagedTypeSpec generateResultStateClass() {
        final var resultStateClass = runtimeConfiguration.jdbc().resultStateClass();
        final var type = classes.openClass(resultStateClass)
                .addFields(fields())
                .addMethods(methods())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, resultStateClass.packageName(),
                resultStateClass.simpleName());
        return PackagedTypeSpec.of(type, resultStateClass.packageName());
    }

    private Iterable<FieldSpec> fields() {
        return List.of(resultSetField(), metaDataField(), columnCountField());
    }

    private FieldSpec resultSetField() {
        return FieldSpec.builder(ResultSet.class, runtimeConfiguration.jdbc().resultSet())
                .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                .build();
    }

    private FieldSpec metaDataField() {
        return FieldSpec.builder(ResultSetMetaData.class, runtimeConfiguration.jdbc().resultSetMetaData())
                .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                .build();
    }

    private FieldSpec columnCountField() {
        return FieldSpec.builder(int.class, runtimeConfiguration.jdbc().columnCount())
                .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                .build();
    }

    private Iterable<MethodSpec> methods() {
        final var methods = new ArrayList<MethodSpec>();
        methods.add(constructor());
        methods.add(next());
        methods.add(getColumnName());
        methods.add(getResultSet());
        methods.add(getColumnCount());
        return methods;
    }

    private MethodSpec constructor() {
        return methods.constructor()
                .addParameter(jdbcParameters.resultSet())
                .addParameter(jdbcParameters.metaData())
                .addParameter(jdbcParameters.columnCount())
                .addCode(blocks.initializeFieldToSelf(runtimeConfiguration.jdbc().resultSet()))
                .addCode(blocks.initializeFieldToSelf(runtimeConfiguration.jdbc().resultSetMetaData()))
                .addCode(blocks.initializeFieldToSelf(runtimeConfiguration.jdbc().columnCount()))
                .build();
    }

    private MethodSpec next() {
        return methods.publicMethod("next")
                .returns(boolean.class)
                .addException(SQLException.class)
                .addStatement("return $N.next()", runtimeConfiguration.jdbc().resultSet())
                .build();
    }

    private MethodSpec getColumnName() {
        return methods.publicMethod("getColumnName")
                .returns(String.class)
                .addParameter(jdbcParameters.index())
                .addException(SQLException.class)
                .addStatement("return $N.getColumnName($N)", runtimeConfiguration.jdbc().resultSetMetaData(),
                        runtimeConfiguration.jdbc().indexVariable())
                .build();
    }

    private MethodSpec getResultSet() {
        return methods.publicMethod("getResultSet")
                .returns(ResultSet.class)
                .addStatement("return $N", runtimeConfiguration.jdbc().resultSet())
                .build();
    }

    private MethodSpec getColumnCount() {
        return methods.publicMethod("getColumnCount")
                .returns(int.class)
                .addStatement("return $N", runtimeConfiguration.jdbc().columnCount())
                .build();
    }

}
