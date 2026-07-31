/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.models.immutables;

import com.squareup.javapoet.TypeSpec;
import org.immutables.value.Value;

/**
 * Wrapper around JavaPoet's {@link TypeSpec} that adds the package name.
 */
@Value.Immutable
public interface PackagedTypeSpec {

    static PackagedTypeSpec of(final TypeSpec type, final String packageName) {
        return ImmutablePackagedTypeSpec.builder()
                .setType(type)
                .setPackageName(packageName)
                .build();
    }

    /**
     * @return The target type to wrap.
     */
    TypeSpec getType();

    /**
     * @return The target package name for the type.
     */
    String getPackageName();

}
