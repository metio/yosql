/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.dao.r2dbc;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import wtf.metio.yosql.tooling.codegen.generator.api.FieldsGenerator;
import wtf.metio.yosql.tooling.codegen.generator.api.LoggingGenerator;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.Fields;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.Javadoc;
import wtf.metio.yosql.tooling.codegen.generator.blocks.jdbc.JdbcFields;
import wtf.metio.yosql.tooling.codegen.generator.blocks.jdbc.JdbcNames;
import wtf.metio.yosql.tooling.codegen.sql.SqlStatement;

import java.util.List;

public final class R2dbcFieldsGenerator implements FieldsGenerator {

    private final Fields fields;
    private final LoggingGenerator logging;
    private final JdbcFields jdbcFields;
    private final JdbcNames jdbcNames;
    private final Javadoc javadoc;

    public R2dbcFieldsGenerator(
            final Fields fields,
            final LoggingGenerator logging,
            final JdbcFields jdbcFields,
            final JdbcNames jdbcNames,
            final Javadoc javadoc) {
        this.fields = fields;
        this.logging = logging;
        this.jdbcFields = jdbcFields;
        this.jdbcNames = jdbcNames;
        this.javadoc = javadoc;
    }

    @Override
    public CodeBlock staticInitializer(final List<SqlStatement> statements) {
        return null;
    }

    @Override
    public Iterable<FieldSpec> asFields(final List<SqlStatement> statements) {
        return null;
    }
}
