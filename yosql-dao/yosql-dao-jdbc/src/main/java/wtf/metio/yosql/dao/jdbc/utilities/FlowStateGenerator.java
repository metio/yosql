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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FlowStateGenerator {

    private final LocLogger logger;
    private final JdbcConfiguration jdbcConfiguration;
    private final NamesConfiguration names;
    private final GenericBlocks blocks;
    private final AnnotationGenerator annotations;
    private final Classes classes;
    private final Methods methods;
    private final JdbcParameters jdbcParameters;

    @Inject
    public FlowStateGenerator(
            final @Utilities LocLogger logger,
            final JdbcConfiguration jdbcConfiguration,
            final NamesConfiguration names,
            final GenericBlocks blocks,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Methods methods,
            final JdbcParameters jdbcParameters) {
        this.logger = logger;
        this.jdbcConfiguration = jdbcConfiguration;
        this.names = names;
        this.blocks = blocks;
        this.annotations = annotations;
        this.classes = classes;
        this.methods = methods;
        this.jdbcParameters = jdbcParameters;
    }

    PackagedTypeSpec generateFlowStateClass() {
        final var flowStateClass = jdbcConfiguration.flowStateClass();
        final var type = classes.publicClass(flowStateClass)
                .superclass(jdbcConfiguration.resultStateClass())
                .addField(connectionField())
                .addField(preparedStatementField())
                .addMethod(constructor())
                .addMethod(close())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, flowStateClass.packageName(),
                flowStateClass.simpleName());
        return PackagedTypeSpec.of(type, flowStateClass.packageName());
    }

    private FieldSpec connectionField() {
        return FieldSpec.builder(Connection.class, names.connection())
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    private FieldSpec preparedStatementField() {
        return FieldSpec.builder(PreparedStatement.class, names.statement())
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    private MethodSpec constructor() {
        return methods.constructor()
                .addParameter(jdbcParameters.connection())
                .addParameter(jdbcParameters.preparedStatement())
                .addParameter(jdbcParameters.resultSet())
                .addParameter(jdbcParameters.metaData())
                .addParameter(jdbcParameters.columnCount())
                .addStatement("super($N, $N, $N)",
                        names.resultSet(),
                        names.resultSetMetaData(),
                        names.columnCount())
                .addCode(blocks.initializeFieldToSelf(names.connection()))
                .addCode(blocks.initializeFieldToSelf(names.statement()))
                .build();
    }

    private MethodSpec close() {
        return methods.publicMethod("close")
                .returns(void.class)
                .addException(SQLException.class)
                .addStatement("$N.close()", names.resultSet())
                .addStatement("$N.close()", names.statement())
                .addStatement("$N.close()", names.connection())
                .build();
    }

}
