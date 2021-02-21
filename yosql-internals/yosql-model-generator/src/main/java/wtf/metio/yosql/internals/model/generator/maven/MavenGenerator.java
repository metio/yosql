package wtf.metio.yosql.internals.model.generator.maven;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;
import wtf.metio.yosql.internals.meta.model.data.AllConfigurations;
import wtf.metio.yosql.internals.model.generator.api.Generator;
import wtf.metio.yosql.internals.utils.generator.StandardClasses;

import javax.lang.model.element.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.function.Consumer;

public final class MavenGenerator implements Generator {

    private final String immutablesBasePackage;

    public MavenGenerator(final String immutablesBasePackage) {
        this.immutablesBasePackage = immutablesBasePackage;
    }

    @Override
    public TypeSpec apply(final ConfigurationGroup group) {
        final var builder = StandardClasses.openClass(ClassName.bestGuess(group.name()))
                .addJavadoc(group.description());
        group.settings().forEach(setting -> {
            final var fieldType = setting.mavenType().orElse(setting.type());
            final var field = FieldSpec.builder(fieldType, setting.name(), Modifier.PRIVATE, Modifier.FINAL)
                    .addJavadoc(setting.description());
            setting.defaultValue().ifPresent(value -> initializeField(field, fieldType, value));
            builder.addField(field.build());
        });
        builder.addMethod(asConfiguration(group));
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

    private MethodSpec asConfiguration(final ConfigurationGroup group) {
        final var returnType = ClassName.get(immutablesBasePackage, "Immutable" + group.name());
        final var builder = MethodSpec.methodBuilder("asConfiguration")
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType);
        final var configBuilder = CodeBlock.builder()
                .add("return $T.builder()\n", returnType);
        if ("Files".equals(group.name())) {
            builder.addParameter(ParameterSpec.builder(Path.class, "baseDirectory").addModifiers(Modifier.FINAL).build())
                    .addParameter(ParameterSpec.builder(Path.class, "buildDirectory").addModifiers(Modifier.FINAL).build());
            configBuilder
                    .add(".setInputBaseDirectory(baseDirectory.resolve(inputBaseDirectory))\n")
                    .add(".setOutputBaseDirectory(buildDirectory.resolve(outputBaseDirectory))\n")
                    .add(".setSqlFilesSuffix(sqlFilesSuffix)\n")
                    .add(".setSqlFilesCharset($T.forName(sqlFilesCharset))\n", Charset.class)
                    .add(".setSqlStatementSeparator(sqlStatementSeparator)\n")
                    .add(".setSkipLines(skipLines)\n");
        }
        configBuilder.add(".build()");
        return builder.addStatement(configBuilder.build()).build();
    }

}
