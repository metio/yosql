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

public final class Converter {

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
                .build();
    }

    private static ConfigurationSetting defaultConverter() {
        return ConfigurationSetting.builder()
                .setName("defaultConverter")
                .setDescription("The default converter to use, if no other is specified on a query itself.")
                .setType(TypeName.get(ResultRowConverter.class))
                .setCliType(ClassName.get(String.class))
                .setGradleType(ClassName.bestGuess("wtf.metio.yosql.tooling.gradle.DefaultRowConverter"))
                .setMavenType(ClassName.bestGuess("wtf.metio.yosql.tooling.maven.RowConverter"))
                .setCliValue("")
                .setMavenValue("")
                .build();
    }

    private static ConfigurationSetting rowConverters() {
        return ConfigurationSetting.builder()
                .setName("rowConverters")
                .setDescription("The converters configured by the user.")
                .setType(TypicalTypes.listOf(ResultRowConverter.class))
                .setCliType(TypicalTypes.listOf(ClassName.get(String.class)))
                .setMavenType(TypicalTypes.listOf(ClassName.bestGuess("wtf.metio.yosql.tooling.maven.RowConverter")))
                .setGradleType(TypicalTypes.gradleContainerOf(ClassName.bestGuess("wtf.metio.yosql.tooling.gradle.RowConverter")))
                .setMavenValue("")
                .setValue(CodeBlock.builder()
                        .add("$T.of()", List.class)
                        .build())
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
                .setName("mapConverterClass")
                .setDescription("The fully-qualified class name of the ToMap converter.")
                .setType(ClassName.get(String.class))
                .setValue("com.example.persistence.converter.ToMapConverter")
                .build();
    }

    private static ConfigurationSetting mapConverterMethod() {
        return ConfigurationSetting.builder()
                .setName("mapConverterMethod")
                .setDescription("The name of the method to generate/call in the ToMap converter.")
                .setType(ClassName.get(String.class))
                .setValue("apply")
                .build();
    }

    private static ConfigurationSetting mapConverterAlias() {
        return ConfigurationSetting.builder()
                .setName("mapConverterAlias")
                .setDescription("The name of the alias referencing the ToMap converter.")
                .setType(ClassName.get(String.class))
                .setValue("toMap")
                .build();
    }

    private static MethodSpec createCliRowConverters() {
        return MethodSpec.methodBuilder("createRowConverters")
                .addModifiers(Modifier.FINAL)
                .returns(TypicalTypes.listOf(ResultRowConverter.class))
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable(rowConverters)", Stream.class)
                        .add("$>$>\n.flatMap($T::stream)", List.class)
                        .add("\n.map(this::asRowConverter)")
                        .add("\n.filter($T::nonNull)", Objects.class)
                        .add("\n.collect($T.toList())$<$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec createCliRowConverter() {
        return MethodSpec.methodBuilder("createRowConverter")
                .addModifiers(Modifier.FINAL)
                .returns(ResultRowConverter.class)
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable(defaultConverter)", Optional.class)
                        .add("$>$>\n.map($T::strip)", String.class)
                        .add("\n.filter($T.not($T::isBlank))", Predicate.class, Strings.class)
                        .add("\n.map(this::asRowConverter)")
                        .add("\n.filter($T::nonNull)", Objects.class)
                        .add("\n.orElse(ResultRowConverter.builder()")
                        .add("$>\n.setAlias(mapConverterAlias)")
                        .add("\n.setConverterType(mapConverterClass)")
                        .add("\n.setMethodName(mapConverterMethod)")
                        .add("\n.setResultType($S)", "java.util.Map<String, Object>")
                        .add("\n.build())$<$<$<")
                        .build())
                .build();
    }

    private static MethodSpec createCliAsRowConverter() {
        return MethodSpec.methodBuilder("asRowConverter")
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
        return MethodSpec.methodBuilder("createRowConverters")
                .addModifiers(Modifier.PRIVATE)
                .returns(TypicalTypes.listOf(ResultRowConverter.class))
                .addStatement(CodeBlock.builder()
                        .add("return getRowConverters().stream()", Stream.class)
                        .add("$>\n.map($T::asRowConverter)", ClassName.bestGuess("wtf.metio.yosql.tooling.gradle.RowConverter"))
                        .add("\n.collect($T.toList())$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec createGradleRowConverter() {
        final var defaultConverterType = ClassName.bestGuess("wtf.metio.yosql.tooling.gradle.DefaultRowConverter");
        return MethodSpec.methodBuilder("createRowConverter")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(TypicalTypes.GRADLE_OBJECTS, "objects", Modifier.FINAL).build())
                .returns(defaultConverterType)
                .addStatement("final var defaultConverter = objects.newInstance($T.class)", defaultConverterType)
                .addStatement("defaultConverter.getAlias().set(getMapConverterAlias())")
                .addStatement("defaultConverter.getConverterType().set(getMapConverterClass())")
                .addStatement("defaultConverter.getMethodName().set(getMapConverterMethod())")
                .addStatement("defaultConverter.getResultType().set($S)", "java.util.Map<String, Object>")
                .addStatement("return defaultConverter")
                .build();
    }

    private static MethodSpec createMavenRowConverters() {
        return MethodSpec.methodBuilder("createRowConverters")
                .addModifiers(Modifier.FINAL)
                .returns(TypicalTypes.listOf(ResultRowConverter.class))
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable(rowConverters)", Stream.class)
                        .add("$>$>\n.flatMap($T::stream)", List.class)
                        .add("\n.map($T::asRowConverter)", ClassName.bestGuess("wtf.metio.yosql.tooling.maven.RowConverter"))
                        .add("\n.filter($T::nonNull)", Objects.class)
                        .add("\n.collect($T.toList())$<$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec createMavenRowConverter() {
        return MethodSpec.methodBuilder("createRowConverter")
                .addModifiers(Modifier.FINAL)
                .returns(ResultRowConverter.class)
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable(defaultConverter)", Optional.class)
                        .add("$>$>\n.map($T::asRowConverter)", ClassName.bestGuess("wtf.metio.yosql.tooling.maven.RowConverter"))
                        .add("\n.orElse(ResultRowConverter.builder()")
                        .add("$>\n.setAlias(mapConverterAlias)")
                        .add("\n.setConverterType(mapConverterClass)")
                        .add("\n.setMethodName(mapConverterMethod)")
                        .add("\n.setResultType($S)", "java.util.Map<String, Object>")
                        .add("\n.build())$<$<$<")
                        .build())
                .build();
    }

    private Converter() {
        // data class
    }

}
