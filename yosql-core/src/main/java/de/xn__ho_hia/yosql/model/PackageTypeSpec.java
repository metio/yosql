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
