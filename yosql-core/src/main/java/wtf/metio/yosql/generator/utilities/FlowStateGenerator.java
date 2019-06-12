/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.utilities;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.model.annotations.Utilities;
import wtf.metio.yosql.model.ApplicationEvents;
import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.model.PackageTypeSpec;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.helpers.*;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class FlowStateGenerator {

    private final AnnotationGenerator annotations;
    private final ExecutionConfiguration configuration;
    private final LocLogger              logger;

    @Inject
    FlowStateGenerator(
            final AnnotationGenerator annotations,
            final ExecutionConfiguration configuration,
            final @Utilities LocLogger logger) {
        this.annotations = annotations;
        this.configuration = configuration;
        this.logger = logger;
    }

    public PackageTypeSpec generateFlowStateClass() {
        final ClassName flowStateClass = configuration.getFlowStateClass();
        final TypeSpec type = TypicalTypes.publicClass(flowStateClass)
                .superclass(configuration.getResultStateClass())
                .addFields(fields())
                .addMethods(methods())
                .addAnnotations(annotations.generatedClass(FlowStateGenerator.class))
                .build();
        logger.debug(ApplicationEvents.TYPE_GENERATED, flowStateClass.packageName(),
                flowStateClass.simpleName());
        return new PackageTypeSpec(type, flowStateClass.packageName());
    }

    private static Iterable<FieldSpec> fields() {
        final List<FieldSpec> fields = new ArrayList<>();
        fields.add(connectionField());
        fields.add(preparedStatementField());
        return fields;
    }

    private static FieldSpec connectionField() {
        return FieldSpec.builder(Connection.class, TypicalNames.CONNECTION)
                .addModifiers(TypicalModifiers.privateField())
                .build();
    }

    private static FieldSpec preparedStatementField() {
        return FieldSpec.builder(PreparedStatement.class, TypicalNames.STATEMENT)
                .addModifiers(TypicalModifiers.privateField())
                .build();
    }

    private static Iterable<MethodSpec> methods() {
        final List<MethodSpec> fields = new ArrayList<>();
        fields.add(constructor());
        fields.add(close());
        return fields;
    }

    private static MethodSpec constructor() {
        return TypicalMethods.constructor()
                .addParameter(TypicalParameters.connection())
                .addParameter(TypicalParameters.preparedStatement())
                .addParameter(TypicalParameters.resultSet())
                .addParameter(TypicalParameters.metaData())
                .addParameter(TypicalParameters.columnCount())
                .addStatement("super($N, $N, $N)", TypicalNames.RESULT_SET, TypicalNames.META_DATA,
                        TypicalNames.COLUMN_COUNT)
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.CONNECTION))
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.STATEMENT))
                .build();
    }

    private static MethodSpec close() {
        return TypicalMethods.publicMethod("close")
                .returns(void.class)
                .addException(SQLException.class)
                .addStatement("$N.close()", TypicalNames.RESULT_SET)
                .addStatement("$N.close()", TypicalNames.STATEMENT)
                .addStatement("$N.close()", TypicalNames.CONNECTION)
                .build();
    }

}
