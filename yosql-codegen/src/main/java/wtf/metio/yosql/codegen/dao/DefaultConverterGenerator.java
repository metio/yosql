/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.codegen.records.RecordConverterGenerator;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

public final class DefaultConverterGenerator implements ConverterGenerator {

    private final ConverterConfiguration converters;
    private final ToMapConverterGenerator mapConverterGenerator;
    private final RecordConverterGenerator recordConverterGenerator;

    public DefaultConverterGenerator(
            final ConverterConfiguration converters,
            final ToMapConverterGenerator mapConverterGenerator,
            final RecordConverterGenerator recordConverterGenerator) {
        this.converters = converters;
        this.mapConverterGenerator = mapConverterGenerator;
        this.recordConverterGenerator = recordConverterGenerator;
    }

    @Override
    public Stream<PackagedTypeSpec> generateConverterClasses(final List<SqlStatement> allStatements) {
        final var mapConverter = converters.generateMapConverter()
                ? Stream.of(mapConverterGenerator.generateToMapConverterClass())
                : Stream.<PackagedTypeSpec>empty();
        return Stream.concat(mapConverter, recordConverterGenerator.generateConverterClasses(allStatements));
    }

}
