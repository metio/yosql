/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator.gradle;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.javapoet.StandardClasses;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.internals.model.generator.api.AbstractMethodsBasedGenerator;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GradleGenerator extends AbstractMethodsBasedGenerator {

    private final String immutablesBasePackage;

    public GradleGenerator(final String immutablesBasePackage) {
        this.immutablesBasePackage = immutablesBasePackage;
    }

    @Override
    public Stream<TypeSpec> apply(final ConfigurationGroup group) {
        if ("Converter".equalsIgnoreCase(group.name())) {
            return Stream.of(configGroupClass(group),
                    rowConverterClass(true), rowConverterClass(false));
        }
        return Stream.of(configGroupClass(group));
    }

    private TypeSpec configGroupClass(final ConfigurationGroup group) {
        return StandardClasses.abstractClass(ClassName.bestGuess(group.name()))
                .addJavadoc(group.description())
                .addAnnotations(annotationsFor(group))
                .addMethods(properties(group))
                .addMethods(methodsFor(group))
                .addMethod(configureConventions(group))
                .addMethod(asConfiguration(group, immutablesBasePackage))
                .build();
    }

    private TypeSpec rowConverterClass(boolean defaultConverter) {
        final var gradleStringProperty = TypicalTypes.gradlePropertyOf(TypicalTypes.STRING);
        final var className = defaultConverter ? "DefaultRowConverter" : "RowConverter";
        final var classBuilder = StandardClasses.abstractClass(ClassName.bestGuess(className))
                .addJavadoc("Configures a single ResultRowConverter.")
                .addMethod(MethodSpec.constructorBuilder()
                        .addJavadoc("Required by Gradle")
                        .addAnnotation(TypicalTypes.INJECT)
                        .addModifiers(Modifier.PUBLIC)
                        .build());
        if (defaultConverter) {
            classBuilder.addMethod(MethodSpec.methodBuilder("getAlias")
                    .addJavadoc("@return The short alias for the converter.")
                    .addAnnotation(TypicalTypes.GRADLE_INPUT)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(gradleStringProperty)
                    .build());
        } else {
            classBuilder.addSuperinterface(TypicalTypes.GRADLE_NAMED);
        }
        return classBuilder
                .addMethod(MethodSpec.methodBuilder("getConverterType")
                        .addJavadoc("@return The fully-qualified name of the converter class.")
                        .addAnnotation(TypicalTypes.GRADLE_INPUT)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(gradleStringProperty)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getMethodName")
                        .addJavadoc("@return The name of the method to call.")
                        .addAnnotation(TypicalTypes.GRADLE_INPUT)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(gradleStringProperty)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getResultType")
                        .addJavadoc("@return The fully-qualified name of the converter class.")
                        .addAnnotation(TypicalTypes.GRADLE_INPUT)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(gradleStringProperty)
                        .build())
                .addMethod(MethodSpec.methodBuilder("asRowConverter")
                        .returns(ResultRowConverter.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()$>", ResultRowConverter.class)
                                .add(defaultConverter ? "\n.setAlias(getAlias().get())" : "\n.setAlias(getName())")
                                .add("\n.setConverterType(getConverterType().get())")
                                .add("\n.setMethodName(getMethodName().get())")
                                .add("\n.setResultType(getResultType().get())")
                                .add("\n.build()$<")
                                .build())
                        .build())
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
                .filter(setting -> !"rowConverters".equals(setting.name()))
                .forEach(setting -> builder.addStatement(conventionValue(setting)));
        group.settings().stream()
                .filter(setting -> "defaultConverter".equals(setting.name()))
                .forEach(setting -> builder.addStatement(CodeBlock.of("getDefaultConverter().convention(createRowConverter(objects))")));
        return builder.build();
    }

    private CodeBlock conventionValue(final ConfigurationSetting setting) {
        final var type = typeOf(setting);
        if (TypicalTypes.PATH.equals(type)) {
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
        if (TypicalTypes.PATH.equals(type)) {
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

    private String propertyName(final ConfigurationSetting setting) {
        return "get" + Strings.upperCase(setting.name());
    }

    private TypeName gradleReturnType(final ConfigurationSetting setting) {
        final var type = typeOf(setting);
        if (type.isPrimitive()) {
            return TypicalTypes.gradlePropertyOf(type.box());
        }
        if (type.toString().startsWith(TypicalTypes.GRADLE_CONTAINERS.canonicalName())) {
            return type;
        }
        if (type.toString().startsWith(TypicalTypes.LIST.canonicalName())) {
            return TypicalTypes.gradleListPropertyOf(TypicalTypes.STRING);
        }
        if (TypicalTypes.PATH.equals(type)) {
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
                    .add(".set$L($L().get().asRowConverter())\n", Strings.upperCase(setting.name()), propertyName(setting))
                    .build();
        }
        if (usesResultRowConverters(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($L())\n", Strings.upperCase(setting.name()), "createRowConverters")
                    .build();
        }
        if (usesNioPath(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($L().get().getAsFile().toPath())\n", Strings.upperCase(setting.name()), propertyName(setting))
                    .build();
        }
        return CodeBlock.of(".set$L($L().get())\n", Strings.upperCase(setting.name()), propertyName(setting));
    }
}
