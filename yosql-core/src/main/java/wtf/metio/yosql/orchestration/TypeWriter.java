package wtf.metio.yosql.orchestration;

import wtf.metio.yosql.model.sql.PackageTypeSpec;

/**
 * Writes a single {@link com.squareup.javapoet.TypeSpec type} into a directory.
 */
public interface TypeWriter {

    /**
     * Writes the given type specification into the
     * {@link wtf.metio.yosql.model.configuration.FileConfiguration#outputBaseDirectory() configured output directory}.
     *
     * @param typeSpec The type specification to write.
     */
    void writeType(PackageTypeSpec typeSpec);

}
