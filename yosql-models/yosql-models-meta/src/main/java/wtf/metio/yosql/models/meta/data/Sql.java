/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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
                .addAllSettings(withExtraAnnotations(Repositories.methods()))
                .setImmutableMethods(derivedMethods())
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

    private static List<ConfigurationSetting> withExtraAnnotations(final List<ConfigurationSetting> settings) {
        return settings.stream()
                .map(s -> ConfigurationSetting.copyOf(s).withImmutableAnnotations(List.of(jsonProperty(s.name()))))
                .collect(Collectors.toList());
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

    private static ConfigurationSetting repository() {
        return ConfigurationSetting.builder()
                .setName("repository")
                .setDescription("The fully qualified name of the target repository.")
                .setType(TypicalTypes.STRING)
                .setValue("Repository")
                .setImmutableAnnotations(List.of(jsonProperty("repository")))
                .build();
    }

    private static AnnotationSpec jsonProperty(final String name) {
        return AnnotationSpec.builder(JsonProperty.class).addMember("value", "$S", name).build();
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
                .setValue(ReturningMode.LIST)
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

    private static List<MethodSpec> derivedMethods() {
        return List.of(
                fromStatements(),
                merge(),
                mergeParameters(),
                flowableName(),
                batchName(),
                streamLazyName(),
                streamEagerName(),
                joinMethodNameParts());
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
                .addStatement("final var defaults = $T.copyOf($L)\n$L", configuration, "first", withAllSettings())
                .addStatement("return defaults")
                .build();
    }

    private static ClassName immutableType(final String type) {
        return ClassName.get("wtf.metio.yosql.models.immutables", type);
    }

    private static CodeBlock withAllSettings() {
        final var builder = CodeBlock.builder();
        settings().forEach(setting -> addSetting(builder, setting));
        Repositories.methods().forEach(setting -> addSetting(builder, setting));
        return builder.build();
    }

    private static void addSetting(final CodeBlock.Builder builder, final ConfigurationSetting setting) {
        if (setting.value().isPresent() && SqlType.class.equals(setting.value().get().getClass())) {
            builder.add("\t.with$L($T.UNKNOWN == first.$L() ? second.$L() : first.$L())\n",
                    Strings.upCase(setting.name()), SqlType.class, setting.name(), setting.name(), setting.name());
        } else if (setting.value().isPresent() && ReturningMode.class.equals(setting.value().get().getClass())) {
            builder.add("\t.with$L($T.NONE == first.$L() ? second.$L() : first.$L())\n",
                    Strings.upCase(setting.name()), ReturningMode.class, setting.name(), setting.name(), setting.name());
        } else if (TypicalTypes.STRING.equals(setting.type())) {
            builder.add("\t.with$L($S.equals(first.$L()) ? second.$L() : first.$L())\n",
                    Strings.upCase(setting.name()), setting.value().orElse(""), setting.name(), setting.name(), setting.name());
        } else if (TypeName.get(boolean.class).equals(setting.type())) {
            builder.add("\t.with$L(first.$L() || second.$L())\n",
                    Strings.upCase(setting.name()), setting.name(), setting.name());
        } else if (TypeName.get(ResultRowConverter.class).equals(setting.type())) {
            builder.add("\t.with$L(first.$L().or(second::resultRowConverter))\n",
                    Strings.upCase(setting.name()), setting.name());
        } else if (TypicalTypes.listOf(TypeName.get(SqlParameter.class)).equals(setting.type())) {
            builder.add("\t.with$L(mergeParameters(first.$L(), second.$L()))\n",
                    Strings.upCase(setting.name()), setting.name(), setting.name());
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
                .addCode("return first.stream()\n")
                .addCode("\t.map(param -> second.stream()\n")
                .addCode("\t\t.filter(op -> param.name().equals(op.name()))\n")
                .addCode("\t\t.findFirst()\n")
                .addCode("\t\t.map(other -> $T.builder()\n", SqlParameter.class)
                .addCode("\t\t\t.setName(param.name())\n")
                .addCode("\t\t\t.setType($T.class.getName().equals(param.type()) ? other.type() : param.type())\n", Object.class)
                .addCode("\t\t\t.setConverter(param.converter().or(other::converter))\n")
                .addCode("\t\t\t.setIndices(param.indices() == null ? other.indices() : param.indices())\n")
                .addCode("\t\t\t.build())\n")
                .addCode("\t\t.orElse($T.copy(param)))\n", SqlParameter.class)
                .addStatement("\t.collect($T.toList())", Collectors.class)
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
                        .add("return $T.stream($L)\n", Arrays.class, "strings")
                        .add("\t.filter($T::nonNull)\n", Objects.class)
                        .add("\t.map($T::strip)\n", String.class)
                        .add("\t.filter($T.not($T::isBlank))\n", Predicate.class, String.class)
                        .add("\t.map(string -> part.getAndIncrement() == 0\n")
                        .add("\t\t? string\n")
                        .add("\t\t: string.substring(0,1).toUpperCase() + string.substring(1))\n")
                        .add("\t.collect($T.joining())", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec flowableName() {
        return MethodSpec.methodBuilder("flowableName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L(), $L(), $L())", "rxjava2Prefix", "name", "rxjava2Suffix")
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

    private static MethodSpec streamLazyName() {
        return MethodSpec.methodBuilder("streamLazyName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L(), $L(), $L(), $L())", "streamPrefix", "name", "streamSuffix", "lazyName")
                .build();
    }

    private static MethodSpec streamEagerName() {
        return MethodSpec.methodBuilder("streamEagerName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L(), $L(), $L(), $L())", "streamPrefix", "name", "streamSuffix", "eagerName")
                .build();
    }

    private Sql() {
        // data class
    }

}
