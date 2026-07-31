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
 * <p>The package follows the map converter's, so record converters land beside it wherever the user
 * put that. The class name is {@code <prefix><record><suffix>}, and the field a repository injects
 * it into is the record's name with the same suffix.</p>
 *
 * <p>The configurer and the generator both compute these, and they have to agree: the configurer
 * writes the field's type and the generator writes the class that field points at.</p>
 */
public final class RecordConverterNames {

    private final String converterPackage;
    private final String prefix;
    private final String suffix;
    private final String methodName;

    public RecordConverterNames(final ConverterConfiguration converters) {
        this.converterPackage = ClassName.bestGuess(converters.mapConverterClass()).packageName();
        this.prefix = converters.recordConverterPrefix();
        this.suffix = converters.recordConverterSuffix();
        this.methodName = converters.recordConverterMethod();
    }

    public ClassName converterClass(final ClassName resultRowType) {
        return ClassName.get(converterPackage, prefix + resultRowType.simpleName() + suffix);
    }

    /**
     * The record's name plus the class-name suffix, not the whole class name — a field holding a
     * converter reads better as {@code tenantConverter} than as {@code toTenantConverter}, and the
     * suffix is what says which kind of thing it is.
     */
    public String alias(final ClassName resultRowType) {
        final var simpleName = resultRowType.simpleName();
        return simpleName.substring(0, 1).toLowerCase(Locale.ROOT) + simpleName.substring(1) + suffix;
    }

    public String methodName() {
        return methodName;
    }

}
