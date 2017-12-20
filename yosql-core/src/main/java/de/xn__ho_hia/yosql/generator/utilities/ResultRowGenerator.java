/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.utilities;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;

@SuppressWarnings("nls")
final class ResultRowGenerator {

    private final AnnotationGenerator    annotations;
    private final ExecutionConfiguration configuration;

    @Inject
    ResultRowGenerator(
            final AnnotationGenerator annotations,
            final ExecutionConfiguration configuration) {
        this.annotations = annotations;
        this.configuration = configuration;
    }

    public PackageTypeSpec generateResultRowClass() {
        // TODO: replace w/ 'configuration.getResultRowClass().getPackageName()'
        final String packageName = configuration.basePackageName() + "." + configuration.utilityPackageName();
        final TypeSpec type = TypicalTypes.publicClass(configuration.getResultRowClass())
                .addField(row())
                .addMethod(constructor())
                .addMethod(setColumnValue())
                .addMethod(toStringMethod())
                .addAnnotations(annotations.generatedClass(ResultRowGenerator.class))
                .build();
        // TODO: add logger w/ event 'ApplicationEvents.TYPE_GENERATED'
        return new PackageTypeSpec(type, packageName);
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
