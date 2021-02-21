package wtf.metio.yosql.internals.config.generator.immutables;

import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.internals.config.generator.settings.Annotations;

public final class ImmutableAnnotations {

    public static TypeSpec asTypeSpec() {
        final var group = Annotations.configurationGroup();
        return TypeSpec.classBuilder(group.name()).build();
    }

    private ImmutableAnnotations() {
        // factory class
    }

}
