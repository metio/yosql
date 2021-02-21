package wtf.metio.yosql.internals.config.generator.ant;

import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.internals.config.generator.settings.Annotations;

public final class AntAnnotations {

    public static TypeSpec asTypeSpec() {
        final var group = Annotations.configurationGroup();
        return TypeSpec.classBuilder(group.name()).build();
    }

    private AntAnnotations() {
        // factory class
    }

}
