/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.javapoet.*;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.constants.sql.ReturningMode;
import wtf.metio.yosql.models.constants.sql.SqlType;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.sql.ResultRowConverter;
import wtf.metio.yosql.models.sql.SqlParameter;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Sql {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Sql.class.getSimpleName())
                .setDescription("The configuration for a single SQL statement.")
                .setImmutableAnnotations(extraTypeAnnotations())
                .addAllSettings(settings())
                .addAllSettings(withExtraAnnotations(Repositories.stringMethods())) // TODO: turn all repo settings into Optionals
                .addAllSettings(withExtraAnnotations(booleanRepositorySettings()))
                .setImmutableMethods(List.of(
                        fromStatements(),
                        merge(),
                        mergeParameters(),
                        batchName(),
                        blockingName(),
                        mutinyName(),
                        reactorName(),
                        rxJavaName(),
                        streamName(),
                        joinMethodNameParts()))
                .build();
    }

    private static List<ConfigurationSetting> settings() {
        return List.of(
                repository(),
                name(),
                description(),
                vendor(),
                type(),
                returningMode(),
                resultRowConverter(),
                parameters());
    }

    private static List<ConfigurationSetting> booleanRepositorySettings() {
        return Repositories.booleanMethods().stream()
                .map(setting -> ConfigurationSetting.copyOf(setting)
                        .withType(TypicalTypes.BOOLEAN)
                        .withValue(Optional.empty()))
                .collect(Collectors.toList());
    }

    private static List<? extends ConfigurationSetting> withExtraAnnotations(final List<ConfigurationSetting> settings) {
        return settings.stream()
                .map(setting -> ConfigurationSetting.copyOf(setting)
                        .withImmutableAnnotations(List.of(jsonProperty(setting.name()))))
                .toList();
    }

    private static List<AnnotationSpec> extraTypeAnnotations() {
        final var serialize = AnnotationSpec.builder(JsonSerialize.class)
                .addMember("as", "$T.class", immutableType("ImmutableSqlConfiguration"))
                .build();
        final var deserialize = AnnotationSpec.builder(JsonDeserialize.class)
                .addMember("as", "$T.class", immutableType("ImmutableSqlConfiguration"))
                .build();
        return List.of(serialize, deserialize);
    }

    private static AnnotationSpec jsonProperty(final String name) {
        return AnnotationSpec.builder(JsonProperty.class).addMember("value", "$S", name).build();
    }

    private static ConfigurationSetting repository() {
        return ConfigurationSetting.builder()
                .setName("repository")
                .setDescription("The fully qualified name of the target repository.")
                .setType(TypicalTypes.STRING)
                .setValue("Repository")
                .setImmutableAnnotations(List.of(jsonProperty("repository")))
                .build();
    }

    private static ConfigurationSetting name() {
        return ConfigurationSetting.builder()
                .setName("name")
                .setDescription("The name of the SQL statement")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .setImmutableAnnotations(List.of(jsonProperty("name")))
                .build();
    }

    private static ConfigurationSetting description() {
        return ConfigurationSetting.builder()
                .setName("description")
                .setDescription("The description for the SQL statement")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .setImmutableAnnotations(List.of(jsonProperty("description")))
                .build();
    }

    private static ConfigurationSetting vendor() {
        return ConfigurationSetting.builder()
                .setName("vendor")
                .setDescription("The vendor name of the database the SQL statement is intended for")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .setImmutableAnnotations(List.of(jsonProperty("vendor")))
                .build();
    }

    private static ConfigurationSetting type() {
        return ConfigurationSetting.builder()
                .setName("type")
                .setDescription("The type of the SQL statement.")
                .setType(TypeName.get(SqlType.class))
                .setValue(SqlType.UNKNOWN)
                .setImmutableAnnotations(List.of(jsonProperty("type")))
                .build();
    }

    private static ConfigurationSetting returningMode() {
        return ConfigurationSetting.builder()
                .setName("returningMode")
                .setDescription("The returning mode of the SQL statement.")
                .setType(TypeName.get(ReturningMode.class))
                .setValue(ReturningMode.NONE)
                .setImmutableAnnotations(List.of(jsonProperty("returningMode")))
                .build();
    }

    private static ConfigurationSetting resultRowConverter() {
        return ConfigurationSetting.builder()
                .setName("resultRowConverter")
                .setDescription("The alias or fully-qualified name of the converter to use")
                .setType(TypeName.get(ResultRowConverter.class))
                .build();
    }

    private static ConfigurationSetting parameters() {
        return ConfigurationSetting.builder()
                .setName("parameters")
                .setDescription("The parameters of the SQL statement.")
                .setType(TypicalTypes.listOf(TypeName.get(SqlParameter.class)))
                .setValue(CodeBlock.builder().add("$T.of()", List.class).build())
                .build();
    }

    private static MethodSpec fromStatements() {
        final var statements = TypicalTypes.listOf(immutableType("SqlStatement"));
        final var configuration = immutableType("SqlConfiguration");
        return MethodSpec.methodBuilder("fromStatements")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(statements, "statements", Modifier.FINAL).build())
                .returns(configuration)
                .addStatement("$T latest = $T.usingDefaults().build()", configuration, configuration)
                .addCode(CodeBlock.builder()
                        .beginControlFlow("for (final var $L : $L)", "statement", "statements")
                        .addStatement("final var config = $L.getConfiguration()", "statement")
                        .addStatement("latest = merge(latest, config)")
                        .endControlFlow()
                        .build())
                .addStatement("return latest")
                .build();
    }

    private static MethodSpec merge() {
        final var configuration = immutableType("SqlConfiguration");
        return MethodSpec.methodBuilder("merge")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(configuration, "first", Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(configuration, "second", Modifier.FINAL).build())
                .returns(configuration)
                .addStatement("var merged = $T.copyOf($L)", configuration, "first")
                .addCode(withAllSettings())
                .addStatement("return merged")
                .build();
    }

    private static ClassName immutableType(final String type) {
        return ClassName.get("wtf.metio.yosql.models.immutables", type);
    }

    private static CodeBlock withAllSettings() {
        final var builder = CodeBlock.builder();
        settings().forEach(setting -> addSetting(builder, setting));
        Repositories.stringMethods().forEach(setting -> addSetting(builder, setting));
        booleanRepositorySettings().forEach(setting -> addSetting(builder, setting));
        return builder.build();
    }

    private static void addSetting(final CodeBlock.Builder builder, final ConfigurationSetting setting) {
        if (setting.value().isPresent() && SqlType.class.equals(setting.value().get().getClass())) {
            builder.addStatement("merged = merged.with$L($T.UNKNOWN == first.$L() ? second.$L() : first.$L())",
                    Strings.upperCase(setting.name()), SqlType.class, setting.name(), setting.name(), setting.name());
        } else if (setting.value().isPresent() && ReturningMode.class.equals(setting.value().get().getClass())) {
            builder.addStatement("merged = merged.with$L($T.NONE == first.$L() ? second.$L() : first.$L())",
                    Strings.upperCase(setting.name()), ReturningMode.class, setting.name(), setting.name(), setting.name());
        } else if (TypicalTypes.STRING.equals(setting.type())) {
            builder.addStatement("merged = merged.with$L($S.equals(first.$L()) ? second.$L() : first.$L())",
                    Strings.upperCase(setting.name()), setting.value().orElse(""), setting.name(), setting.name(), setting.name());
        } else if (TypeName.get(Boolean.class).equals(setting.type())) {
            // first.generateBlockingApi().or(() -> second.generateBlockingApi()).ifPresent(defaults::withGenerateBlockingApi)
            builder.beginControlFlow("if (first.$L().or(() -> second.$L()).isPresent())",
                    setting.name(), setting.name());
            builder.addStatement("merged = merged.with$L(first.$L().or(() -> second.$L()).get())",
                    Strings.upperCase(setting.name()), setting.name(), setting.name());
            builder.endControlFlow();
        } else if (TypeName.get(ResultRowConverter.class).equals(setting.type())) {
            builder.addStatement("merged = merged.with$L(first.$L().or(second::resultRowConverter))",
                    Strings.upperCase(setting.name()), setting.name());
        } else if (TypicalTypes.listOf(TypeName.get(SqlParameter.class)).equals(setting.type())) {
            builder.addStatement("merged = merged.with$L(mergeParameters(first.$L(), second.$L()))",
                    Strings.upperCase(setting.name()), setting.name(), setting.name());
        }
    }

    private static MethodSpec mergeParameters() {
        final var listOfParameters = TypicalTypes.listOf(TypeName.get(SqlParameter.class));
        return MethodSpec.methodBuilder("mergeParameters")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(listOfParameters, "first", Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(listOfParameters, "second", Modifier.FINAL).build())
                .returns(listOfParameters)
                .beginControlFlow("if (first == null || first.isEmpty())")
                .addStatement("return second")
                .endControlFlow()
                .addCode("return first.stream()")
                .addCode("$>\n.map(param -> second.stream()")
                .addCode("$>\n.filter(op -> param.name().equals(op.name()))$<")
                .addCode("\n.findFirst()")
                .addCode("\n.map(other -> $T.builder()", SqlParameter.class)
                .addCode("$>\n.setName(param.name())")
                .addCode("\n.setType($T.class.getName().equals(param.type()) ? other.type() : param.type())", Object.class)
                .addCode("\n.setIndices(param.indices() == null ? other.indices() : param.indices())")
                .addCode("\n.setConverter(param.converter().or(other::converter))")
                .addCode("\n.build())$<")
                .addCode("\n.orElse($T.copy(param)))$<", SqlParameter.class)
                .addStatement("\n.collect($T.toList())", Collectors.class)
                .build();
    }

    private static MethodSpec joinMethodNameParts() {
        return MethodSpec.methodBuilder("joinMethodNameParts")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String.class)
                .varargs(true)
                .addParameter(ParameterSpec.builder(ArrayTypeName.of(String.class), "strings", Modifier.FINAL).build())
                .addStatement("final var part = new $T()", AtomicInteger.class)
                .addStatement(CodeBlock.builder()
                        .add("return $T.stream($L)", Arrays.class, "strings")
                        .add("$>\n.filter($T::nonNull)", Objects.class)
                        .add("\n.map($T::strip)", String.class)
                        .add("\n.filter($T.not($T::isBlank))", Predicate.class, String.class)
                        .add("\n.map(string -> part.getAndIncrement() == 0")
                        .add("$>\n? string")
                        .add("\n: string.substring(0,1).toUpperCase() + string.substring(1))$<")
                        .add("\n.collect($T.joining())$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec batchName() {
        return MethodSpec.methodBuilder("batchName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L(), $L(), $L())", "batchPrefix", "name", "batchSuffix")
                .build();
    }

    private static MethodSpec blockingName() {
        return MethodSpec.methodBuilder("blockingName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L(), $L(), $L())", "blockingPrefix", "name", "blockingSuffix")
                .build();
    }

    private static MethodSpec rxJavaName() {
        return MethodSpec.methodBuilder("rxJavaName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L(), $L(), $L())", "rxJavaPrefix", "name", "rxJavaSuffix")
                .build();
    }

    private static MethodSpec reactorName() {
        return MethodSpec.methodBuilder("reactorName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L(), $L(), $L())", "reactorPrefix", "name", "reactorSuffix")
                .build();
    }

    private static MethodSpec mutinyName() {
        return MethodSpec.methodBuilder("mutinyName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L(), $L(), $L())", "mutinyPrefix", "name", "mutinySuffix")
                .build();
    }

    private static MethodSpec streamName() {
        return MethodSpec.methodBuilder("streamName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L(), $L(), $L())", "streamPrefix", "name", "streamSuffix")
                .build();
    }

    private Sql() {
        // data class
    }

}
