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
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.lifecycle.CodegenLifecycle;
import wtf.metio.yosql.codegen.logging.Utilities;
import wtf.metio.yosql.dao.jdbc.JdbcParameters;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Inject;
import java.util.LinkedHashMap;

public class ResultRowGenerator {

    private final LocLogger logger;
    private final ConverterConfiguration converters;
    private final NamesConfiguration names;
    private final AnnotationGenerator annotations;
    private final Classes classes;
    private final Fields fields;
    private final Methods methods;
    private final Parameters parameters;
    private final JdbcParameters jdbcParameters;

    @Inject
    public ResultRowGenerator(
            final @Utilities LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Fields fields,
            final Methods methods,
            final Parameters parameters,
            final JdbcParameters jdbcParameters) {
        this.names = runtimeConfiguration.names();
        this.converters = runtimeConfiguration.converter();
        this.annotations = annotations;
        this.logger = logger;
        this.classes = classes;
        this.fields = fields;
        this.methods = methods;
        this.parameters = parameters;
        this.jdbcParameters = jdbcParameters;
    }

    PackagedTypeSpec generateResultRowClass() {
        final var resultRowClass = converters.resultRowClass();
        final var type = classes.publicClass(resultRowClass)
                .addField(row())
                .addMethod(constructor())
                .addMethod(setColumnValue())
                .addMethod(getColumnValue())
                .addMethod(toStringMethod())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, resultRowClass.packageName(),
                resultRowClass.simpleName());
        return PackagedTypeSpec.of(type, resultRowClass.packageName());
    }

    private FieldSpec row() {
        return fields.field(TypicalTypes.MAP_OF_STRING_AND_OBJECTS, names.row());
    }

    private MethodSpec constructor() {
        return methods.constructor()
                .addParameter(jdbcParameters.columnCount())
                .addStatement("$N = new $T<>($N)",
                        names.row(),
                        LinkedHashMap.class,
                        names.columnCount())
                .build();
    }

    private MethodSpec setColumnValue() {
        return methods.publicMethod("setColumnValue")
                .addParameter(parameters.parameter(String.class, names.name()))
                .addParameter(parameters.parameter(Object.class, names.value()))
                .returns(void.class)
                .addStatement("$N.put($N, $N)",
                        names.row(),
                        names.name(),
                        names.value())
                .build();
    }

    private MethodSpec getColumnValue() {
        return methods.publicMethod("getColumnValue")
                .addParameter(parameters.parameter(String.class, names.name()))
                .returns(Object.class)
                .addStatement("return $N.get($N)",
                        names.row(),
                        names.name())
                .build();
    }

    private MethodSpec toStringMethod() {
        return methods.implementation("toString")
                .returns(String.class)
                .addStatement("return $N.toString()", names.row())
                .build();
    }

}
