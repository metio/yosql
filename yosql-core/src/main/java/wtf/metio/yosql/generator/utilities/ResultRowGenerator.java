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
import wtf.metio.yosql.generator.blocks.api.*;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcParameters;
import wtf.metio.yosql.generator.helpers.TypicalTypes;
import wtf.metio.yosql.model.annotations.Utilities;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.internal.ApplicationEvents;
import wtf.metio.yosql.model.sql.PackageTypeSpec;

import javax.inject.Inject;
import java.util.LinkedHashMap;

public final class ResultRowGenerator {

    private final LocLogger logger;
    private final RuntimeConfiguration runtime;
    private final Names names;
    private final AnnotationGenerator annotations;
    private final Classes classes;
    private final Fields fields;
    private final Methods methods;
    private final Parameters parameters;
    private final JdbcParameters jdbcParameters;

    @Inject
    ResultRowGenerator(
            final @Utilities LocLogger logger,
            final RuntimeConfiguration runtime,
            final Names names,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Fields fields,
            final Methods methods,
            final Parameters parameters,
            final JdbcParameters jdbcParameters) {
        this.names = names;
        this.annotations = annotations;
        this.runtime = runtime;
        this.logger = logger;
        this.classes = classes;
        this.fields = fields;
        this.methods = methods;
        this.parameters = parameters;
        this.jdbcParameters = jdbcParameters;
    }

    PackageTypeSpec generateResultRowClass() {
        final var resultRowClass = runtime.result().resultRowClass();
        final var type = classes.publicClass(resultRowClass)
                .addField(row())
                .addMethod(constructor())
                .addMethod(setColumnValue())
                .addMethod(toStringMethod())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(ApplicationEvents.TYPE_GENERATED, resultRowClass.packageName(),
                resultRowClass.simpleName());
        return new PackageTypeSpec(type, resultRowClass.packageName());
    }

    private FieldSpec row() {
        return fields.privateField(TypicalTypes.MAP_OF_STRING_AND_OBJECTS, runtime.jdbcNames().row());
    }

    private MethodSpec constructor() {
        return methods.constructor()
                .addParameter(jdbcParameters.columnCount())
                .addStatement("$N = new $T<>($N)",
                        runtime.jdbcNames().row(),
                        LinkedHashMap.class,
                        runtime.jdbcNames().columnCount())
                .build();
    }

    private MethodSpec setColumnValue() {
        return methods.publicMethod("setColumnValue")
                .addParameter(parameters.parameter(String.class, names.name()))
                .addParameter(parameters.parameter(Object.class, names.value()))
                .returns(void.class)
                .addStatement("$N.put($N, $N)",
                        runtime.jdbcNames().row(),
                        names.name(),
                        names.value())
                .build();
    }

    private MethodSpec toStringMethod() {
        return methods.implementation("toString")
                .returns(String.class)
                .addStatement("return $N.toString()", runtime.jdbcNames().row())
                .build();
    }

}
