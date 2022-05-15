/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
