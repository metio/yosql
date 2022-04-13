/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.api;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generates Javadocs comments for various parts of the generated code.
 */
public interface Javadoc {

    /**
     * Creates typical javadoc documentation for generated repositories.
     *
     * @param statements The statements of the repository.
     * @return The class javadoc for a repository.
     */
    CodeBlock repositoryJavadoc(List<SqlStatement> statements);

    /**
     * Creates typical javadoc documentation for generated methods.
     *
     * @param statements The statements of the method.
     * @param configuration The configuration toggle to use.
     * @return The javadoc for a single method based on the given statements.
     */
    CodeBlock methodJavadoc(List<SqlStatement> statements, String configuration);

    /**
     * Creates typical javadoc documentation for generated fields.
     *
     * @param statement The statement of the field.
     * @return The javadoc for a single field based on the given statement.
     */
    CodeBlock fieldJavaDoc(SqlStatement statement);

}
