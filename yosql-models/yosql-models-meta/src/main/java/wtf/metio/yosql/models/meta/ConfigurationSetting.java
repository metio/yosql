/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import org.immutables.value.Value;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a single configuration setting.
 */
@Value.Immutable
public interface ConfigurationSetting {

    //region builders

    static ImmutableConfigurationSetting.NameBuildStage builder() {
        return ImmutableConfigurationSetting.builder();
    }

    static ImmutableConfigurationSetting copyOf(final ConfigurationSetting setting) {
        return ImmutableConfigurationSetting.copyOf(setting);
    }

    //endregion

    /**
     * @return The name of this configuration setting.
     */
    String name();

    /**
     * @return The description of this configuration setting.
     */
    String description();

    /**
     * @return Longer explanation intended for the website.
     */
    Optional<String> explanation();

    /**
     * @return Example code for the configuration value in the front matter section.
     */
    Optional<String> frontMatterExampleCode();

    /**
     * @return The optional list of tags associated with this configuration setting.
     */
    List<String> tags();

    /**
     * @return The optional list of examples for this configuration setting.
     */
    List<ConfigurationExample> examples();

    /**
     * @return Methods for Ant models.
     */
    List<MethodSpec> antMethods();

    /**
     * @return Methods for CLI models.
     */
    List<MethodSpec> cliMethods();

    /**
     * @return Methods for Gradle models.
     */
    List<MethodSpec> gradleMethods();

    /**
     * @return Methods for Immutables models.
     */
    List<MethodSpec> immutableMethods();

    /**
     * @return Methods for Maven models.
     */
    List<MethodSpec> mavenMethods();

    /**
     * @return Fields for Ant models.
     */
    List<FieldSpec> antFields();

    /**
     * @return Fields for CLI models.
     */
    List<FieldSpec> cliFields();

    /**
     * @return Fields for Gradle models.
     */
    List<FieldSpec> gradleFields();

    /**
     * @return Fields for Immutables models.
     */
    List<FieldSpec> immutableFields();

    /**
     * @return Fields for Maven models.
     */
    List<FieldSpec> mavenFields();

    /**
     * @return Code to initialize an Ant setting.
     */
    Optional<CodeBlock> antInitializer();

    /**
     * @return Code to initialize a CLI setting.
     */
    Optional<CodeBlock> cliInitializer();

    /**
     * @return Code to initialize a Gradle setting.
     */
    Optional<CodeBlock> gradleInitializer();

    /**
     * @return Code to initialize a Maven setting.
     */
    Optional<CodeBlock> mavenInitializer();

    /**
     * @return Code to configure a Gradle convention.
     */
    Optional<CodeBlock> gradleConvention();

    /**
     * @return Code to configure the SqlConfiguration#merge method.
     */
    Optional<CodeBlock> mergeCode();

    //region defaults

    /**
     * @return Whether markdown documentation should be generated for this setting.
     */
    @Value.Default
    default boolean generateDocs() {
        return true;
    }

    /**
     * The key this setting is written under in a file, which is not always its name:
     * {@code returningMode} is written {@code returning}, because that is the name Jackson binds the
     * model from. Anything telling a reader — or a schema — what to write has to say this one.
     *
     * @return the {@code JsonProperty} name, or the setting's own when it carries none
     */
    @Value.Derived
    default String frontMatterKey() {
        return immutableMethods().stream()
                .flatMap(method -> method.annotations().stream())
                .filter(annotation -> JSON_PROPERTY.equals(annotation.type().toString()))
                .map(annotation -> annotation.members().get("value"))
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(Object::toString)
                .map(ConfigurationSetting::unquote)
                .findFirst()
                .orElseGet(this::name);
    }

    /**
     * What to show a reader who is about to copy the example.
     *
     * <p>A setting that names no example falls back to a placeholder, and a placeholder that cannot
     * be bound is worse than none: {@code executeOnce: configValue} is a page telling the reader to
     * write something the parser rejects. A boolean has only two values, so the placeholder can be
     * one of them.</p>
     */
    @Value.Derived
    default String frontMatterExample() {
        return frontMatterExampleCode().orElseGet(() -> isBoolean() ? "true" : "configValue");
    }

    private boolean isBoolean() {
        return immutableMethods().stream()
                .map(method -> method.returnType().toString())
                .anyMatch(type -> "boolean".equals(type) || type.endsWith("Boolean")
                        || type.endsWith("Boolean>"));
    }

    String JSON_PROPERTY = "com.fasterxml.jackson.annotation.JsonProperty";

    private static String unquote(final String literal) {
        if (literal.length() > 1 && literal.startsWith("\"") && literal.endsWith("\"")) {
            return literal.substring(1, literal.length() - 1);
        }
        return literal;
    }

    //endregion

}
