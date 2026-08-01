/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
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
     * @return The description a generated repository's constructor carries.
     */
    CodeBlock constructorJavadoc();

    /**
     * Documents a finished method's signature.
     *
     * <p>Generated code lands in a build that is not ours, and a javadoc run configured to treat
     * warnings as errors will not accept a documented method whose parameters and result are not.
     * The tags are written from the method itself, so they cannot describe a signature it does not
     * have.</p>
     *
     * @param method The method to document.
     * @return The same method, carrying a tag for every parameter and for its result.
     */
    MethodSpec withSignatureTags(MethodSpec method);

    /**
     * Creates typical javadoc documentation for generated fields.
     *
     * @param statement The statement of the field.
     * @return The javadoc for a single field based on the given statement.
     */
    CodeBlock fieldJavaDoc(SqlStatement statement);

}
