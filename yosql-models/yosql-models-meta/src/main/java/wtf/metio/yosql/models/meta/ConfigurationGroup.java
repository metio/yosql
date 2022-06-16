/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta;

import com.squareup.javapoet.*;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

/**
 * Meta model for a single configuration group (e.g. a single Java class).
 */
@Value.Immutable
public interface ConfigurationGroup {

    //region builders

    static ImmutableConfigurationGroup.NameBuildStage builder() {
        return ImmutableConfigurationGroup.builder();
    }

    //endregion

    /**
     * @return The unique name of the configuration group. Must be a CamelCase name like "RowConverter" or "Repository".
     */
    String name();

    /**
     * @return The description for the entire configuration group.
     */
    String description();

    /**
     * @return Longer explanation for the website.
     */
    Optional<String> explanation();

    /**
     * @return All configuration settings in this configuration group.
     */
    List<ConfigurationSetting> settings();

    /**
     * @return The optional list of tags associated with this configuration group.
     */
    List<String> tags();

    /**
     * @return The optional list of additional types for Ant.
     */
    List<TypeSpec> antTypes();

    /**
     * @return The optional list of additional types for the CLI.
     */
    List<TypeSpec> cliTypes();

    /**
     * @return The optional list of additional types for Maven.
     */
    List<TypeSpec> mavenTypes();

    /**
     * @return The optional list of additional types for Gradle.
     */
    List<TypeSpec> gradleTypes();

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
     * @return Parameters for the Ant asConfiguration method.
     */
    List<ParameterSpec> antParameters();

    /**
     * @return Parameters for the CLI asConfiguration method.
     */
    List<ParameterSpec> cliParameters();

    /**
     * @return Parameters for the Gradle asConfiguration method.
     */
    List<ParameterSpec> gradleParameters();

    /**
     * @return Parameters for the Gradle configureConventions method.
     */
    List<ParameterSpec> gradleConventionParameters();

    /**
     * @return Parameters for the Maven asConfiguration method.
     */
    List<ParameterSpec> mavenParameters();

    //region lazy

    @Value.Lazy
    default String configurationName() { // TODO: remove?
        return name() + "Configuration";
    }

    //endregion

}
