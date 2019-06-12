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
import java.util.LinkedHashMap;

public final class ResultRowGenerator {

    private final AnnotationGenerator annotations;
    private final ExecutionConfiguration configuration;
    private final LocLogger              logger;

    @Inject
    ResultRowGenerator(
            final AnnotationGenerator annotations,
            final ExecutionConfiguration configuration,
            final @Utilities LocLogger logger) {
        this.annotations = annotations;
        this.configuration = configuration;
        this.logger = logger;
    }

    public PackageTypeSpec generateResultRowClass() {
        final ClassName resultRowClass = configuration.getResultRowClass();
        final TypeSpec type = TypicalTypes.publicClass(resultRowClass)
                .addField(row())
                .addMethod(constructor())
                .addMethod(setColumnValue())
                .addMethod(toStringMethod())
                .addAnnotations(annotations.generatedClass(ResultRowGenerator.class))
                .build();
        logger.debug(ApplicationEvents.TYPE_GENERATED, resultRowClass.packageName(),
                resultRowClass.simpleName());
        return new PackageTypeSpec(type, resultRowClass.packageName());
    }

    private static FieldSpec row() {
        return TypicalFields.privateField(TypicalTypes.MAP_OF_STRING_AND_OBJECTS, TypicalNames.ROW);
    }

    private static MethodSpec constructor() {
        return TypicalMethods.constructor()
                .addParameter(TypicalParameters.columnCount())
                .addStatement("$N = new $T<>($N)", TypicalNames.ROW, LinkedHashMap.class, TypicalNames.COLUMN_COUNT)
                .build();
    }

    private static MethodSpec setColumnValue() {
        return TypicalMethods.publicMethod("setColumnValue")
                .addParameter(TypicalParameters.parameter(String.class, TypicalNames.NAME))
                .addParameter(TypicalParameters.parameter(Object.class, TypicalNames.VALUE))
                .returns(void.class)
                .addStatement("$N.put($N, $N)", TypicalNames.ROW, TypicalNames.NAME, TypicalNames.VALUE)
                .build();
    }

    private static MethodSpec toStringMethod() {
        return TypicalMethods.implementation("toString")
                .returns(String.class)
                .addStatement("return $N.toString()", TypicalNames.ROW)
                .build();
    }

}
