/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.blocks;

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
     * @param statements    The statements of the method.
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
