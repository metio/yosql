/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.utilities;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalModifiers;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;

@SuppressWarnings("nls")
final class ResultStateGenerator {

    private final AnnotationGenerator    annotations;
    private final ExecutionConfiguration configuration;

    @Inject
    ResultStateGenerator(
            final AnnotationGenerator annotations,
            final ExecutionConfiguration configuration) {
        this.annotations = annotations;
        this.configuration = configuration;
    }

    public PackageTypeSpec generateResultStateClass() {
        final TypeSpec type = TypicalTypes.openClass(configuration.getResultStateClass())
                .addFields(fields())
                .addMethods(methods())
                .addAnnotations(annotations.generatedClass(ResultStateGenerator.class))
                .build();
        // TODO: replace w/ 'configuration.getResultStateClass().getPackageName()'
        final String packageName = configuration.basePackageName() + "." + configuration.utilityPackageName();
        // TODO: add logger w/ event 'ApplicationEvents.TYPE_GENERATED'
        return new PackageTypeSpec(type, packageName);
    }

    private static Iterable<FieldSpec> fields() {
        final List<FieldSpec> fields = new ArrayList<>();
        fields.add(resultSetField());
        fields.add(metaDataField());
        fields.add(columnCountField());
        return fields;
    }

    private static FieldSpec resultSetField() {
        return FieldSpec.builder(ResultSet.class, TypicalNames.RESULT_SET)
                .addModifiers(TypicalModifiers.protectedField())
                .build();
    }

    private static FieldSpec metaDataField() {
        return FieldSpec.builder(ResultSetMetaData.class, TypicalNames.META_DATA)
                .addModifiers(TypicalModifiers.protectedField())
                .build();
    }

    private static FieldSpec columnCountField() {
        return FieldSpec.builder(int.class, TypicalNames.COLUMN_COUNT)
                .addModifiers(TypicalModifiers.protectedField())
                .build();
    }

    private static Iterable<MethodSpec> methods() {
        final List<MethodSpec> methods = new ArrayList<>();
        methods.add(constructor());
        methods.add(next());
        methods.add(getColumnName());
        methods.add(getResultSet());
        methods.add(getColumnCount());
        return methods;
    }

    private static MethodSpec constructor() {
        return TypicalMethods.constructor()
                .addParameter(TypicalParameters.resultSet())
                .addParameter(TypicalParameters.metaData())
                .addParameter(TypicalParameters.columnCount())
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.RESULT_SET))
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.META_DATA))
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.COLUMN_COUNT))
                .build();
    }

    private static MethodSpec next() {
        return TypicalMethods.publicMethod("next")
                .returns(boolean.class)
                .addException(SQLException.class)
                .addStatement("return $N.next()", TypicalNames.RESULT_SET)
                .build();
    }

    private static MethodSpec getColumnName() {
        return TypicalMethods.publicMethod("getColumnName")
                .returns(String.class)
                .addParameter(TypicalParameters.index())
                .addException(SQLException.class)
                .addStatement("return $N.getColumnName($N)", TypicalNames.META_DATA, TypicalNames.INDEX)
                .build();
    }

    private static MethodSpec getResultSet() {
        return TypicalMethods.publicMethod("getResultSet")
                .returns(ResultSet.class)
                .addStatement("return $N", TypicalNames.RESULT_SET)
                .build();
    }

    private static MethodSpec getColumnCount() {
        return TypicalMethods.publicMethod("getColumnCount")
                .returns(int.class)
                .addStatement("return $N", TypicalNames.COLUMN_COUNT)
                .build();
    }

}
