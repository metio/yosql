/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static wtf.metio.yosql.internals.javapoet.TypicalTypes.*;
import static wtf.metio.yosql.internals.jdk.Strings.upperCase;

public final class Converter extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Converter.class.getSimpleName();
    private static final String ROW_CONVERTER = "RowConverterSpec";
    private static final ClassName ANT_ROW_CONVERTER_TYPE = antType(ROW_CONVERTER);
    private static final ClassName MAVEN_ROW_CONVERTER_TYPE = mavenType(ROW_CONVERTER);
    private static final String AS_ROW_CONVERTER = "asRowConverter";
    private static final String CREATE_ROW_CONVERTERS = "createRowConverters";
    private static final String CREATE_ROW_CONVERTER = "createRowConverter";
    private static final String ROW_CONVERTERS = "rowConverters";
    private static final String MAP_CONVERTER_ALIAS = "mapConverterAlias";
    private static final String MAP_CONVERTER_METHOD = "mapConverterMethod";
    private static final String MAP_CONVERTER_CLASS = "mapConverterClass";
    private static final String DEFAULT_ROW_CONVERTER = "DefaultRowConverter";
    private static final ClassName GRADLE_DEFAULT_ROW_CONVERTER_TYPE = gradleType(DEFAULT_ROW_CONVERTER);
    private static final String DEFAULT_CONVERTER = "defaultConverter";
    private static final String ALIAS = "alias";
    private static final String CONVERTER_TYPE = "converterType";
    private static final String METHOD_NAME = "methodName";
    private static final String RESULT_TYPE = "resultType";
    private static final String OBJECTS = "objects";

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures converter related settings.")
                .addSettings(defaultConverter())
                .addSettings(rowConverters())
                .addSettings(generateMapConverter())
                .addSettings(mapConverterClass())
                .addSettings(mapConverterMethod())
                .addSettings(mapConverterAlias())
                .addAntTypes(antRowConverterType())
                .addAntMethods(createAntRowConverters(), createAntRowConverter())
                .addCliMethods(createCliRowConverters(), createCliRowConverter())
                .addGradleMethods(createGradleRowConverters(), createGradleRowConverter())
                .addGradleTypes(gradleRowConverterType(true), gradleRowConverterType(false))
                .addGradleConventionParameters(ParameterSpec.builder(GRADLE_OBJECTS, OBJECTS, Modifier.FINAL).build())
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .addMavenMethods(createMavenRowConverters(), createMavenRowConverter())
                .addMavenTypes(mavenRowConverterType())
                .build();
    }

    private static ConfigurationSetting defaultConverter() {
        final var name = DEFAULT_CONVERTER;
        final var description = "The default converter to use, if no other is specified on a query itself.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setAntInitializer(CodeBlock.of(".set$L($L($L))\n", upperCase(name), CREATE_ROW_CONVERTER, name))
                .setCliInitializer(CodeBlock.of(".set$L($L($L))\n", upperCase(name), CREATE_ROW_CONVERTER, name))
                .setGradleInitializer(CodeBlock.of(".set$L($L().get().asRowConverter())\n", upperCase(name), gradlePropertyName(name)))
                .setMavenInitializer(CodeBlock.of(".set$L($L($L))\n", upperCase(name), CREATE_ROW_CONVERTER, name))
                .setGradleConvention(CodeBlock.of("$L().convention($L($N))", gradlePropertyName(name), CREATE_ROW_CONVERTER, OBJECTS))
                .addAntFields(antField(ANT_ROW_CONVERTER_TYPE, name, description))
                .addAntMethods(antSetter(ANT_ROW_CONVERTER_TYPE, name, description))
                .addCliFields(picocliOption(ClassName.get(String.class), GROUP_NAME, name, description, ""))
                .addGradleMethods(gradleProperty(gradlePropertyOf(GRADLE_DEFAULT_ROW_CONVERTER_TYPE), name, description))
                .addImmutableMethods(immutableMethod(ClassName.get(ResultRowConverter.class), name, description))
                .addMavenFields(mavenParameter(MAVEN_ROW_CONVERTER_TYPE, name, description, "${classObject}"))
                .build();
    }

    private static ConfigurationSetting rowConverters() {
        final var name = ROW_CONVERTERS;
        final var description = "The converters configured by the user.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setAntInitializer(CodeBlock.of(".addAll$L($L($L))\n", upperCase(name), CREATE_ROW_CONVERTERS, name))
                .setCliInitializer(CodeBlock.of(".addAll$L($L($L))\n", upperCase(name), CREATE_ROW_CONVERTERS, name))
                .setGradleInitializer(CodeBlock.of(".addAll$L($L())\n", upperCase(name), CREATE_ROW_CONVERTERS))
                .setMavenInitializer(CodeBlock.of(".addAll$L($L($L))\n", upperCase(name), CREATE_ROW_CONVERTERS, name))
                .addAntFields(antField(listOf(ANT_ROW_CONVERTER_TYPE), name, description, CodeBlock.of("new $T<>()", ArrayList.class)))
                .addAntMethods(antAdder(ANT_ROW_CONVERTER_TYPE, name, description))
                .addCliFields(picocliOption(listOf(String.class), GROUP_NAME, name, description))
                .addGradleMethods(gradleProperty(gradleContainerOf(gradleType(ROW_CONVERTER)), name, description))
                .addImmutableMethods(immutableMethod(listOf(ResultRowConverter.class), name, description))
                .addMavenFields(mavenParameter(listOf(MAVEN_ROW_CONVERTER_TYPE), name, description, "", CodeBlock.of("new $T<>()", ArrayList.class)))
                .build();
    }

    private static ConfigurationSetting generateMapConverter() {
        final var name = "generateMapConverter";
        final var description = "Whether the ToMap converter should be generated.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .build();
    }

    private static ConfigurationSetting mapConverterClass() {
        final var description = "The fully-qualified class name of the ToMap converter.";
        final var value = "com.example.persistence.converter.ToMapConverter";
        return setting(GROUP_NAME, MAP_CONVERTER_CLASS, description, value)
                .build();
    }

    private static ConfigurationSetting mapConverterMethod() {
        final var description = "The name of the method to generate/call in the ToMap converter.";
        final var value = "apply";
        return setting(GROUP_NAME, MAP_CONVERTER_METHOD, description, value)
                .build();
    }

    private static ConfigurationSetting mapConverterAlias() {
        final var description = "The name of the alias referencing the ToMap converter.";
        final var value = "toMap";
        return setting(GROUP_NAME, MAP_CONVERTER_ALIAS, description, value)
                .build();
    }

    private static MethodSpec createAntRowConverters() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTERS)
                .addModifiers(Modifier.FINAL)
                .returns(listOf(ResultRowConverter.class))
                .addParameter(ParameterSpec.builder(listOf(ANT_ROW_CONVERTER_TYPE), "specs", Modifier.FINAL).build())
                .addStatement("return $T.ofNullable($L).flatMap($T::stream).map($T::$L).toList()",
                        Stream.class, "specs", Collection.class, ANT_ROW_CONVERTER_TYPE, AS_ROW_CONVERTER)
                .build();
    }

    private static MethodSpec createAntRowConverter() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTER)
                .addModifiers(Modifier.FINAL)
                .returns(ResultRowConverter.class)
                .addParameter(ParameterSpec.builder(ANT_ROW_CONVERTER_TYPE, "spec", Modifier.FINAL).build())
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Optional.class, "spec")
                        .add("\n.map($T::$L)", ANT_ROW_CONVERTER_TYPE, AS_ROW_CONVERTER)
                        .add("\n.orElse($T.builder()", ResultRowConverter.class)
                        .add("$>\n.setAlias($L)", MAP_CONVERTER_ALIAS)
                        .add("\n.setConverterType($L)", MAP_CONVERTER_CLASS)
                        .add("\n.setMethodName($L)", MAP_CONVERTER_METHOD)
                        .add("\n.setResultType($S)", "java.util.Map<String, Object>")
                        .add("\n.build())$<")
                        .build())
                .build();
    }

    private static MethodSpec createCliRowConverters() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTERS)
                .addModifiers(Modifier.FINAL)
                .returns(listOf(ResultRowConverter.class))
                .addParameter(ParameterSpec.builder(listOf(String.class), "specs", Modifier.FINAL).build())
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Stream.class, "specs")
                        .add("$>$>\n.flatMap($T::stream)", List.class)
                        .add("\n.map($T::fromString)", ResultRowConverter.class)
                        .add("\n.filter($T::nonNull)", Objects.class)
                        .add("\n.toList()$<$<")
                        .build())
                .build();
    }

    private static MethodSpec createCliRowConverter() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTER)
                .addModifiers(Modifier.FINAL)
                .returns(ResultRowConverter.class)
                .addParameter(ParameterSpec.builder(String.class, "spec", Modifier.FINAL).build())
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Optional.class, "spec")
                        .add("$>$>\n.map($T::strip)", String.class)
                        .add("\n.filter($T.not($T::isBlank))", Predicate.class, Strings.class)
                        .add("\n.map($T::fromString)", ResultRowConverter.class)
                        .add("\n.filter($T::nonNull)", Objects.class)
                        .add("\n.orElse($T.builder()", ResultRowConverter.class)
                        .add("$>\n.setAlias($L)", MAP_CONVERTER_ALIAS)
                        .add("\n.setConverterType($L)", MAP_CONVERTER_CLASS)
                        .add("\n.setMethodName($L)", MAP_CONVERTER_METHOD)
                        .add("\n.setResultType($S)", "java.util.Map<String, Object>")
                        .add("\n.build())$<$<$<")
                        .build())
                .build();
    }

    private static MethodSpec createGradleRowConverters() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTERS)
                .addModifiers(Modifier.PRIVATE)
                .returns(listOf(ResultRowConverter.class))
                .addStatement(CodeBlock.builder()
                        .add("return get$L().stream()", upperCase(ROW_CONVERTERS))
                        .add("$>\n.map($T::$L)", gradleType(ROW_CONVERTER), AS_ROW_CONVERTER)
                        .add("\n.collect($T.toList())$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec createGradleRowConverter() {
        final var defaultConverterType = GRADLE_DEFAULT_ROW_CONVERTER_TYPE;
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTER)
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(GRADLE_OBJECTS, OBJECTS, Modifier.FINAL).build())
                .returns(defaultConverterType)
                .addStatement("final var defaultConverter = objects.newInstance($T.class)", defaultConverterType)
                .addStatement("defaultConverter.getAlias().set(get$L())", upperCase(MAP_CONVERTER_ALIAS))
                .addStatement("defaultConverter.getConverterType().set(get$L())", upperCase(MAP_CONVERTER_CLASS))
                .addStatement("defaultConverter.getMethodName().set(get$L())", upperCase(MAP_CONVERTER_METHOD))
                .addStatement("defaultConverter.getResultType().set($S)", "java.util.Map<String, Object>")
                .addStatement("return defaultConverter")
                .build();
    }

    private static MethodSpec createMavenRowConverters() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTERS)
                .addModifiers(Modifier.FINAL)
                .returns(listOf(ResultRowConverter.class))
                .addParameter(ParameterSpec.builder(listOf(MAVEN_ROW_CONVERTER_TYPE), "specs", Modifier.FINAL).build())
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Stream.class, "specs")
                        .add("$>$>\n.flatMap($T::stream)", List.class)
                        .add("\n.map($T::$L)", MAVEN_ROW_CONVERTER_TYPE, AS_ROW_CONVERTER)
                        .add("\n.filter($T::nonNull)", Objects.class)
                        .add("\n.toList()$<$<")
                        .build())
                .build();
    }

    private static MethodSpec createMavenRowConverter() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTER)
                .addModifiers(Modifier.FINAL)
                .returns(ResultRowConverter.class)
                .addParameter(ParameterSpec.builder(MAVEN_ROW_CONVERTER_TYPE, "spec", Modifier.FINAL).build())
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Optional.class, "spec")
                        .add("$>$>\n.map($T::$L)", MAVEN_ROW_CONVERTER_TYPE, AS_ROW_CONVERTER)
                        .add("\n.orElse($T.builder()", ResultRowConverter.class)
                        .add("$>\n.setAlias($L)", MAP_CONVERTER_ALIAS)
                        .add("\n.setConverterType($L)", MAP_CONVERTER_CLASS)
                        .add("\n.setMethodName($L)", MAP_CONVERTER_METHOD)
                        .add("\n.setResultType($S)", "java.util.Map<String, Object>")
                        .add("\n.build())$<$<$<")
                        .build())
                .build();
    }

    private static TypeSpec antRowConverterType() {
        return TypeSpec.classBuilder(ROW_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(ClassName.get(String.class), ALIAS).build())
                .addField(FieldSpec.builder(ClassName.get(String.class), CONVERTER_TYPE).build())
                .addField(FieldSpec.builder(ClassName.get(String.class), METHOD_NAME).build())
                .addField(FieldSpec.builder(ClassName.get(String.class), RESULT_TYPE).build())
                .addMethod(MethodSpec.methodBuilder(AS_ROW_CONVERTER)
                        .addModifiers(Modifier.FINAL)
                        .returns(ResultRowConverter.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()", ResultRowConverter.class)
                                .add("$>\n.setAlias($L)", ALIAS)
                                .add("\n.setConverterType($L)", CONVERTER_TYPE)
                                .add("\n.setMethodName($L)", METHOD_NAME)
                                .add("\n.setResultType($L)", RESULT_TYPE)
                                .add("\n.build()$<")
                                .build())
                        .build())
                .build();
    }

    private static TypeSpec mavenRowConverterType() {
        return TypeSpec.classBuilder(ROW_CONVERTER)
                .addModifiers(Modifier.PUBLIC)
                .addField(mavenStringParameter(ALIAS))
                .addField(mavenStringParameter(CONVERTER_TYPE))
                .addField(mavenStringParameter(METHOD_NAME))
                .addField(mavenStringParameter(RESULT_TYPE))
                .addMethod(MethodSpec.methodBuilder(AS_ROW_CONVERTER)
                        .addModifiers(Modifier.FINAL)
                        .returns(ResultRowConverter.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()", ResultRowConverter.class)
                                .add("$>\n.setAlias($L)", ALIAS)
                                .add("\n.setConverterType($L)", CONVERTER_TYPE)
                                .add("\n.setMethodName($L)", METHOD_NAME)
                                .add("\n.setResultType($L)", RESULT_TYPE)
                                .add("\n.build()$<")
                                .build())
                        .build())
                .build();
    }

    private static TypeSpec gradleRowConverterType(boolean defaultConverter) {
        final var className = defaultConverter ? DEFAULT_ROW_CONVERTER : ROW_CONVERTER;
        final var classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("Configures a single ResultRowConverter.")
                .addMethod(gradleConstructor());
        if (defaultConverter) {
            classBuilder.addMethod(gradleStringProperty(ALIAS, "The short alias for the converter"));
        } else {
            classBuilder.addSuperinterface(GRADLE_NAMED);
        }
        return classBuilder
                .addMethod(gradleStringProperty(CONVERTER_TYPE, "The fully-qualified name of the converter class"))
                .addMethod(gradleStringProperty(METHOD_NAME, "The name of the method to call"))
                .addMethod(gradleStringProperty(RESULT_TYPE, "The fully-qualified name of the converter class"))
                .addMethod(MethodSpec.methodBuilder(AS_ROW_CONVERTER)
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

    private Converter() {
        // data class
    }

}
