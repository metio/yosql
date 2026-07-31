/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import ch.qos.cal10n.IMessageConveyor;
import com.squareup.javapoet.CodeBlock;
import wtf.metio.javapoet.TypeGuesser;
import wtf.metio.yosql.internals.jdk.FileNames;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

public final class DefaultJavadoc implements Javadoc {

    private final FilesConfiguration files;
    private final IMessageConveyor messages;

    public DefaultJavadoc(final FilesConfiguration files, final IMessageConveyor messages) {
        this.files = files;
        this.messages = messages;
    }

    @Override
    public CodeBlock repositoryJavadoc(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder()
                .add(messages.getMessage(Javadocs.USED_FILES))
                .add(messages.getMessage(Javadocs.LIST_START));
        final var input = files.inputBaseDirectory();
        statements.stream()
                .map(SqlStatement::getSourcePath)
                .distinct()
                .map(input::relativize)
                .forEach(path -> builder.add(messages.getMessage(Javadocs.LIST_ITEM), FileNames.toSlashes(path)));
        builder.add(messages.getMessage(Javadocs.LIST_END));
        return builder.build();
    }

    @Override
    public CodeBlock fieldJavaDoc(final SqlStatement statement) {
        final var input = files.inputBaseDirectory();
        final var builder = CodeBlock.builder()
                .add(messages.getMessage(Javadocs.USED_FILE))
                .add(messages.getMessage(Javadocs.LIST_START))
                .add(messages.getMessage(Javadocs.LIST_ITEM), FileNames.toSlashes(input.relativize(statement.getSourcePath())));
        builder.add(messages.getMessage(Javadocs.LIST_END));
        return builder.build();
    }

    @Override
    public CodeBlock methodJavadoc(final List<SqlStatement> statements, final String configuration) {
        final var builder = CodeBlock.builder();
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .flatMap(config -> config.description().stream())
                .filter(not(Strings::isBlank))
                .forEach(description -> builder.add(messages.getMessage(Javadocs.DESCRIPTION), description));
        if (statements.size() > 1) {
            builder.add(messages.getMessage(Javadocs.EXECUTED_STATEMENTS));
        } else {
            builder.add(messages.getMessage(Javadocs.EXECUTED_STATEMENT));
        }
        for (final var statement : statements) {
            statement.getConfiguration().vendor()
                    .filter(not(Strings::isBlank))
                    .ifPresentOrElse(vendor ->
                                    builder.add(messages.getMessage(Javadocs.VENDOR), vendor),
                            () -> {
                                if (statements.size() > 1) {
                                    builder.add(messages.getMessage(Javadocs.FALLBACK));
                                }
                            });
            builder.add(messages.getMessage(Javadocs.STATEMENT), statement.getRawStatement());
        }
        builder.add(messages.getMessage(Javadocs.USED_FILES_METHOD))
                .add(messages.getMessage(Javadocs.LIST_START));
        final var input = files.inputBaseDirectory();
        statements.stream()
                .map(SqlStatement::getSourcePath)
                .distinct()
                .map(input::relativize)
                .forEach(path -> builder.add(messages.getMessage(Javadocs.LIST_ITEM), FileNames.toSlashes(path)));
        builder.add(messages.getMessage(Javadocs.LIST_END));
        builder.add(messages.getMessage(Javadocs.DISABLE_WITH), configuration);
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .map(SqlConfiguration::resultRowConverter)
                .flatMap(Optional::stream)
                .map(ResultRowConverter::resultType)
                .flatMap(Optional::stream)
                .filter(type -> !type.startsWith("java"))
                .map(type -> type.substring(0, type.contains("<") ? type.indexOf("<") : type.length()))
                .distinct()
                .map(TypeGuesser::guessTypeName)
                .filter(type -> !type.isPrimitive())
                .filter(type -> !type.isBoxedPrimitive())
                .forEach(type -> builder.add(messages.getMessage(Javadocs.SEE), type));
        return builder.build();
    }

}
