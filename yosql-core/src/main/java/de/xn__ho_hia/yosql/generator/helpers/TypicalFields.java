/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import java.lang.reflect.Type;

import javax.inject.Inject;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.FieldSpec.Builder;
import com.squareup.javapoet.TypeName;

import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.model.SqlConfiguration;

@SuppressWarnings({ "nls", "javadoc" })
public class TypicalFields {

    public static final FieldSpec privateField(final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addModifiers(TypicalModifiers.privateField())
                .build();
    }

    public static final String constantSqlStatementFieldName(final SqlConfiguration configuration) {
        return configuration.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase() + getVendor(configuration);
    }

    public static final String constantRawSqlStatementFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + "_RAW" + getVendor(configuration);
    }

    public static final String constantSqlStatementParameterIndexFieldName(
            final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + "_PARAMETER_INDEX";
    }

    private static final String getVendor(final SqlConfiguration configuration) {
        return configuration.getVendor() == null ? "" : "_" + configuration.getVendor().replace(" ", "_").toUpperCase();
    }

    private final AnnotationGenerator annotations;

    @Inject
    public TypicalFields(final AnnotationGenerator annotations) {
        this.annotations = annotations;
    }

    public final FieldSpec field(final Class<?> generatorClass, final Type type, final String name) {
        return field(generatorClass, TypeName.get(type), name);
    }

    public final FieldSpec field(final Class<?> generatorClass, final TypeName type, final String name) {
        return prepareField(generatorClass, type, name)
                .build();
    }

    public Builder prepareField(final Class<?> generatorClass, final TypeName type, final String name) {
        return builder(generatorClass, type, name)
                .addModifiers(TypicalModifiers.privateField());
    }

    public Builder prepareConstant(final Class<?> generatorClass, final Type type, final String name) {
        return prepareConstant(generatorClass, TypeName.get(type), name);
    }

    public Builder prepareConstant(final Class<?> generatorClass, final TypeName type, final String name) {
        return builder(generatorClass, type, name)
                .addModifiers(TypicalModifiers.constantField());
    }

    private Builder builder(final Class<?> generatorClass, final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addAnnotations(annotations.generatedField(generatorClass));
    }

}
