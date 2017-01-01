package com.github.sebhoss.yosql;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class CodeGenerator {

    private final PluginErrors pluginErrors;

    @Inject
    public CodeGenerator(final PluginErrors pluginErrors) {
        this.pluginErrors = pluginErrors;
    }

    /**
     * Generates a single repository.
     *
     * @param targetPath
     *            The target path to the generated repository.
     * @param sqlStatements
     *            The SQL statements to be included in the repository.
     */
    public void generateRepository(final Path targetPath, final List<SqlStatement> sqlStatements) {
        // TODO: Write repository + add all SQL statements as methods to it

        final StringBuilder names = new StringBuilder();
        sqlStatements
                .stream()
                .sorted((s1, s2) -> s1.getConfiguration().getName().compareTo(s2.getConfiguration().getName()))
                .forEach(statement -> {
                    names.append(statement.getConfiguration().getName());
                    names.append("\n");
                });

        try {
            if (!targetPath.getParent().toFile().exists()) {
                targetPath.getParent().toFile().mkdirs();
            }
            if (!Files.exists(targetPath)) {
                Files.createFile(targetPath);
            }
            Files.write(targetPath, names.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (final IOException exception) {
            pluginErrors.add(exception);
        }
    }

}
