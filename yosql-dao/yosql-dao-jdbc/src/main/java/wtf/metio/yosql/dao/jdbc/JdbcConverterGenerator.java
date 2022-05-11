/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc;

import wtf.metio.yosql.codegen.api.ConverterGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

public final class JdbcConverterGenerator implements ConverterGenerator {

    private final ConverterConfiguration converters;
    private final ToMapConverterGenerator mapConverterGenerator;

    public JdbcConverterGenerator(
            final ConverterConfiguration converters,
            final ToMapConverterGenerator mapConverterGenerator) {
        this.converters = converters;
        this.mapConverterGenerator = mapConverterGenerator;
    }

    @Override
    public boolean supports(final PersistenceApis api) {
        return PersistenceApis.JDBC.equals(api);
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
