/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.spring.data.r2dbc;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import wtf.metio.yosql.codegen.api.Fields;
import wtf.metio.yosql.codegen.api.FieldsGenerator;
import wtf.metio.yosql.codegen.api.Javadoc;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.Optional;

public final class SpringDataR2dbcFieldsGenerator implements FieldsGenerator {

    private final Fields fields;
    private final LoggingGenerator logging;
    private final Javadoc javadoc;

    public SpringDataR2dbcFieldsGenerator(
            final Fields fields,
            final LoggingGenerator logging,
            final Javadoc javadoc) {
        this.fields = fields;
        this.logging = logging;
        this.javadoc = javadoc;
    }

    @Override
    public Optional<CodeBlock> staticInitializer(final List<SqlStatement> statements) {
        return Optional.empty();
    }

    @Override
    public Iterable<FieldSpec> asFields(final List<SqlStatement> statements) {
        return null;
    }
}
