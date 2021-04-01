/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.CodeBlock;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.api.Javadoc;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.util.List;
import java.util.Objects;

import static java.util.function.Predicate.not;

public final class DefaultJavadoc implements Javadoc {

    private final FilesConfiguration files;

    public DefaultJavadoc(final FilesConfiguration files) {
        this.files = files;
    }

    @Override
    public CodeBlock repositoryJavadoc(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder()
                .add("Generated based on the following file(s):\n")
                .add("<ul>\n");
        final var input = files.inputBaseDirectory();
        statements.stream()
                .map(SqlStatement::getSourcePath)
                .distinct()
                .map(input::relativize)
                .forEach(path -> builder.add("<li>$L</li>\n", path));
        builder.add("</ul>\n");
        return builder.build();
    }

    @Override
    public CodeBlock fieldJavaDoc(final SqlStatement statement) {
        final var input = files.inputBaseDirectory();
        final var builder = CodeBlock.builder()
                .add("Generated based on the following file:\n")
                .add("<ul>\n")
                .add("<li>$L</li>\n", input.relativize(statement.getSourcePath()));
        builder.add("</ul>\n");
        return builder.build();
    }

    @Override
    public CodeBlock methodJavadoc(final List<SqlStatement> statements, final String configuration) {
        final var builder = CodeBlock.builder();
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .map(SqlConfiguration::description)
                .filter(not(Strings::isBlank))
                .forEach(description -> builder.add("<p>$L<p>\n", description));
        if (statements.size() > 1) {
            builder.add("<p>Executes one of the following statements:</p>");
        } else {
            builder.add("<p>Executes the following statement:</p>");
        }
        for (final var statement : statements) {
            if (Strings.isBlank(statement.getConfiguration().vendor())) {
                if (statements.size() > 1) {
                    builder.add("\n\n<p>Fallback for other databases:</p>");
                }
            } else {
                builder.add("\n\n<p>Vendor: $L</p>", statement.getConfiguration().vendor());
            }
            builder.add("\n<pre>\n$L</pre>", statement.getRawStatement());
        }
        builder.add("\n\n<p>Generated based on the following file(s):</p>\n")
                .add("<ul>\n");
        final var input = files.inputBaseDirectory();
        statements.stream()
                .map(SqlStatement::getSourcePath)
                .distinct()
                .map(input::relativize)
                .forEach(path -> builder.add("<li>$L</li>\n", path));
        builder.add("</ul>\n");
        builder.add("<p>Disable generating this method by setting <strong>$L</strong> to <strong>false</strong></p>\n", configuration);
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .flatMap(config -> config.resultRowConverter().stream())
                .filter(Objects::nonNull)
                .map(ResultRowConverter::resultType)
                .filter(Objects::nonNull)
                .filter(type -> !type.startsWith("java"))
                .map(type -> type.substring(0, type.contains("<") ? type.indexOf("<") : type.length()))
                .distinct()
                .map(TypeGuesser::guessTypeName)
                .filter(type -> !type.isPrimitive())
                .filter(type -> !type.isBoxedPrimitive())
                .forEach(type -> builder.add("\n@see $L", type));
        return builder.build();
    }

}
