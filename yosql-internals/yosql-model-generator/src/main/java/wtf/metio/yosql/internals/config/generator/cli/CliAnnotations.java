package wtf.metio.yosql.internals.config.generator.cli;

import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.internals.config.generator.settings.Annotations;

public final class CliAnnotations {

    public static TypeSpec asTypeSpec() {
        final var group = Annotations.configurationGroup();
        return TypeSpec.classBuilder(group.name()).build();
    }

    private CliAnnotations() {
        // factory class
    }

}
