/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.api;

import java.util.List;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

import de.xn__ho_hia.yosql.model.SqlStatement;

/**
 * Generates fields and static initializers for those.
 */
public interface FieldsGenerator {

    /**
     * Creates the static initializer block for a class based on a number of {@link SqlStatement}s.
     *
     * @param statements
     *            The statements to use.
     * @return The static initializer for the given statements.
     */
    CodeBlock staticInitializer(List<SqlStatement> statements);

    /**
     * Creates the field specifications for a class based on a number of {@link SqlStatement}s.
     *
     * @param statements
     *            The statements to use.
     * @return The field specifications.
     */
    Iterable<FieldSpec> asFields(List<SqlStatement> statements);

}
