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
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;

import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultStateGenerator {

    private final LocLogger logger;
    private final JdbcConfiguration jdbcConfiguration;
    private final NamesConfiguration names;
    private final AnnotationGenerator annotations;
    private final GenericBlocks blocks;
    private final Classes classes;
    private final Methods methods;
    private final JdbcParameters jdbcParameters;

    @Inject
    public ResultStateGenerator(
            final @Utilities LocLogger logger,
            final JdbcConfiguration jdbcConfiguration,
            final NamesConfiguration names,
            final AnnotationGenerator annotations,
            final GenericBlocks blocks,
            final Classes classes,
            final Methods methods,
            final JdbcParameters jdbcParameters) {
        this.names = names;
        this.annotations = annotations;
        this.jdbcConfiguration = jdbcConfiguration;
        this.logger = logger;
        this.blocks = blocks;
        this.classes = classes;
        this.methods = methods;
        this.jdbcParameters = jdbcParameters;
    }

    PackagedTypeSpec generateResultStateClass() {
        final var resultStateClass = jdbcConfiguration.resultStateClass();
        final var type = classes.openClass(resultStateClass)
                .addField(resultSetField())
                .addField(metaDataField())
                .addField(columnCountField())
                .addMethod(constructor())
                .addMethod(next())
                .addMethod(getColumnName())
                .addMethod(getResultSet())
                .addMethod(getColumnCount())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, resultStateClass.packageName(),
                resultStateClass.simpleName());
        return PackagedTypeSpec.of(type, resultStateClass.packageName());
    }

    private FieldSpec resultSetField() {
        return FieldSpec.builder(ResultSet.class, names.resultSet())
                .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                .build();
    }

    private FieldSpec metaDataField() {
        return FieldSpec.builder(ResultSetMetaData.class, names.resultSetMetaData())
                .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                .build();
    }

    private FieldSpec columnCountField() {
        return FieldSpec.builder(int.class, names.columnCount())
                .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                .build();
    }

    private MethodSpec constructor() {
        return methods.constructor()
                .addParameter(jdbcParameters.resultSet())
                .addParameter(jdbcParameters.metaData())
                .addParameter(jdbcParameters.columnCount())
                .addCode(blocks.initializeFieldToSelf(names.resultSet()))
                .addCode(blocks.initializeFieldToSelf(names.resultSetMetaData()))
                .addCode(blocks.initializeFieldToSelf(names.columnCount()))
                .build();
    }

    private MethodSpec next() {
        return methods.publicMethod("next")
                .returns(boolean.class)
                .addException(SQLException.class)
                .addStatement("return $N.next()", names.resultSet())
                .build();
    }

    private MethodSpec getColumnName() {
        return methods.publicMethod("getColumnName")
                .returns(String.class)
                .addParameter(jdbcParameters.index())
                .addException(SQLException.class)
                .addStatement("return $N.getColumnName($N)", names.resultSetMetaData(), names.indexVariable())
                .build();
    }

    private MethodSpec getResultSet() {
        return methods.publicMethod("getResultSet")
                .returns(ResultSet.class)
                .addStatement("return $N", names.resultSet())
                .build();
    }

    private MethodSpec getColumnCount() {
        return methods.publicMethod("getColumnCount")
                .returns(int.class)
                .addStatement("return $N", names.columnCount())
                .build();
    }

}
