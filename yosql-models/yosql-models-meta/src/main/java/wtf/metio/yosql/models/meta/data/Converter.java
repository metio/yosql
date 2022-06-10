/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static wtf.metio.yosql.internals.jdk.Strings.upperCase;

public final class Converter extends AbstractConfigurationGroup {

    private static final String ROW_CONVERTER = "RowConverter";
    private static final String AS_ROW_CONVERTER = "asRowConverter";
    private static final String CREATE_ROW_CONVERTERS = "createRowConverters";
    private static final String CREATE_ROW_CONVERTER = "createRowConverter";
    private static final String ROW_CONVERTERS = "rowConverters";
    private static final String MAP_CONVERTER_ALIAS = "mapConverterAlias";
    private static final String MAP_CONVERTER_METHOD = "mapConverterMethod";
    private static final String MAP_CONVERTER_CLASS = "mapConverterClass";
    private static final String DEFAULT_ROW_CONVERTER = "DefaultRowConverter";
    private static final String DEFAULT_CONVERTER = "defaultConverter";
    private static final String ALIAS = "alias";
    private static final String CONVERTER_TYPE = "converterType";
    private static final String METHOD_NAME = "methodName";
    private static final String RESULT_TYPE = "resultType";

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName("Converter")
                .setDescription("Configures converter related settings.")
                .addSettings(defaultConverter())
                .addSettings(rowConverters())
                .addSettings(generateMapConverter())
                .addSettings(mapConverterClass())
                .addSettings(mapConverterMethod())
                .addSettings(mapConverterAlias())
                .setCliMethods(List.of(createCliRowConverters(), createCliRowConverter(), createCliAsRowConverter()))
                .setGradleMethods(List.of(createGradleRowConverters(), createGradleRowConverter()))
                .setMavenMethods(List.of(createMavenRowConverters(), createMavenRowConverter()))
                .addMavenTypes(mavenRowConverterType())
                .addGradleTypes(gradleRowConverterType(true), gradleRowConverterType(false))
                .build();
    }

    private static ConfigurationSetting defaultConverter() {
        return ConfigurationSetting.builder()
                .setName(DEFAULT_CONVERTER)
                .setDescription("The default converter to use, if no other is specified on a query itself.")
                .setType(TypeName.get(ResultRowConverter.class))
                .setCliType(ClassName.get(String.class))
                .setGradleType(gradleType(DEFAULT_ROW_CONVERTER))
                .setMavenType(mavenType(ROW_CONVERTER))
                .setCliValue("")
                .setMavenValue("")
                .build();
    }

    private static ConfigurationSetting rowConverters() {
        return ConfigurationSetting.builder()
                .setName(ROW_CONVERTERS)
                .setDescription("The converters configured by the user.")
                .setType(TypicalTypes.listOf(ResultRowConverter.class))
                .setCliType(TypicalTypes.listOf(ClassName.get(String.class)))
                .setMavenType(TypicalTypes.listOf(mavenType(ROW_CONVERTER)))
                .setGradleType(TypicalTypes.gradleContainerOf(gradleType(ROW_CONVERTER)))
                .setMavenValue("")
                .setValue(CodeBlock.builder().add("$T.of()", List.class).build())
                .build();
    }

    private static ConfigurationSetting generateMapConverter() {
        return ConfigurationSetting.builder()
                .setName("generateMapConverter")
                .setDescription("Whether the ToMap converter should be generated.")
                .setType(ClassName.get(Boolean.class))
                .setValue(true)
                .build();
    }

    private static ConfigurationSetting mapConverterClass() {
        return ConfigurationSetting.builder()
                .setName(MAP_CONVERTER_CLASS)
                .setDescription("The fully-qualified class name of the ToMap converter.")
                .setType(ClassName.get(String.class))
                .setValue("com.example.persistence.converter.ToMapConverter")
                .build();
    }

    private static ConfigurationSetting mapConverterMethod() {
        return ConfigurationSetting.builder()
                .setName(MAP_CONVERTER_METHOD)
                .setDescription("The name of the method to generate/call in the ToMap converter.")
                .setType(ClassName.get(String.class))
                .setValue("apply")
                .build();
    }

    private static ConfigurationSetting mapConverterAlias() {
        return ConfigurationSetting.builder()
                .setName(MAP_CONVERTER_ALIAS)
                .setDescription("The name of the alias referencing the ToMap converter.")
                .setType(ClassName.get(String.class))
                .setValue("toMap")
                .build();
    }

    private static MethodSpec createCliRowConverters() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTERS)
                .addModifiers(Modifier.FINAL)
                .returns(TypicalTypes.listOf(ResultRowConverter.class))
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Stream.class, ROW_CONVERTERS)
                        .add("$>$>\n.flatMap($T::stream)", List.class)
                        .add("\n.map(this::$L)", AS_ROW_CONVERTER)
                        .add("\n.filter($T::nonNull)", Objects.class)
                        .add("\n.collect($T.toList())$<$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec createCliRowConverter() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTER)
                .addModifiers(Modifier.FINAL)
                .returns(ResultRowConverter.class)
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Optional.class, DEFAULT_CONVERTER)
                        .add("$>$>\n.map($T::strip)", String.class)
                        .add("\n.filter($T.not($T::isBlank))", Predicate.class, Strings.class)
                        .add("\n.map(this::$L)", AS_ROW_CONVERTER)
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

    private static MethodSpec createCliAsRowConverter() {
        return MethodSpec.methodBuilder(AS_ROW_CONVERTER)
                .addModifiers(Modifier.FINAL)
                .returns(ResultRowConverter.class)
                .addParameter(String.class, "converterDefinition", Modifier.FINAL)
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable(converterDefinition)", Optional.class)
                        .add("$>$>\n.map($T::strip)", String.class)
                        .add("\n.filter($T.not($T::isBlank))", Predicate.class, Strings.class)
                        .add("\n.map(value -> value.split($S))", ":")
                        .add("\n.map(values -> $T.builder()", ResultRowConverter.class)
                        .add("$>\n.setAlias(values[0])")
                        .add("\n.setConverterType(values[1])")
                        .add("\n.setMethodName(values[2])")
                        .add("\n.setResultType(values[3])$<")
                        .add("\n.build())")
                        .add("\n.orElse(null)$<$<")
                        .build())
                .build();
    }

    private static MethodSpec createGradleRowConverters() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTERS)
                .addModifiers(Modifier.PRIVATE)
                .returns(TypicalTypes.listOf(ResultRowConverter.class))
                .addStatement(CodeBlock.builder()
                        .add("return get$L().stream()", upperCase(ROW_CONVERTERS))
                        .add("$>\n.map($T::$L)", gradleType(ROW_CONVERTER), AS_ROW_CONVERTER)
                        .add("\n.collect($T.toList())$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec createGradleRowConverter() {
        final var defaultConverterType = gradleType(DEFAULT_ROW_CONVERTER);
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTER)
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(TypicalTypes.GRADLE_OBJECTS, "objects", Modifier.FINAL).build())
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
                .returns(TypicalTypes.listOf(ResultRowConverter.class))
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Stream.class, ROW_CONVERTERS)
                        .add("$>$>\n.flatMap($T::stream)", List.class)
                        .add("\n.map($T::$L)", mavenType(ROW_CONVERTER), AS_ROW_CONVERTER)
                        .add("\n.filter($T::nonNull)", Objects.class)
                        .add("\n.collect($T.toList())$<$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec createMavenRowConverter() {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTER)
                .addModifiers(Modifier.FINAL)
                .returns(ResultRowConverter.class)
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Optional.class, DEFAULT_CONVERTER)
                        .add("$>$>\n.map($T::$L)", mavenType(ROW_CONVERTER), AS_ROW_CONVERTER)
                        .add("\n.orElse($T.builder()", ResultRowConverter.class)
                        .add("$>\n.setAlias($L)", MAP_CONVERTER_ALIAS)
                        .add("\n.setConverterType($L)", MAP_CONVERTER_CLASS)
                        .add("\n.setMethodName($L)", MAP_CONVERTER_METHOD)
                        .add("\n.setResultType($S)", "java.util.Map<String, Object>")
                        .add("\n.build())$<$<$<")
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
            classBuilder.addMethod(gradleStringProperty("getAlias", "The short alias for the converter"));
        } else {
            classBuilder.addSuperinterface(TypicalTypes.GRADLE_NAMED);
        }
        return classBuilder
                .addMethod(gradleStringProperty("getConverterType", "The fully-qualified name of the converter class"))
                .addMethod(gradleStringProperty("getMethodName", "The name of the method to call"))
                .addMethod(gradleStringProperty("getResultType", "The fully-qualified name of the converter class"))
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
