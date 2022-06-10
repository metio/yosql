/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.generator;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static wtf.metio.yosql.internals.jdk.Strings.upperCase;

public final class GradleGenerator extends AbstractMethodsBasedGenerator {

    private final String immutablesBasePackage;

    public GradleGenerator(final String immutablesBasePackage) {
        this.immutablesBasePackage = immutablesBasePackage;
    }

    @Override
    public Stream<TypeSpec> apply(final ConfigurationGroup group) {
        return Stream.concat(Stream.of(configGroupClass(group)), group.gradleTypes().stream());
    }

    private TypeSpec configGroupClass(final ConfigurationGroup group) {
        return TypeSpec.classBuilder(ClassName.bestGuess(group.name()))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc(group.description())
                .addAnnotations(annotationsFor(group))
                .addMethods(properties(group))
                .addMethods(methodsFor(group))
                .addMethod(configureConventions(group))
                .addMethod(asConfiguration(group, immutablesBasePackage))
                .build();
    }

    private List<MethodSpec> properties(final ConfigurationGroup group) {
        return group.settings().stream()
                .map(this::defaultMethod)
                .collect(Collectors.toList());
    }

    private MethodSpec configureConventions(final ConfigurationGroup group) {
        final var builder = MethodSpec.methodBuilder("configureConventions");
        group.settings().stream()
                .filter(this::usesNioPath)
                .findAny()
                .ifPresent(s -> builder.addParameter(ParameterSpec.builder(TypicalTypes.GRADLE_LAYOUT, "layout").build()));
        group.settings().stream()
                .filter(this::usesRowConverters)
                .findAny()
                .ifPresent(s -> builder.addParameter(ParameterSpec.builder(TypicalTypes.GRADLE_OBJECTS, "objects").build()));
        group.settings().stream()
                .filter(setting -> valueOf(setting).isPresent())
                .filter(setting -> !typeOf(setting).toString().startsWith(TypicalTypes.GRADLE_CONTAINERS.canonicalName()))
                .forEach(setting -> builder.addStatement(conventionValue(setting)));
        group.settings().stream()
                .filter(setting -> "defaultConverter".equals(setting.name()))
                .forEach(setting -> builder.addStatement(CodeBlock.of("getDefaultConverter().convention(createRowConverter(objects))")));
        return builder.build();
    }

    private CodeBlock conventionValue(final ConfigurationSetting setting) {
        final var type = typeOf(setting);
        if (ClassName.get(Path.class).equals(type)) {
            if ("inputBaseDirectory".equals(setting.name())) {
                return CodeBlock.of("$L().convention($N.getProjectDirectory().dir($S))", propertyName(setting), "layout", valueOf(setting).orElseThrow());
            }
            if ("outputBaseDirectory".equals(setting.name())) {
                return CodeBlock.of("$L().convention($N.getBuildDirectory().dir($S))", propertyName(setting), "layout", valueOf(setting).orElseThrow());
            }
        }
        return CodeBlock.of("$L().convention($L)", propertyName(setting), defaultValue(valueOf(setting).orElseThrow(), typeOf(setting)));
    }

    @Override
    public TypeName typeOf(final ConfigurationSetting setting) {
        return setting.gradleType().orElse(setting.type());
    }

    @Override
    public Optional<Object> valueOf(final ConfigurationSetting setting) {
        return setting.gradleValue().or(setting::value);
    }

    @Override
    public List<AnnotationSpec> annotationsFor(final ConfigurationGroup group) {
        return group.gradleAnnotations();
    }

    @Override
    public List<AnnotationSpec> annotationsFor(final ConfigurationSetting setting) {
        return Stream.concat(
                        inputAnnotation(setting),
                        setting.gradleAnnotations().stream())
                .toList();
    }

    private Stream<AnnotationSpec> inputAnnotation(final ConfigurationSetting setting) {
        final var type = typeOf(setting);
        if (ClassName.get(Path.class).equals(type)) {
            return Stream.of();
        }
        return Stream.of(AnnotationSpec.builder(TypicalTypes.GRADLE_INPUT).build());
    }

    @Override
    public List<MethodSpec> methodsFor(final ConfigurationGroup group) {
        return group.gradleMethods();
    }

    @Override
    protected MethodSpec defaultMethod(final ConfigurationSetting setting) {
        return MethodSpec.methodBuilder(propertyName(setting))
                .addJavadoc("@return " + setting.description())
                .addAnnotations(annotationsFor(setting))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(gradleReturnType(setting))
                .build();
    }

    private static String propertyName(final ConfigurationSetting setting) {
        return "get" + upperCase(setting.name());
    }

    private TypeName gradleReturnType(final ConfigurationSetting setting) {
        final var type = typeOf(setting);
        if (type.isPrimitive()) {
            return TypicalTypes.gradlePropertyOf(type.box());
        }
        if (type.toString().startsWith(TypicalTypes.GRADLE_CONTAINERS.canonicalName())
                || type.toString().startsWith(TypicalTypes.GRADLE_LIST_PROPERTY.canonicalName())) {
            return type;
        }
        if (type.toString().startsWith(ClassName.get(List.class).canonicalName())) {
            return TypicalTypes.gradleListPropertyOf(ClassName.get(String.class));
        }
        if (ClassName.get(Path.class).equals(type)) {
            return TypicalTypes.GRADLE_DIRECTORY;
        }
        return TypicalTypes.gradlePropertyOf(type);
    }

    private MethodSpec asConfiguration(final ConfigurationGroup group, final String immutablesBasePackage) {
        final var returnType = ClassName.get(immutablesBasePackage, group.configurationName());
        final var builder = MethodSpec.methodBuilder("asConfiguration")
                .returns(returnType);
        final var configBuilder = CodeBlock.builder()
                .add("return $T.builder()\n", returnType);
        group.settings().stream()
                .map(this::methodConfiguration)
                .forEach(configBuilder::add);
        configBuilder.add(".build()");
        return builder.addStatement(configBuilder.build()).build();
    }

    private CodeBlock methodConfiguration(final ConfigurationSetting setting) {
        if (usesResultRowConverter(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($L().get().asRowConverter())\n", upperCase(setting.name()), propertyName(setting))
                    .build();
        }
        if (usesResultRowConverters(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($L())\n", upperCase(setting.name()), "createRowConverters")
                    .build();
        }
        if (usesNioPath(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($L().get().getAsFile().toPath())\n", upperCase(setting.name()), propertyName(setting))
                    .build();
        }
        if (usesAnnotations(setting)) {
            return CodeBlock.builder()
                    .add(".set$L(createAnnotations($L().stream().toList()))\n", upperCase(setting.name()), propertyName(setting))
                    .build();
        }
        return CodeBlock.of(".set$L($L().get())\n", upperCase(setting.name()), propertyName(setting));
    }
}
