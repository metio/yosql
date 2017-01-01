package com.github.sebhoss.yosql;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.yaml.snakeyaml.Yaml;

@Named
@Singleton
public class SqlFileParser {

    private final Yaml         yamlParser = new Yaml();
    private final PluginErrors pluginErrors;

    @Inject
    public SqlFileParser(final PluginErrors pluginErrors) {
        this.pluginErrors = pluginErrors;
    }

    public SqlStatement parse(final Path path) {
        final StringBuilder yaml = new StringBuilder();
        final StringBuilder sql = new StringBuilder();

        splitUpYamlAndSql(path, yaml::append, sql::append);

        final SqlStatementConfiguration configuration = yamlParser.loadAs(yaml.toString(),
                SqlStatementConfiguration.class);

        return new SqlStatement(configuration, sql.toString());
    }

    private void splitUpYamlAndSql(final Path path, final Consumer<String> yaml, final Consumer<String> sql) {
        try (final BufferedReader reader = Files.newBufferedReader(path)) {
            for (String line; (line = reader.readLine()) != null;) {
                if (line.startsWith("--")) {
                    yaml.accept(line.substring(2));
                    yaml.accept("\n");
                } else {
                    sql.accept(line);
                    sql.accept("\n");
                }
            }
        } catch (final IOException exception) {
            pluginErrors.add(exception);
        }
    }

}
