/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.*;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.sql.ResultRowConverter;

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
                .addSettings(converterPackageName())
                .addSettings(resultStateClassName())
                .addSettings(resultRowClassName())
                .addSettings(flowStateClassName())
                .setCliMethods(List.of(createCliRowConverters(), createCliRowConverter(), createCliAsRowConverter()))
                .setImmutableMethods(List.of(resultStateClass(), resultRowClass(), flowStateClass()))
                .setGradleMethods(List.of(createGradleRowConverters(), createGradleRowConverter()))
                .setMavenMethods(List.of(createMavenRowConverters(), createMavenRowConverter()))
                .build();
    }

    private static ConfigurationSetting defaultConverter() {
        return ConfigurationSetting.builder()
                .setName("defaultConverter")
                .setDescription("The default converter to use, if no other is specified on a query itself.")
                .setType(TypeName.get(ResultRowConverter.class))
                .setCliType(TypicalTypes.STRING)
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
                .setCliType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setMavenType(TypicalTypes.listOf(ClassName.bestGuess("wtf.metio.yosql.tooling.maven.RowConverter")))
                .setGradleType(TypicalTypes.gradleContainerOf(ClassName.bestGuess("wtf.metio.yosql.tooling.gradle.RowConverter")))
                .setMavenValue("")
                .setValue(CodeBlock.builder()
                        .add("$T.of()", List.class)
                        .build())
                .build();
    }

    private static ConfigurationSetting converterPackageName() {
        return ConfigurationSetting.builder()
                .setName("converterPackageName")
                .setDescription("The package name for all converter related classes.")
                .setType(TypicalTypes.STRING)
                .setValue("com.example.persistence.converter")
                .build();
    }

    private static ConfigurationSetting resultStateClassName() {
        return ConfigurationSetting.builder()
                .setName("resultStateClassName")
                .setDescription("The class name of the result-state class")
                .setType(TypicalTypes.STRING)
                .setValue("ResultState")
                .build();
    }

    private static ConfigurationSetting resultRowClassName() {
        return ConfigurationSetting.builder()
                .setName("resultRowClassName")
                .setDescription("The class name of the result-row class")
                .setType(TypicalTypes.STRING)
                .setValue("ResultRow")
                .build();
    }

    private static ConfigurationSetting flowStateClassName() {
        return ConfigurationSetting.builder()
                .setName("flowStateClassName")
                .setDescription("The class name of the flow-state class")
                .setType(TypicalTypes.STRING)
                .setValue("FlowState")
                .build();
    }

    private static MethodSpec resultStateClass() {
        return MethodSpec.methodBuilder("resultStateClass")
                .addAnnotation(Value.Lazy.class)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(ClassName.class)
                .addStatement("return $T.get($L(), $L())", ClassName.class, "converterPackageName", "resultStateClassName")
                .build();
    }

    private static MethodSpec resultRowClass() {
        return MethodSpec.methodBuilder("resultRowClass")
                .addAnnotation(Value.Lazy.class)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(ClassName.class)
                .addStatement("return $T.get($L(), $L())", ClassName.class, "converterPackageName", "resultRowClassName")
                .build();
    }

    private static MethodSpec flowStateClass() {
        return MethodSpec.methodBuilder("flowStateClass")
                .addAnnotation(Value.Lazy.class)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(ClassName.class)
                .addStatement("return $T.get($L(), $L())", ClassName.class, "converterPackageName", "flowStateClassName")
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
                        .add("$>\n.setAlias($S)", "resultRow")
                        .add("\n.setConverterType(converterPackageName + $S)", ".ToResultRowConverter")
                        .add("\n.setMethodName($S)", "apply")
                        .add("\n.setResultType(converterPackageName + $S + resultRowClassName)", ".")
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
                .addStatement("defaultConverter.getAlias().set($S)", "resultRow")
                .addStatement("defaultConverter.getConverterType().set($S)", "com.example.persistence.converter.ToResultRowConverter")
                .addStatement("defaultConverter.getMethodName().set($S)", "apply")
                .addStatement("defaultConverter.getResultType().set($S)", "com.example.persistence.converter.ResultRow")
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
                        .add("$>\n.setAlias($S)", "resultRow")
                        .add("\n.setConverterType(converterPackageName + $S)", ".ToResultRowConverter")
                        .add("\n.setMethodName($S)", "apply")
                        .add("\n.setResultType(converterPackageName + $S + resultRowClassName)", ".")
                        .add("\n.build())$<$<$<")
                        .build())
                .build();
    }

    private Converter() {
        // data class
    }

}
