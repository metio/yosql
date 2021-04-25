/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeName;
import org.immutables.value.Value;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a single configuration setting.
 */
@Value.Immutable
public interface ConfigurationSetting {

    //region builders

    static ImmutableConfigurationSetting.Builder builder() {
        return ImmutableConfigurationSetting.builder();
    }

    static ImmutableConfigurationSetting copyOf(final ConfigurationSetting setting) {
        return ImmutableConfigurationSetting.copyOf(setting);
    }

    //endregion

    String name();

    String description();

    TypeName type();

    Optional<TypeName> cliType();

    Optional<TypeName> gradleType();

    Optional<TypeName> mavenType();

    Optional<Object> value();

    Optional<Object> cliValue();

    Optional<Object> gradleValue();

    Optional<Object> mavenValue();

    /**
     * @return The optional list of tags associated with this configuration setting.
     */
    List<String> tags();

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

    //endregion

}
