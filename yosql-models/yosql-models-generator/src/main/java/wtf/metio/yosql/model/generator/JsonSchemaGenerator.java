/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.model.generator;

import com.palantir.javapoet.MethodSpec;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.meta.data.AllConfigurations;
import wtf.metio.yosql.models.meta.data.Sql;
import wtf.metio.yosql.models.meta.data.Tags;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Describes the front matter of a statement as a JSON schema, so that an editor can complete and
 * check it while it is being written rather than leaving every mistake to the build.
 *
 * <p>The keys are the settings tagged {@link Tags#FRONT_MATTER}, wherever they are declared: some
 * belong to a statement alone and some are repository settings a statement may override. Both are
 * written the same way in a file, so the schema does not distinguish them either — and a setting
 * declared in both places is one key, which is why the properties are collected by name.</p>
 */
final class JsonSchemaGenerator {

    private static final String SCHEMA_ID = "https://yosql.projects.metio.wtf/schema/frontmatter.json";
    private static final String JSON_PROPERTY = "com.fasterxml.jackson.annotation.JsonProperty";

    private JsonSchemaGenerator() {
        // utility class, call #frontMatterSchema() directly
    }

    static String frontMatterSchema(final String version) {
        final var properties = new TreeMap<String, String>();
        frontMatterSettings().forEach(setting -> properties.putIfAbsent(key(setting), property(setting)));

        final var schema = new StringBuilder();
        schema.append("{\n");
        schema.append("  \"$schema\": \"https://json-schema.org/draft/2020-12/schema\",\n");
        schema.append("  \"$id\": \"").append(SCHEMA_ID).append("\",\n");
        schema.append("  \"title\": \"YoSQL statement front matter\",\n");
        schema.append("  \"description\": \"The YAML written in SQL comments above a statement.")
                .append(" Generated from YoSQL ").append(escape(version)).append(".\",\n");
        schema.append("  \"type\": \"object\",\n");
        schema.append("  \"additionalProperties\": false,\n");
        schema.append("  \"properties\": {\n");
        schema.append(String.join(",\n", properties.values())).append("\n");
        schema.append("  }\n");
        schema.append("}\n");
        return schema.toString();
    }

    private static Stream<ConfigurationSetting> frontMatterSettings() {
        return Stream.concat(AllConfigurations.allConfigurationGroups(), Stream.of(Sql.configurationGroup()))
                .flatMap(group -> group.settings().stream())
                .filter(setting -> setting.tags().contains(Tags.FRONT_MATTER));
    }

    private static String property(final ConfigurationSetting setting) {
        final var type = schemaType(setting)
                .map(",\n      "::concat)
                .orElse("");
        return "    \"" + key(setting) + "\": {\n"
                + "      \"description\": \"" + escape(setting.description()) + "\"" + type + "\n"
                + "    }";
    }

    /**
     * The key a setting is written under in a file, which is the name the model is bound from rather
     * than the name of the setting: {@code returningMode} is written {@code returning}. A schema that
     * refuses everything it does not list has to agree with the parser about all of them.
     */
    private static String key(final ConfigurationSetting setting) {
        return setting.immutableMethods().stream()
                .flatMap(method -> method.annotations().stream())
                .filter(annotation -> JSON_PROPERTY.equals(annotation.type().toString()))
                .map(annotation -> annotation.members().get("value"))
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(Object::toString)
                .map(JsonSchemaGenerator::unquote)
                .findFirst()
                .orElseGet(setting::name);
    }

    private static String unquote(final String literal) {
        if (literal.length() > 1 && literal.startsWith("\"") && literal.endsWith("\"")) {
            return literal.substring(1, literal.length() - 1);
        }
        return literal;
    }

    /**
     * The JSON type a value is written as, taken from the type the generator reads it into.
     *
     * <p>Anything richer than a scalar — the parameter list, the annotations, the converter, each of
     * which accepts more than one shape — is left untyped rather than described half-correctly, so a
     * schema-aware editor completes the key and then stays out of the way.</p>
     */
    private static Optional<String> schemaType(final ConfigurationSetting setting) {
        return setting.immutableMethods().stream()
                .map(MethodSpec::returnType)
                .map(Object::toString)
                .findFirst()
                .flatMap(JsonSchemaGenerator::jsonType);
    }

    private static Optional<String> jsonType(final String returnType) {
        return switch (unwrapOptional(returnType)) {
            case "java.lang.String", "java.nio.file.Path" -> Optional.of("\"type\": \"string\"");
            case "java.lang.Boolean", "boolean" -> Optional.of("\"type\": \"boolean\"");
            case "java.lang.Integer", "int", "java.lang.Long", "long" -> Optional.of("\"type\": \"integer\"");
            case "wtf.metio.yosql.models.configuration.ReturningMode" ->
                    Optional.of("\"enum\": [\"none\", \"single\", \"multiple\", \"cursor\"]");
            case "wtf.metio.yosql.models.configuration.SqlStatementType" ->
                    Optional.of("\"enum\": [\"reading\", \"writing\", \"calling\"]");
            default -> Optional.empty();
        };
    }

    private static String unwrapOptional(final String returnType) {
        final var prefix = "java.util.Optional<";
        if (returnType.startsWith(prefix) && returnType.endsWith(">")) {
            return returnType.substring(prefix.length(), returnType.length() - 1);
        }
        return returnType;
    }

    private static String escape(final String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", " ")
                .replace("\r", "");
    }

}
