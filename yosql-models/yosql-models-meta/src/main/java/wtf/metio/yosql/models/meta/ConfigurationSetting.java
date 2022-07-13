/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.immutables.value.Value;

import java.util.List;
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
     * @return Annotations for Ant models.
     */
    List<AnnotationSpec> antAnnotations();

    /**
     * @return Annotations for CLI models.
     */
    List<AnnotationSpec> cliAnnotations();

    /**
     * @return Annotations for Gradle models.
     */
    List<AnnotationSpec> gradleAnnotations();

    /**
     * @return Annotations for Immutables models.
     */
    List<AnnotationSpec> immutableAnnotations();

    /**
     * @return Annotations for Maven models.
     */
    List<AnnotationSpec> mavenAnnotations();

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

    //endregion

}
