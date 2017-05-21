/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.api;

import java.util.List;

import com.squareup.javapoet.MethodSpec;

import de.xn__ho_hia.yosql.model.SqlStatement;

/**
 * Generates methods for a repository.
 */
@FunctionalInterface
public interface MethodsGenerator {

    /**
     * Creates all methods of a repository based on a number of {@link SqlStatement}s.
     *
     * @param statements
     *            The statements to use.
     * @return The method specifications based on the given statements.
     */
    Iterable<MethodSpec> asMethods(List<SqlStatement> statements);

}
