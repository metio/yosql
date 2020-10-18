/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.model.sql;

import com.squareup.javapoet.TypeSpec;
import org.immutables.value.Value;

/**
 * Wrapper around JavaPoet's {@link TypeSpec} that adds the package name.
 */
@Value.Immutable
public interface PackagedTypeSpec {

    static PackagedTypeSpec of(TypeSpec type, String packageName) {
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
