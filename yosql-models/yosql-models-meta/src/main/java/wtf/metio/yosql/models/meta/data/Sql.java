/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.javapoet.*;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.*;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static wtf.metio.yosql.models.meta.data.Repositories.INJECT_CONVERTERS;

public final class Sql extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Sql.class.getSimpleName();
    private static final String MERGE = "merge";
    private static final String RESULT_ROW_CONVERTER = "resultRowConverter";

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("The configuration for a single SQL statement.")
                .setExplanation("""
                        All of these options are to be placed in the front matter of SQL statements and their overwrite
                        their respective counterparts in the global configuration, e.g. in [repositories](../repositories/).""")
                .addAllSettings(settings())
                .addAllSettings(withExtraAnnotations(stringRepositorySettings()))
                .addAllSettings(withExtraAnnotations(booleanRepositorySettings()))
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableMethods(
                        fromStatements(),
                        merge(),
                        converter(),
                        executeOnceName(),
                        executeBatchName(),
                        joinMethodNameParts())
                .addImmutableAnnotations(immutableAnnotation())
                .addImmutableAnnotations(AnnotationSpec.builder(JsonSerialize.class)
                        .addMember("as", "$T.class", immutableType(immutableConfigurationName(GROUP_NAME)))
                        .build())
                .addImmutableAnnotations(AnnotationSpec.builder(JsonDeserialize.class)
                        .addMember("as", "$T.class", immutableType(immutableConfigurationName(GROUP_NAME)))
                        .build())
                .build();
    }

    private static List<ConfigurationSetting> settings() {
        return List.of(
                repository(),
                repositoryInterface(),
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
                        .withImmutableMethods(immutableMethod(ClassName.get(String.class),
                                setting.name(), setting.description())))
                .collect(Collectors.toList());
    }

    private static List<ConfigurationSetting> booleanRepositorySettings() {
        return Repositories.booleanMethods().stream()
                .filter(setting -> !INJECT_CONVERTERS.equals(setting.name()))
                .map(setting -> ConfigurationSetting.copyOf(setting)
                        .withImmutableMethods(immutableMethod(ClassName.get(Boolean.class),
                                setting.name(), setting.description())))
                .collect(Collectors.toList());
    }

    private static List<? extends ConfigurationSetting> withExtraAnnotations(final List<ConfigurationSetting> settings) {
        return settings.stream()
                .map(setting -> ConfigurationSetting.copyOf(setting)
                        .withImmutableAnnotations(jsonProperty(setting.name())))
                .toList();
    }

    private static AnnotationSpec jsonProperty(final String name) {
        return AnnotationSpec.builder(JsonProperty.class).addMember("value", "$S", name).build();
    }

    private static ConfigurationSetting repository() {
        final var name = "repository";
        final var description = "The fully qualified name of the target repository class.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setExplanation("""
                        In order to overwrite the target repository of a single SQL statement, use the `repository` option.
                        You can specify the fully qualified name of the repository that should contain your statement, or
                        you can just specify the name of the class and `YoSQL` will automatically add the
                        [base package name](../../repositories/basepackagename/) as well as the
                        [repositoryNamePrefix](../../repositories/repositorynameprefix/) and
                        [repositoryNameSuffix](../../repositories/repositorynamesuffix/) for you.""")
                .addTags(Tags.FRONT_MATTER)
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description))
                .build();
    }

    private static ConfigurationSetting repositoryInterface() {
        final var name = "repositoryInterface";
        final var description = "The fully qualified name of the target repository interface.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description, AnnotationSpec.builder(JsonIgnore.class).build()))
                .setGenerateDocs(false)
                .build();
    }

    private static ConfigurationSetting name() {
        final var name = "name";
        final var description = "The name of the SQL statement";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("yourSpecialName")
                        .setDescription("In case you use the `name` option, `YoSQL` will use the supplied value as a method name")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void yourSpecialName() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting description() {
        final var name = "description";
        final var description = "The description for the SQL statement";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("Some random description")
                        .setDescription("In case you use the `description` option, `YoSQL` will use the supplied value as a JavaDoc comment")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    /**
                                     * Some random description
                                     */
                                    public void someMethod() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting vendor() {
        final var name = "vendor";
        final var description = "The vendor name of the database the SQL statement is intended for";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting type() {
        final var name = "type";
        final var description = "The type of the SQL statement.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(SqlStatementType.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting returningMode() {
        final var name = "returningMode";
        final var description = "The returning mode of the SQL statement.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(ReturningMode.class), name, description, jsonProperty("returning")))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting resultRowConverter() {
        final var name = RESULT_ROW_CONVERTER;
        final var description = "The alias or fully-qualified name of the converter to use";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(ResultRowConverter.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting parameters() {
        final var name = "parameters";
        final var description = "The parameters of the SQL statement.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(TypicalTypes.listOf(SqlParameter.class), name, description))
                .setMergeCode(CodeBlock.of("$T.mergeParameters(first.$L(), second.$L())", SqlParameter.class, name, name))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting annotations() {
        final var name = "annotations";
        final var description = "The additional annotations to be placed on generated methods.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setExplanation("This list is empty by default and thus no annotations are added to generated methods.")
                .addImmutableMethods(immutableMethod(TypicalTypes.listOf(Annotation.class), name, description))
                .setMergeCode(CodeBlock.of("$T.mergeAnnotations(first.$L(), second.$L())", Annotation.class, name, name))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("your.own.Annotation")
                        .setDescription("In order to add an annotation to the generated methods, specify its fully-qualified name.")
                        .setResult("""
                                package com.example.persistence;
                                                                
                                import your.own.Annotation;

                                public class SomeRepository {

                                    @Annotation
                                    public void someMethod() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("your.other.Annotation")
                        .setDescription("In order to add an annotation with some member, specify name of the annotation member, its value, and its type. The type defaults to `java.lang.String`.")
                        .setResult("""
                                package com.example.persistence;
                                                                
                                import your.other.Annotation;

                                public class SomeRepository {

                                    @Annotation(member = "value")
                                    public void someMethod() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .setFrontMatterExampleCode("""
                        
                        --  - type: your.own.Annotation
                        --    members:
                        --      - key: someMember
                        --        value: this is your value
                        --        type: java.lang.String
                        --      - key: another
                        --        value: 5
                        --        type: int
                        --  - type: your.other.Annotation
                        --    members:
                        --      - key: value
                        --        value: yep
                        --  - type: some.annotation.WithoutMembers""")
                .build();
    }

    private static MethodSpec fromStatements() {
        final var statements = TypicalTypes.listOf(immutableType("SqlStatement"));
        final var configuration = immutableType("SqlConfiguration");
        return MethodSpec.methodBuilder("fromStatements")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(statements, "statements", Modifier.FINAL).build())
                .returns(configuration)
                .addStatement("$T latest = $T.$L().build()", configuration, configuration, BUILDER_METHOD_NAME)
                .addCode(CodeBlock.builder()
                        .beginControlFlow("for (final var $L : $L)", "statement", "statements")
                        .addStatement("final var config = $L.getConfiguration()", "statement")
                        .addStatement("latest = $L(latest, config)", MERGE)
                        .endControlFlow()
                        .build())
                .addStatement("return latest")
                .build();
    }

    private static MethodSpec merge() {
        final var configuration = immutableType("SqlConfiguration");
        return MethodSpec.methodBuilder(MERGE)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(configuration, "first", Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(configuration, "second", Modifier.FINAL).build())
                .returns(configuration)
                .addStatement(CodeBlock.builder()
                        .add("return $T.$L($L)", configuration, COPY_OF_METHOD_NAME, "first")
                        .add(withAllSettings())
                        .build())
                .build();
    }

    private static CodeBlock withAllSettings() {
        final var builder = CodeBlock.builder();
        settings().forEach(setting -> addSetting(builder, setting));
        stringRepositorySettings().forEach(setting -> addSetting(builder, setting));
        booleanRepositorySettings().forEach(setting -> addSetting(builder, setting));
        return builder.build();
    }

    private static void addSetting(final CodeBlock.Builder builder, final ConfigurationSetting setting) {
        setting.mergeCode().ifPresentOrElse(
                code -> builder.add("$>$>\n.with$L($L)$<$<", Strings.upperCase(setting.name()), code),
                () -> builder.add("$>$>\n.with$L(first.$L().or(second::$L))$<$<",
                        Strings.upperCase(setting.name()), setting.name(), setting.name()));
    }

    private static MethodSpec converter() {
        final var parameterType = TypicalTypes.supplierOf(TypicalTypes.optionalOf(ResultRowConverter.class));
        final var defaultConverter = "defaultConverter";
        return MethodSpec.methodBuilder("converter")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(parameterType, defaultConverter).build())
                .returns(ResultRowConverter.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement(CodeBlock.builder()
                        .add("return $L()", RESULT_ROW_CONVERTER)
                        .add("$>\n.or($L)", defaultConverter)
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
                        .add("\n: string.substring(0, 1).toUpperCase($T.ROOT) + string.substring(1))$<", Locale.class)
                        .add("\n.collect($T.joining())$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec executeOnceName() {
        return MethodSpec.methodBuilder("executeOnceName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L().orElse($S), $L().orElse($S), $L().orElse($S))",
                        "executeOncePrefix", "", "name", "", "executeOnceSuffix", "")
                .build();
    }

    private static MethodSpec executeBatchName() {
        return MethodSpec.methodBuilder("executeBatchName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return joinMethodNameParts($L().orElse($S), $L().orElse($S), $L().orElse($S))",
                        "executeBatchPrefix", "", "name", "", "executeBatchSuffix", "")
                .build();
    }

    private Sql() {
        // data class
    }

}
