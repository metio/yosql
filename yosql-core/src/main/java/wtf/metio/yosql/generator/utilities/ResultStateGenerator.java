/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.utilities;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.blocks.api.Classes;
import wtf.metio.yosql.generator.blocks.api.GenericBlocks;
import wtf.metio.yosql.generator.blocks.api.Methods;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcParameters;
import wtf.metio.yosql.model.annotations.Utilities;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.internal.ApplicationEvents;
import wtf.metio.yosql.model.sql.PackageTypeSpec;

import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ResultStateGenerator {

    private final LocLogger logger;
    private final RuntimeConfiguration runtime;
    private final AnnotationGenerator annotations;
    private final GenericBlocks blocks;
    private final Classes classes;
    private final Methods methods;
    private final JdbcParameters jdbcParameters;

    @Inject
    ResultStateGenerator(
            final @Utilities LocLogger logger,
            final RuntimeConfiguration runtime,
            final AnnotationGenerator annotations,
            final GenericBlocks blocks,
            final Classes classes,
            final Methods methods,
            final JdbcParameters jdbcParameters) {
        this.annotations = annotations;
        this.runtime = runtime;
        this.logger = logger;
        this.blocks = blocks;
        this.classes = classes;
        this.methods = methods;
        this.jdbcParameters = jdbcParameters;
    }

    PackageTypeSpec generateResultStateClass() {
        final var resultStateClass = runtime.getResultStateClass();
        final var type = classes.openClass(resultStateClass)
                .addFields(fields())
                .addMethods(methods())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(ApplicationEvents.TYPE_GENERATED, resultStateClass.packageName(),
                resultStateClass.simpleName());
        return new PackageTypeSpec(type, resultStateClass.packageName());
    }

    private Iterable<FieldSpec> fields() {
        return List.of(resultSetField(), metaDataField(), columnCountField());
    }

    private FieldSpec resultSetField() {
        return FieldSpec.builder(ResultSet.class, runtime.jdbcNames().resultSet())
                .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                .build();
    }

    private FieldSpec metaDataField() {
        return FieldSpec.builder(ResultSetMetaData.class, runtime.jdbcNames().metaData())
                .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                .build();
    }

    private FieldSpec columnCountField() {
        return FieldSpec.builder(int.class, runtime.jdbcNames().columnCount())
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
                .addCode(blocks.setFieldToSelf(jdbcNames.resultSet()))
                .addCode(blocks.setFieldToSelf(jdbcNames.metaData()))
                .addCode(blocks.setFieldToSelf(jdbcNames.columnCount()))
                .build();
    }

    private MethodSpec next() {
        return methods.publicMethod("next")
                .returns(boolean.class)
                .addException(SQLException.class)
                .addStatement("return $N.next()", jdbcNames.resultSet())
                .build();
    }

    private MethodSpec getColumnName() {
        return methods.publicMethod("getColumnName")
                .returns(String.class)
                .addParameter(jdbcParameters.index())
                .addException(SQLException.class)
                .addStatement("return $N.getColumnName($N)", jdbcNames.metaData(), jdbcNames.index())
                .build();
    }

    private MethodSpec getResultSet() {
        return methods.publicMethod("getResultSet")
                .returns(ResultSet.class)
                .addStatement("return $N", jdbcNames.resultSet())
                .build();
    }

    private MethodSpec getColumnCount() {
        return methods.publicMethod("getColumnCount")
                .returns(int.class)
                .addStatement("return $N", jdbcNames.columnCount())
                .build();
    }

}
