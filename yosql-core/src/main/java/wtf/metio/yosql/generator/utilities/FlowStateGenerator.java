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
import wtf.metio.yosql.generator.blocks.generic.Classes;
import wtf.metio.yosql.generator.blocks.generic.GenericBlocks;
import wtf.metio.yosql.generator.blocks.generic.Methods;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcParameters;
import wtf.metio.yosql.model.annotations.Utilities;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.internal.ApplicationEvents;
import wtf.metio.yosql.model.sql.PackagedTypeSpec;

import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

final class FlowStateGenerator {

    private final LocLogger logger;
    private final RuntimeConfiguration runtimeConfiguration;
    private final GenericBlocks blocks;
    private final AnnotationGenerator annotations;
    private final Classes classes;
    private final Methods methods;
    private final JdbcParameters jdbcParameters;

    @Inject
    FlowStateGenerator(
            final @Utilities LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final GenericBlocks blocks,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Methods methods,
            final JdbcParameters jdbcParameters) {
        this.logger = logger;
        this.runtimeConfiguration = runtimeConfiguration;
        this.blocks = blocks;
        this.annotations = annotations;
        this.classes = classes;
        this.methods = methods;
        this.jdbcParameters = jdbcParameters;
    }

    PackagedTypeSpec generateFlowStateClass() {
        final var flowStateClass = runtimeConfiguration.result().flowStateClass();
        final var type = classes.publicClass(flowStateClass)
                .superclass(runtimeConfiguration.result().flowStateClass())
                .addFields(fields())
                .addMethods(methods())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(ApplicationEvents.TYPE_GENERATED, flowStateClass.packageName(),
                flowStateClass.simpleName());
        return PackagedTypeSpec.of(type, flowStateClass.packageName());
    }

    private Iterable<FieldSpec> fields() {
        final var fields = new ArrayList<FieldSpec>();
        fields.add(connectionField());
        fields.add(preparedStatementField());
        return fields;
    }

    private FieldSpec connectionField() {
        return FieldSpec.builder(Connection.class, runtimeConfiguration.jdbc().connection())
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    private FieldSpec preparedStatementField() {
        return FieldSpec.builder(PreparedStatement.class, runtimeConfiguration.jdbc().statement())
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    private Iterable<MethodSpec> methods() {
        final var fields = new ArrayList<MethodSpec>();
        fields.add(constructor());
        fields.add(close());
        return fields;
    }

    private MethodSpec constructor() {
        return methods.constructor()
                .addParameter(jdbcParameters.connection())
                .addParameter(jdbcParameters.preparedStatement())
                .addParameter(jdbcParameters.resultSet())
                .addParameter(jdbcParameters.metaData())
                .addParameter(jdbcParameters.columnCount())
                .addStatement("super($N, $N, $N)",
                        runtimeConfiguration.jdbc().resultSet(),
                        runtimeConfiguration.jdbc().metaData(),
                        runtimeConfiguration.jdbc().columnCount())
                .addCode(blocks.initializeFieldToSelf(runtimeConfiguration.jdbc().connection()))
                .addCode(blocks.initializeFieldToSelf(runtimeConfiguration.jdbc().statement()))
                .build();
    }

    private MethodSpec close() {
        return methods.publicMethod("close")
                .returns(void.class)
                .addException(SQLException.class)
                .addStatement("$N.close()", runtimeConfiguration.jdbc().resultSet())
                .addStatement("$N.close()", runtimeConfiguration.jdbc().statement())
                .addStatement("$N.close()", runtimeConfiguration.jdbc().connection())
                .build();
    }

}
