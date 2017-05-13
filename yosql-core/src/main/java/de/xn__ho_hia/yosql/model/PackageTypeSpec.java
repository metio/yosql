/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

import com.squareup.javapoet.TypeSpec;

/**
 * Wrapper around JavaPoet's {@link TypeSpec} that adds the package name.
 */
public class PackageTypeSpec {

    private final TypeSpec type;
    private final String   packageName;

    /**
     * @param type
     *            The type to wrap.
     * @param packageName
     *            The target package name for the type.
     */
    public PackageTypeSpec(final TypeSpec type, final String packageName) {
        this.type = type;
        this.packageName = packageName;
    }

    /**
     * @return The target type.
     */
    public TypeSpec getType() {
        return type;
    }

    /**
     * @return The target package name.
     */
    public String getPackageName() {
        return packageName;
    }

}
