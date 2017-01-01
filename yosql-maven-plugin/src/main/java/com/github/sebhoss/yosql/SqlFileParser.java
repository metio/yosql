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

    public SqlStatement parse(final SqlSourceFile source) {
        final StringBuilder yaml = new StringBuilder();
        final StringBuilder sql = new StringBuilder();

        splitUpYamlAndSql(source.getPathToSqlFile(), yaml::append, sql::append);

        final SqlStatementConfiguration configuration = createStatementConfiguration(yaml.toString());

        if (configuration.getName() == null || configuration.getName().isEmpty()) {
            configuration.setName(getFileNameWithoutExtension(source.getPathToSqlFile()));
        }
        if (configuration.getRepository() == null || configuration.getRepository().isEmpty()) {
            final Path relativePath = source.getBaseDirectory().relativize(source.getPathToSqlFile());
            final Path fullyQualifiedPath = relativePath.getParent();
            final String fullyQualifiedRepositoryName = fullyQualifiedPath.toString();
            configuration.setRepository(fullyQualifiedRepositoryName);
        } else {
            final String userGivenRepository = configuration.getRepository();
            final String cleanedRepository = userGivenRepository.replace(".", "/");
            configuration.setRepository(cleanedRepository);
        }

        return new SqlStatement(configuration, sql.toString());
    }

    private SqlStatementConfiguration createStatementConfiguration(final String yaml) {
        SqlStatementConfiguration configuration = yamlParser.loadAs(yaml,
                SqlStatementConfiguration.class);
        if (configuration == null) {
            configuration = new SqlStatementConfiguration();
        }
        return configuration;
    }

    private String getFileNameWithoutExtension(final Path path) {
        final String fileName = path.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf("."));
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
