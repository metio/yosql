/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import org.immutables.value.Value;

import java.util.Collections;
import java.util.List;

/**
 * Meta model for a single configuration group (e.g. a single Java class).
 */
@Value.Immutable
public interface ConfigurationGroup {

    //region builders

    static ImmutableConfigurationGroup.Builder builder() {
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
     * @return All configuration settings in this configuration group.
     */
    List<ConfigurationSetting> settings();

    /**
     * @return The optional list of tags associated with this configuration group.
     */
    List<String> tags();

    //region derived

    @Value.Lazy
    default String configurationName() {
        return name() + "Configuration";
    }

    @Value.Lazy
    default String immutableName() {
        return "Immutable" + name();
    }

    @Value.Lazy
    default String immutableConfigurationName() {
        return "Immutable" + configurationName();
    }

    //endregion

    //region defaults

    @Value.Default
    default List<AnnotationSpec> immutableAnnotations() {
        return Collections.emptyList();
    }

    @Value.Default
    default List<AnnotationSpec> cliAnnotations() {
        return Collections.emptyList();
    }

    @Value.Default
    default List<AnnotationSpec> gradleAnnotations() {
        return Collections.emptyList();
    }

    @Value.Default
    default List<AnnotationSpec> mavenAnnotations() {
        return Collections.emptyList();
    }

    @Value.Default
    default List<MethodSpec> derivedMethods() {
        return Collections.emptyList();
    }

    @Value.Default
    default List<MethodSpec> immutableMethods() {
        return Collections.emptyList();
    }

    @Value.Default
    default List<MethodSpec> cliMethods() {
        return Collections.emptyList();
    }

    @Value.Default
    default List<MethodSpec> gradleMethods() {
        return Collections.emptyList();
    }

    @Value.Default
    default List<MethodSpec> mavenMethods() {
        return Collections.emptyList();
    }

    //endregion

}
