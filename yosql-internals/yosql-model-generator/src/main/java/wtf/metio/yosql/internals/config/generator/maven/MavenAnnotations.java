package wtf.metio.yosql.internals.config.generator.maven;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.internals.config.generator.settings.Annotations;

import javax.lang.model.element.Modifier;

public final class MavenAnnotations {

    public static TypeSpec asTypeSpec() {
        final var group = Annotations.configurationGroup();
        final var builder = TypeSpec.classBuilder(group.name())
                .addJavadoc("Configures how annotations are applied to the generated code.");

        group.settings().forEach(setting -> {
            final var field = FieldSpec.builder(setting.type(), setting.name(), Modifier.PRIVATE, Modifier.FINAL)
                    .addJavadoc(setting.description());
            setting.defaultValue().ifPresent(value -> {
                field.initializer("$S", value);
            });
        });

        return builder.build();
    }

    private MavenAnnotations() {
        // factory class
    }

}
