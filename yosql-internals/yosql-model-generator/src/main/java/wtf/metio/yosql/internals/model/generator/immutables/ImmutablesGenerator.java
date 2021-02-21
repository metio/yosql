package wtf.metio.yosql.internals.model.generator.immutables;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;
import wtf.metio.yosql.internals.meta.model.ConfigurationSetting;
import wtf.metio.yosql.internals.model.generator.api.Generator;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public final class ImmutablesGenerator implements Generator {

    private final String basePackageName;

    public ImmutablesGenerator(final String basePackageName) {
        this.basePackageName = basePackageName;
    }

    @Override
    public TypeSpec apply(final ConfigurationGroup group) {
        return TypeSpec.interfaceBuilder(group.name())
                .addJavadoc(group.description())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Value.Immutable.class)
                .addMethod(builder(group))
                .addMethod(usingDefaults(group))
                .addMethods(defaultMethods(group))
                .build();
    }

    private MethodSpec builder(final ConfigurationGroup group) {
        final var configName = ClassName.get(basePackageName, group.immutableName());
        return MethodSpec.methodBuilder("builder")
                .addJavadoc("@return Builder for new $L", group.name())
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(configName.nestedClass("Builder"))
                .addStatement("return $T.builder()", configName)
                .build();
    }

    private MethodSpec usingDefaults(final ConfigurationGroup group) {
        final var configName = ClassName.get(basePackageName, group.immutableName());
        return MethodSpec.methodBuilder("usingDefaults")
                .addJavadoc("@return A file configuration using default values.")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(configName.nestedClass("Builder"))
                .addStatement("return $T.builder()", configName)
                .build();
    }

    private List<MethodSpec> defaultMethods(final ConfigurationGroup group) {
        return group.settings().stream()
                .filter(setting -> setting.defaultValue().isPresent())
                .map(this::defaultMethod)
                .collect(Collectors.toList());
    }

    private MethodSpec defaultMethod(final ConfigurationSetting setting) {
        return MethodSpec.methodBuilder(setting.name())
                .addJavadoc(setting.description())
                .addAnnotation(Value.Default.class)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.DEFAULT)
                .returns(setting.type())
                .addStatement(defaultReturn(setting))
                .build();
    }

    private CodeBlock defaultReturn(final ConfigurationSetting setting) {
        final var typeName = setting.type().toString();
        return setting.defaultValue().map(value -> {
            if ("java.lang.String".equals(typeName)) {
                return CodeBlock.builder().add("return $S", value).build();
            } else if ("java.nio.file.Path".equals(typeName)) {
                return CodeBlock.builder().add("return $T.get($S)", Paths.class, value).build();
            } else if (value.getClass().isEnum()) {
                return CodeBlock.of("return $T.$L", value.getClass(), value);
            } else if (setting.type().isPrimitive() || setting.type().isBoxedPrimitive()) {
                return CodeBlock.of("return $L", value);
            }
            System.out.println("typeName: " + typeName);
            return null;
        }).orElseThrow();


    }

}
