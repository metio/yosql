/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

public final class DefaultConverterGenerator implements ConverterGenerator {

    private final ConverterConfiguration converters;
    private final ToMapConverterGenerator mapConverterGenerator;

    public DefaultConverterGenerator(
            final ConverterConfiguration converters,
            final ToMapConverterGenerator mapConverterGenerator) {
        this.converters = converters;
        this.mapConverterGenerator = mapConverterGenerator;
    }

    @Override
    public Stream<PackagedTypeSpec> generateConverterClasses(final List<SqlStatement> allStatements) {
        PackagedTypeSpec toMapConverterClass = null;

        if (converters.generateMapConverter()) {
            toMapConverterClass = mapConverterGenerator.generateToMapConverterClass();
        }

        return Stream.of(toMapConverterClass);
    }

}
