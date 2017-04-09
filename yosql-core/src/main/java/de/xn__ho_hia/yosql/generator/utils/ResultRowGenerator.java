/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.utils;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.yosql.generator.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.TypeWriter;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;

@SuppressWarnings({ "nls", "javadoc" })
public class ResultRowGenerator {

    private final AnnotationGenerator    annotations;
    private final TypeWriter             typeWriter;
    private final ExecutionConfiguration configuration;

    @Inject
    public ResultRowGenerator(
            final AnnotationGenerator annotations,
            final TypeWriter typeWriter,
            final ExecutionConfiguration configuration) {
        this.annotations = annotations;
        this.typeWriter = typeWriter;
        this.configuration = configuration;
    }

    public void generateResultRowClass() {
        final String packageName = configuration.basePackageName() + "." + configuration.utilityPackageName();
        final TypeSpec type = TypicalTypes.publicClass(configuration.getResultRowClass())
                .addField(row())
                .addMethod(constructor())
                .addMethod(setColumnValue())
                .addMethod(toStringMethod())
                .addAnnotations(annotations.generatedClass(ResultRowGenerator.class))
                .build();
        typeWriter.writeType(configuration.outputBaseDirectory(), packageName, type);
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
