/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.blocks.api;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

/**
 * Generates Javadocs comments for various parts of the generated code.
 */
public interface Javadoc {

    /**
     * Creates the typical javadoc documentation for generated repositories.
     *
     * @param statements
     *            The vendor statements of the repository.
     * @return The class javadoc for a repository.
     */
    CodeBlock repositoryJavadoc(List<SqlStatement> statements);

    /**
     * Creates the typical javadoc documentation for generated methods.
     *
     * @param statements
     *            The vendor statements of the method.
     * @return The javadoc for a single method based on the given statements.
     */
    CodeBlock methodJavadoc(List<SqlStatement> statements);

}
