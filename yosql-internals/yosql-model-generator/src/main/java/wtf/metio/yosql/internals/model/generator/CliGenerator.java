package wtf.metio.yosql.internals.model.generator;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;
import wtf.metio.yosql.internals.meta.model.data.AllConfigurations;
import wtf.metio.yosql.internals.utils.generator.StandardClasses;

import javax.lang.model.element.Modifier;
import java.util.function.Consumer;

final class CliGenerator {

    static void writeCliModels(final Consumer<TypeSpec> writer) {
        AllConfigurations.allConfigurationGroups()
                .stream().map(CliGenerator::asCliType)
                .forEach(writer);
    }

    private static TypeSpec asCliType(final ConfigurationGroup group) {
        final var builder = StandardClasses.openClass(ClassName.bestGuess(group.name()))
                .addJavadoc(group.description());
        group.settings().forEach(setting -> {
            final var field = FieldSpec.builder(setting.type(), setting.name(), Modifier.PRIVATE, Modifier.FINAL)
                    .addJavadoc(setting.description());
            setting.defaultValue().ifPresent(value -> initializeField(field, setting.type(), value));
            builder.addField(field.build());
        });
        return builder.build();
    }

    private static void initializeField(final FieldSpec.Builder field, final TypeName type, final Object value) {
        if ("java.lang.String".equals(type.toString())) {
            field.initializer("$S", value);
        } else if (value.getClass().isEnum()) {
            field.initializer("$T.$L", type, value);
        } else {
            field.initializer("$L", value);
        }
    }

}
