/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.files;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Default implementation of a {@link SqlConfigurationParser} that works with YAML based configs.
 */
public final class DefaultSqlConfigurationParser implements SqlConfigurationParser {

    private final ExecutionErrors errors;
    private final YAMLMapper mapper;

    public DefaultSqlConfigurationParser(final ExecutionErrors errors) {
        this.errors = errors;
        mapper = YAMLMapper.builder()
                .addModule(new Jdk8Module())
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
    }

    @Override
    public SqlConfiguration parseConfig(final String yaml) {
        return Optional.of(yaml)
                .filter(Predicate.not(Strings::isBlank))
                .flatMap(this::parseYaml)
                .orElseGet(() -> SqlConfiguration.builder().build());
    }

    private Optional<SqlConfiguration> parseYaml(final String yaml) {
        try {
            return Optional.of(mapper.readValue(yaml, SqlConfiguration.class));
        } catch (final JacksonException exception) {
            errors.add(exception);
            return Optional.empty();
        }
    }

}
