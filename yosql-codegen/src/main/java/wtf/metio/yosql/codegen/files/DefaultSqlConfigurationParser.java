/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    private static final String PARAMETERS = "parameters";

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
            final var tree = mapper.readTree(yaml);
            if (tree instanceof ObjectNode root) {
                expandParameterShorthand(root);
            }
            return Optional.of(mapper.treeToValue(tree, SqlConfiguration.class));
        } catch (final JacksonException exception) {
            errors.add(exception);
            return Optional.empty();
        }
    }

    /**
     * Rewrites {@code parameters} written as a mapping of name to type into the list of objects the
     * model is bound from.
     *
     * <p>A parameter that needs nothing but a name and a type is most of them, and the list form
     * spends two lines and four words of punctuation on each. Both forms describe the same thing, so
     * the shorthand is expanded here rather than understood twice further down:</p>
     *
     * <pre>{@code
     * parameters:            parameters:
     *   id: uuid       ==>     - name: id
     *   name: string             type: java.util.UUID
     *                          - name: name
     *                            type: java.lang.String
     * }</pre>
     *
     * <p>A parameter needing more than a type — a JDBC type, a scale, a variant — keeps the list
     * form, which is why the shorthand maps to a type rather than to an object.</p>
     */
    private void expandParameterShorthand(final ObjectNode root) {
        final var parameters = root.get(PARAMETERS);
        if (parameters == null || !parameters.isObject()) {
            return;
        }
        final var expanded = mapper.createArrayNode();
        parameters.properties().forEach(entry -> {
            final var parameter = expanded.addObject();
            parameter.put("name", entry.getKey());
            final var type = entry.getValue();
            if (type != null && !type.isNull()) {
                parameter.set("type", type);
            }
        });
        root.set(PARAMETERS, expanded);
    }

}
