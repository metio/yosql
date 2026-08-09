/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import com.palantir.javapoet.*;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static wtf.metio.yosql.internals.javapoet.TypicalTypes.*;
import static wtf.metio.yosql.internals.jdk.Strings.upperCase;

public final class Converter extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Converter.class.getSimpleName();
    private static final String CREATE_ROW_CONVERTER = "createRowConverter";
    private static final String MAP_CONVERTER_ALIAS = "mapConverterAlias";
    private static final String MAP_CONVERTER_METHOD = "mapConverterMethod";
    private static final String MAP_CONVERTER_CLASS = "mapConverterClass";
    private static final String BASE_PACKAGE_NAME = "basePackageName";
    private static final String RESOLVE_CONVERTER_CLASS = "resolvedMapConverterClass";
    private static final String RECORD_CONVERTER_METHOD = "recordConverterMethod";
    private static final String RECORD_CONVERTER_PREFIX = "recordConverterPrefix";
    private static final String RECORD_CONVERTER_SUFFIX = "recordConverterSuffix";
    private static final String DEFAULT_CONVERTER = "defaultConverter";
    private static final String MAP_RESULT_TYPE = "java.util.Map<String, Object>";

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures converter related settings.")
                .addSettings(defaultConverter())
                .addSettings(generateMapConverter())
                .addSettings(mapConverterClass())
                .addSettings(mapConverterMethod())
                .addSettings(mapConverterAlias())
                .addSettings(recordConverterMethod())
                .addSettings(recordConverterPrefix())
                .addSettings(recordConverterSuffix())
                .addAntMethods(createRowConverter(Modifier.FINAL, Converter::field))
                .addAntMethods(resolveConverterClass(Modifier.FINAL, Converter::field))
                .addCliMethods(createRowConverter(Modifier.FINAL, Converter::field))
                .addCliMethods(resolveConverterClass(Modifier.FINAL, Converter::field))
                .addGradleMethods(createRowConverter(Modifier.PRIVATE, Converter::gradleAccessor))
                .addGradleMethods(resolveConverterClass(Modifier.PRIVATE, Converter::gradleAccessor))
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .addMavenMethods(createRowConverter(Modifier.FINAL, Converter::field))
                .addMavenMethods(resolveConverterClass(Modifier.FINAL, Converter::field))
                // Where the repositories go decides where the converters go, and the two settings
                // live in different groups — so the one that depends on the other is handed it.
                .addAntParameters(basePackageNameParameter())
                .addCliParameters(basePackageNameParameter())
                .addGradleParameters(basePackageNameParameter())
                .addMavenParameters(basePackageNameParameter())
                .build();
    }

    private static ConfigurationSetting defaultConverter() {
        final var name = DEFAULT_CONVERTER;
        final var description = "The fully-qualified name of the converter to use for statements that name none.";
        final var initializer = CodeBlock.of(".set$L($L($L, $L))\n",
                upperCase(name), CREATE_ROW_CONVERTER, name, BASE_PACKAGE_NAME);
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setExplanation("""
                        A converter is named by its class and nothing else: `YoSQL` reads the class to find its
                        single public method taking a `ResultSet`, and that method's name and return type become
                        the method the repository calls and the type each row becomes. The field the repository
                        injects it into is named after the class, with a lower-case first letter.

                        Leave this empty and statements that name neither a converter nor a
                        [resultRowType](../../sql/resultrowtype/) fall back to the generated
                        [ToMap converter](../mapconverterclass/).""")
                .addExamples(ConfigurationExample.builder()
                        .setValue("")
                        .setDescription("The default value of the `defaultConverter` configuration option is empty, which leaves the generated ToMap converter in place.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("com.example.persistence.converter.ToUserConverter")
                        .setDescription("Setting the `defaultConverter` configuration option to `com.example.persistence.converter.ToUserConverter` makes every statement that names no converter of its own return whatever that converter's `ResultSet` method returns.")
                        .build())
                .setAntInitializer(initializer)
                .setCliInitializer(initializer)
                .setGradleInitializer(CodeBlock.of(".set$L($L($L().get(), $L))\n",
                        upperCase(name), CREATE_ROW_CONVERTER, gradlePropertyName(name), BASE_PACKAGE_NAME))
                .setMavenInitializer(initializer)
                .setGradleConvention(CodeBlock.of("$L().convention($S)", gradlePropertyName(name), ""))
                .addAntFields(antField(name, description, ""))
                .addAntMethods(antSetter(ClassName.get(String.class), name, description))
                .addCliFields(picocliOption(GROUP_NAME, name, description, ""))
                .addGradleMethods(gradleProperty(gradlePropertyOf(ClassName.get(String.class)), name, description))
                .addImmutableMethods(immutableMethod(ClassName.get(ResultRowConverter.class), name, description))
                .addMavenFields(mavenParameter(name, description, ""))
                .build();
    }

    private static ConfigurationSetting generateMapConverter() {
        final var name = "generateMapConverter";
        final var description = "Whether the ToMap converter should be generated.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("""
                        A statement naming neither a [resultRowType](../../sql/resultrowtype/) nor a
                        [resultRowConverter](../../sql/resultrowconverter/) answers with a
                        `Map<String, Object>` per row, read by a converter generated for the purpose. This decides
                        whether that converter is written.

                        Turn it off once every statement names a type of its own, and a statement that goes back
                        to answering with maps then fails the build instead of quietly doing so — which is the
                        point of turning it off.""")
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default. Statements naming no type answer with maps:")
                        .setResult("""
                                package com.example.persistence.converter;

                                public final class ToMapConverter {

                                    public Map<String, Object> apply(final ResultSet resultSet) throws SQLException {
                                        // ... reads every column of the row by its label
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("No converter is written, so a statement that would need one fails the build.")
                        .build())
                .build();
    }

    private static ConfigurationSetting mapConverterClass() {
        final var description = "The class name of the ToMap converter.";
        final var value = "ToMapConverter";
        final var configured = setting(GROUP_NAME, MAP_CONVERTER_CLASS, description, value)
                .setExplanation("""
                        Where the generated ToMap converter is written and what it is called — and, since every
                        generated converter is written beside it, where the rest of them go too.

                        A bare class name, which is the default, puts them in a `converter` package under the
                        repositories' [basePackageName](../../repositories/basepackagename/): a project that has
                        said where its repositories go has thereby said where its converters go. Write a
                        fully-qualified name to put them somewhere else — or, together with
                        [generateMapConverter](../generatemapconverter/) set to `false`, to point repositories at a
                        ToMap converter you wrote yourself.""")
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default. Converters are generated into a `converter` package beneath the base package the repositories use.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("com.example.mapping.ToMapConverter")
                        .setDescription("A name with a package is followed exactly, wherever the repositories are.")
                        .build())
                .build();
        // Resolved as the configuration is assembled rather than where it is read: the default
        // converter copies this value, and a copy taken before the resolution names a class nobody
        // writes.
        final var resolve = CodeBlock.of(".set$L($L($L))\n",
                upperCase(MAP_CONVERTER_CLASS), RESOLVE_CONVERTER_CLASS, BASE_PACKAGE_NAME);
        return ConfigurationSetting.copyOf(configured)
                .withAntInitializer(resolve)
                .withCliInitializer(resolve)
                .withGradleInitializer(resolve)
                .withMavenInitializer(resolve);
    }

    private static ConfigurationSetting recordConverterMethod() {
        final var description = "The name of the method to generate in converters built from a record.";
        final var value = "asUserType";
        return setting(GROUP_NAME, RECORD_CONVERTER_METHOD, description, value)
                .setExplanation("""
                        Every converter generated from a [resultRowType](../../sql/resultrowtype/) declares one method,
                        and this is its name. Change it to match the convention the hand-written converters in your
                        project already follow, so a repository reads the same whichever kind of converter it calls.""")
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value of the `recordConverterMethod` configuration option is `asUserType`, which matches the name used by the hand-written converters in the examples.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("mapRow")
                        .setDescription("Changing the `recordConverterMethod` configuration option to `mapRow` generates `public User mapRow(ResultSet resultSet)` instead.")
                        .build())
                .build();
    }

    private static ConfigurationSetting recordConverterPrefix() {
        final var description = "The prefix of the class name of converters built from a record.";
        final var value = "To";
        return setting(GROUP_NAME, RECORD_CONVERTER_PREFIX, description, value)
                .setExplanation("""
                        A converter generated for a record is named `<prefix><record><suffix>` and lives in the package
                        of the [mapConverterClass](../mapconverterclass/). The field a repository injects it into takes
                        the same name with a lower-case first letter, so changing either affix changes both.""")
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value of the `recordConverterPrefix` configuration option is `To`, which names the converter for a `User` record `ToUserConverter`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("")
                        .setDescription("Clearing the `recordConverterPrefix` configuration option and setting `recordConverterSuffix` to `RowMapper` names it `UserRowMapper`.")
                        .build())
                .build();
    }

    private static ConfigurationSetting recordConverterSuffix() {
        final var description = "The suffix of the class name of converters built from a record.";
        final var value = "Converter";
        return setting(GROUP_NAME, RECORD_CONVERTER_SUFFIX, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value of the `recordConverterSuffix` configuration option is `Converter`, which names the converter for a `User` record `ToUserConverter`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("RowMapper")
                        .setDescription("Changing the `recordConverterSuffix` configuration option to `RowMapper` names it `ToUserRowMapper`.")
                        .build())
                .build();
    }

    private static ConfigurationSetting mapConverterMethod() {
        final var description = "The name of the method to generate/call in the ToMap converter.";
        final var value = "apply";
        return setting(GROUP_NAME, MAP_CONVERTER_METHOD, description, value)
                .setExplanation("""
                        The method repositories call on the ToMap converter. It defaults to `apply`, which is what
                        a `Function<ResultSet, Map<String, Object>>` declares — so the generated converter can be
                        used as one.

                        Where you supply the converter yourself, this is how you name the method it actually
                        has.""")
                .build();
    }

    private static ConfigurationSetting mapConverterAlias() {
        final var description = "The name of the alias referencing the ToMap converter.";
        final var value = "toMap";
        return setting(GROUP_NAME, MAP_CONVERTER_ALIAS, description, value)
                .setExplanation("""
                        The field a repository holds the ToMap converter in. Converters generated from a record are
                        named after their own class; this one is named here, because there is one of it and
                        `toMapConverter` reads worse than `toMap` in the body of every method that uses it.""")
                .build();
    }

    /**
     * Turns the class name a user configured into the converter the generator consumes.
     *
     * <p>Every frontend hands over a plain string, so they all share this method; only how a
     * frontend reads its own fields differs, which is what {@code accessor} supplies. An unset
     * value leaves the generated ToMap converter in place, so a build that configures nothing
     * still has a converter for every statement.</p>
     */
    private static MethodSpec createRowConverter(final Modifier modifier, final Function<String, CodeBlock> accessor) {
        return MethodSpec.methodBuilder(CREATE_ROW_CONVERTER)
                .addModifiers(modifier)
                .returns(ResultRowConverter.class)
                .addParameter(ParameterSpec.builder(String.class, "converterClass", Modifier.FINAL).build())
                .addParameter(basePackageNameParameter())
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable($L)", Optional.class, "converterClass")
                        .add("$>$>\n.map($T::strip)", String.class)
                        .add("\n.filter($T.not($T::isBlank))", Predicate.class, Strings.class)
                        .add("\n.map($T::fromClassName)", ResultRowConverter.class)
                        .add("\n.orElseGet(() -> $T.builder()", ResultRowConverter.class)
                        .add("$>\n.setAlias($L)", accessor.apply(MAP_CONVERTER_ALIAS))
                        .add("\n.setConverterType($L($L))", RESOLVE_CONVERTER_CLASS, BASE_PACKAGE_NAME)
                        .add("\n.setMethodName($L)", accessor.apply(MAP_CONVERTER_METHOD))
                        .add("\n.setResultType($S)", MAP_RESULT_TYPE)
                        .add("\n.build())$<$<$<")
                        .build())
                .build();
    }

    private static ParameterSpec basePackageNameParameter() {
        return ParameterSpec.builder(String.class, BASE_PACKAGE_NAME, Modifier.FINAL).build();
    }

    /**
     * Turns whatever {@code mapConverterClass} says into the class name the rest of the build uses.
     *
     * <p>Generated per frontend rather than resolved further down, because the value is copied while
     * the configuration is assembled — the default converter takes its type from it — and a copy made
     * before the resolution is a reference to a class that is never written.</p>
     */
    private static MethodSpec resolveConverterClass(
            final Modifier modifier,
            final Function<String, CodeBlock> accessor) {
        return MethodSpec.methodBuilder(RESOLVE_CONVERTER_CLASS)
                .addModifiers(modifier)
                .returns(String.class)
                .addParameter(basePackageNameParameter())
                .addStatement("final var declared = $L.strip()", accessor.apply(MAP_CONVERTER_CLASS))
                .addStatement("return declared.contains($S) ? declared : $L + $S + declared",
                        ".", BASE_PACKAGE_NAME, ".converter.")
                .build();
    }

    private static CodeBlock field(final String name) {
        return CodeBlock.of("$L", name);
    }

    private static CodeBlock gradleAccessor(final String name) {
        return CodeBlock.of("get$L().get()", upperCase(name));
    }

    private Converter() {
        // data class
    }

}
