/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.ClassName;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;

import java.util.Locale;

/**
 * Where a generated converter lives and what it is called.
 *
 * <p>Derived rather than configured, and derived from what the user already configured for the map
 * converter: a record converter lands in the same package under the same {@code To…Converter}
 * naming the hand-written examples use, so a repository that gains one reads no differently from
 * one that always had a converter injected.</p>
 *
 * <p>The configurer and the generator both compute these names, and they have to agree — the
 * configurer writes the field's type and the generator writes the class that field points at.</p>
 */
public final class RecordConverterNames {

    private static final String METHOD_NAME = "asUserType";

    private final String converterPackage;

    public RecordConverterNames(final ConverterConfiguration converters) {
        this.converterPackage = ClassName.bestGuess(converters.mapConverterClass()).packageName();
    }

    public ClassName converterClass(final ClassName resultRowType) {
        return ClassName.get(converterPackage, "To" + resultRowType.simpleName() + "Converter");
    }

    public String alias(final ClassName resultRowType) {
        final var simpleName = resultRowType.simpleName();
        return simpleName.substring(0, 1).toLowerCase(Locale.ROOT) + simpleName.substring(1) + "Converter";
    }

    public String methodName() {
        return METHOD_NAME;
    }

}
