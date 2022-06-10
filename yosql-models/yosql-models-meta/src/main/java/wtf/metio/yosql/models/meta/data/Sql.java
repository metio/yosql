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
import wtf.metio.yosql.models.configuration.*;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

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
                .addAllSettings(withExtraAnnotations(stringRepositorySettings()))
                .addAllSettings(withExtraAnnotations(booleanRepositorySettings())) // TODO: remove injectConverters from this list
                .setImmutableMethods(List.of(
                        fromStatements(),
                        merge(),
                        converter(),
                        batchName(),
                        standardName(),
                        joinMethodNameParts()))
                .build();
    }

    private static List<ConfigurationSetting> settings() {
        return List.of(
                repository(),
                repositoryInterface(), // TODO: add JsonIgnore on this one?
                name(),
                description(),
                vendor(),
                type(),
                returningMode(),
                resultRowConverter(),
                parameters(),
                annotations());
    }

    private static List<ConfigurationSetting> stringRepositorySettings() {
        return Repositories.stringMethods().stream()
                .map(setting -> ConfigurationSetting.copyOf(setting)
                        .withType(TypeName.get(String.class))
                        .withValue(Optional.empty()))
                .collect(Collectors.toList());
    }

    private static List<ConfigurationSetting> booleanRepositorySettings() {
        return Repositories.booleanMethods().stream()
                .map(setting -> ConfigurationSetting.copyOf(setting)
                        .withType(TypeName.get(Boolean.class))
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
                .setDescription("The fully qualified name of the target repository class.")
                .setType(TypeName.get(String.class))
                .setImmutableAnnotations(List.of(jsonProperty("repository")))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting repositoryInterface() {
        return ConfigurationSetting.builder()
                .setName("repositoryInterface")
                .setDescription("The fully qualified name of the target repository interface.")
                .setType(TypeName.get(String.class))
                .build();
    }

    private static ConfigurationSetting name() {
        return ConfigurationSetting.builder()
                .setName("name")
                .setDescription("The name of the SQL statement")
                .setType(TypeName.get(String.class))
                .setImmutableAnnotations(List.of(jsonProperty("name")))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting description() {
        return ConfigurationSetting.builder()
                .setName("description")
                .setDescription("The description for the SQL statement")
                .setType(TypeName.get(String.class))
                .setImmutableAnnotations(List.of(jsonProperty("description")))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting vendor() {
        return ConfigurationSetting.builder()
                .setName("vendor")
                .setDescription("The vendor name of the database the SQL statement is intended for")
                .setType(TypeName.get(String.class))
                .setImmutableAnnotations(List.of(jsonProperty("vendor")))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting type() {
        return ConfigurationSetting.builder()
                .setName("type")
                .setDescription("The type of the SQL statement.")
                .setType(TypeName.get(SqlStatementType.class))
                .setImmutableAnnotations(List.of(jsonProperty("type")))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting returningMode() {
        return ConfigurationSetting.builder()
                .setName("returningMode")
                .setDescription("The returning mode of the SQL statement.")
                .setType(TypeName.get(ReturningMode.class))
                .setImmutableAnnotations(List.of(jsonProperty("returning")))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting resultRowConverter() {
        return ConfigurationSetting.builder()
                .setName("resultRowConverter")
                .setDescription("The alias or fully-qualified name of the converter to use")
                .setType(TypeName.get(ResultRowConverter.class))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting parameters() {
        return ConfigurationSetting.builder()
                .setName("parameters")
                .setDescription("The parameters of the SQL statement.")
                .setType(TypicalTypes.listOf(TypeName.get(SqlParameter.class)))
                .setValue(CodeBlock.builder().add("$T.of()", List.class).build())
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting annotations() {
        return ConfigurationSetting.builder()
                .setName("annotations")
                .setDescription("The additional annotations to be placed on generated methods.")
                .setType(TypicalTypes.listOf(Annotation.class))
                .setValue(CodeBlock.builder().add("$T.of()", List.class).build())
                .addTags(Tags.FRONT_MATTER)
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
                .addStatement(CodeBlock.builder()
                        .add("return $T.copyOf($L)", configuration, "first")
                        .add(withAllSettings())
                        .build())
                .build();
    }

    private static ClassName immutableType(final String type) {
        return ClassName.get("wtf.metio.yosql.models.immutables", type);
    }

    private static CodeBlock withAllSettings() {
        final var builder = CodeBlock.builder();
        settings().forEach(setting -> addSetting(builder, setting));
        stringRepositorySettings().forEach(setting -> addSetting(builder, setting));
        booleanRepositorySettings().forEach(setting -> addSetting(builder, setting));
        return builder.build();
    }

    private static void addSetting(final CodeBlock.Builder builder, final ConfigurationSetting setting) {
        if (TypicalTypes.listOf(TypeName.get(SqlParameter.class)).equals(setting.type())) {
            builder.add("$>$>\n.with$L($T.mergeParameters(first.$L(), second.$L()))$<$<",
                    Strings.upperCase(setting.name()), SqlParameter.class, setting.name(), setting.name());
        } else if (TypicalTypes.listOf(TypeName.get(Annotation.class)).equals(setting.type())) {
            builder.add("$>$>\n.with$L($T.mergeAnnotations(first.$L(), second.$L()))$<$<",
                    Strings.upperCase(setting.name()), Annotation.class, setting.name(), setting.name());
        } else {
            builder.add("$>$>\n.with$L(first.$L().or(second::$L))$<$<",
                    Strings.upperCase(setting.name()), setting.name(), setting.name());
        }
    }

    private static MethodSpec converter() {
        final var parameterType = TypicalTypes.supplierOf(TypicalTypes.optionalOf(ResultRowConverter.class));
        return MethodSpec.methodBuilder("converter")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(parameterType, "defaultConverter").build())
                .returns(ResultRowConverter.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement(CodeBlock.builder()
                        .add("return resultRowConverter()")
                        .add("$>\n.or(defaultConverter)")
                        .add("\n.orElseThrow()$<")
                        .build())
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
                .addStatement("return joinMethodNameParts($L().orElse($S), $L().orElse($S), $L().orElse($S))",
                        "batchPrefix", "", "name", "", "batchSuffix", "")
                .build();
    }

    private static MethodSpec standardName() {
        return MethodSpec.methodBuilder("standardName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L().orElse($S), $L().orElse($S), $L().orElse($S))",
                        "standardPrefix", "", "name", "", "standardSuffix", "")
                .build();
    }

    private Sql() {
        // data class
    }

}
