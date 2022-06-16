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
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.meta.ImmutableConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.Arrays;

import static wtf.metio.yosql.internals.javapoet.TypicalTypes.gradlePropertyOf;
import static wtf.metio.yosql.internals.jdk.Strings.upperCase;

abstract class AbstractConfigurationGroup implements ToolingPackages {

    protected static final String BUILDER_METHOD_NAME = "builder";
    protected static final String COPY_OF_METHOD_NAME = "copyOf";

    protected static ClassName antType(final String className) {
        return ClassName.get(ANT_PACKAGE, className);
    }

    protected static ClassName gradleType(final String className) {
        return ClassName.get(GRADLE_PACKAGE, className);
    }

    protected static ClassName immutableType(final String className) {
        return ClassName.get(IMMUTABLES_PACKAGE, className);
    }

    protected static ClassName mavenType(final String className) {
        return ClassName.get(MAVEN_PACKAGE, className);
    }

    protected static FieldSpec mavenStringParameter(final String name) {
        return mavenStringParameter(name, "");
    }

    protected static FieldSpec mavenStringParameter(final String name, final String value) {
        return FieldSpec.builder(ClassName.get(String.class), name)
                .addAnnotation(mavenParameterAnnotation(value))
                .initializer("$S", value)
                .build();
    }

    protected static FieldSpec mavenParameter(final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addAnnotation(mavenParameterAnnotation(""))
                .build();
    }

    protected static FieldSpec mavenParameter(
            final TypeName type,
            final String name,
            final String description,
            final String value) {
        return FieldSpec.builder(type, name)
                .addJavadoc(description)
                .addAnnotation(mavenParameterAnnotation(value))
                .build();
    }

    protected static FieldSpec mavenParameter(
            final String name,
            final String description,
            final boolean value) {
        return mavenParameter(TypeName.BOOLEAN, name, description, String.valueOf(value), CodeBlock.of("$L", value));
    }

    protected static FieldSpec mavenParameter(
            final String name,
            final String description,
            final int value) {
        return mavenParameter(TypeName.INT, name, description, String.valueOf(value), CodeBlock.of("$L", value));
    }

    protected static FieldSpec mavenParameter(
            final String name,
            final String description,
            final String value) {
        return mavenParameter(ClassName.get(String.class), name, description, value, CodeBlock.of("$S", value));
    }

    protected static FieldSpec mavenParameter(
            final TypeName type,
            final String name,
            final String description,
            final String value,
            final CodeBlock initializer) {
        return FieldSpec.builder(type, name)
                .addJavadoc(description)
                .addAnnotation(mavenParameterAnnotation(value))
                .initializer(initializer)
                .build();
    }

    private static AnnotationSpec mavenParameterAnnotation(final String defaultValue) {
        return AnnotationSpec.builder(TypicalTypes.MAVEN_PARAMETER)
                .addMember("required", "true")
                .addMember("defaultValue", "$S", defaultValue)
                .build();
    }

    protected static MethodSpec immutableBuilder(final String groupName) {
        final var configName = immutableType(immutableConfigurationName(groupName));
        return MethodSpec.methodBuilder(BUILDER_METHOD_NAME)
                .addJavadoc("@return Builder for new $L.", configurationName(groupName))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(configName.nestedClass("Builder"))
                .addStatement("return $T.builder()", configName)
                .build();
    }

    protected static MethodSpec immutableCopyOf(final String groupName) {
        final var name = immutableType(configurationName(groupName));
        final var immutable = immutableType(immutableConfigurationName(groupName));
        return MethodSpec.methodBuilder(COPY_OF_METHOD_NAME)
                .addJavadoc("@return A copy of $L.", configurationName(groupName))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(name, "configuration", Modifier.FINAL).build())
                .returns(immutable)
                .addStatement("return $T.copyOf($L)", immutable, "configuration")
                .build();
    }

    protected static String configurationName(final String name) {
        return name + "Configuration";
    }

    protected static String immutableConfigurationName(final String name) {
        return "Immutable" + configurationName(name);
    }

    protected static AnnotationSpec immutableAnnotation() {
        return AnnotationSpec.builder(Value.Immutable.class).build();
    }

    protected static AnnotationSpec immutableDefault() {
        return AnnotationSpec.builder(Value.Default.class).build();
    }

    protected static MethodSpec immutableMethod(
            final String settingName,
            final String description,
            final boolean value) {
        return immutableMethod(TypeName.BOOLEAN, settingName, description, CodeBlock.of("$L", value));
    }

    protected static MethodSpec immutableMethod(
            final String settingName,
            final String description,
            final int value) {
        return immutableMethod(TypeName.INT, settingName, description, CodeBlock.of("$L", value));
    }

    protected static MethodSpec immutableMethod(
            final String settingName,
            final String description,
            final String value) {
        return immutableMethod(ClassName.get(String.class), settingName, description, CodeBlock.of("$S", value));
    }

    protected static MethodSpec immutableMethod(
            final TypeName type,
            final String settingName,
            final String description,
            final CodeBlock returnCode) {
        return MethodSpec.methodBuilder(settingName)
                .addJavadoc(description)
                .addAnnotation(immutableDefault())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .returns(type)
                .addStatement("return $L", returnCode)
                .build();
    }

    protected static MethodSpec immutableMethod(
            final TypeName type,
            final String settingName,
            final String description,
            final AnnotationSpec... annotations) {
        return MethodSpec.methodBuilder(settingName)
                .addJavadoc(description)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotations(Arrays.asList(annotations))
                .returns(type.toString().startsWith("java.util.List") ? type : TypicalTypes.optionalOf(type))
                .build();
    }

    protected static FieldSpec picocliOption(
            final TypeName type,
            final String groupName,
            final String settingName,
            final String description) {
        return FieldSpec.builder(type, settingName)
                .addAnnotation(picocliOptionAnnotation(groupName, settingName, description))
                .build();
    }

    protected static FieldSpec picocliOption(
            final String groupName,
            final String settingName,
            final String description,
            final boolean value) {
        return picocliOption(TypeName.BOOLEAN, groupName, settingName, description, String.valueOf(value));
    }

    protected static FieldSpec picocliOption(
            final String groupName,
            final String settingName,
            final String description,
            final int value) {
        return picocliOption(TypeName.INT, groupName, settingName, description, String.valueOf(value));
    }

    protected static FieldSpec picocliOption(
            final String groupName,
            final String settingName,
            final String description,
            final String value) {
        return picocliOption(ClassName.get(String.class), groupName, settingName, description, value);
    }

    protected static FieldSpec picocliOption(
            final TypeName type,
            final String groupName,
            final String settingName,
            final String description,
            final String value) {
        return FieldSpec.builder(type, settingName)
                .addAnnotation(picocliOptionAnnotation(groupName, settingName, description, value))
                .build();
    }

    private static AnnotationSpec picocliOptionAnnotation(
            final String groupName,
            final String settingName,
            final String description) {
        final var option = ClassName.get("picocli.CommandLine", "Option");
        final var builder = AnnotationSpec.builder(option)
                .addMember("names", "$S", cliOptionName(groupName, settingName))
                .addMember("description", "$S", description);
        return builder.build();
    }

    private static AnnotationSpec picocliOptionAnnotation(
            final String groupName,
            final String settingName,
            final String description,
            final String value) {
        final var option = ClassName.get("picocli.CommandLine", "Option");
        final var builder = AnnotationSpec.builder(option)
                .addMember("names", "$S", cliOptionName(groupName, settingName))
                .addMember("description", "$S", description)
                .addMember("defaultValue", "$S", value);
        return builder.build();
    }

    private static String cliOptionName(final String groupName, final String settingName) {
        return "--" + Strings.lowerCase(groupName) + "-" + Strings.kebabCase(settingName);
    }

    protected static FieldSpec antField(
            final TypeName type,
            final String settingName,
            final String description) {
        return FieldSpec.builder(type, settingName, Modifier.PRIVATE)
                .addJavadoc(description)
                .build();
    }

    protected static FieldSpec antField(
            final String name,
            final String description,
            final boolean value) {
        return antField(TypeName.BOOLEAN, name, description, CodeBlock.of("$L", value));
    }

    protected static FieldSpec antField(
            final String name,
            final String description,
            final int value) {
        return antField(TypeName.INT, name, description, CodeBlock.of("$L", value));
    }

    protected static FieldSpec antField(
            final String name,
            final String description,
            final String value) {
        return antField(ClassName.get(String.class), name, description, CodeBlock.of("$S", value));
    }

    protected static FieldSpec antField(
            final TypeName type,
            final String name,
            final String description,
            final CodeBlock initializer) {
        return FieldSpec.builder(type, name, Modifier.PRIVATE)
                .addJavadoc(description)
                .initializer(initializer)
                .build();
    }

    protected static MethodSpec antSetter(
            final TypeName type,
            final String settingName,
            final String description) {
        return MethodSpec.methodBuilder("set" + upperCase(settingName))
                .addJavadoc(description)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(type, settingName, Modifier.FINAL)
                .addStatement(CodeBlock.of("this.$L = $L", settingName, settingName))
                .build();
    }

    protected static MethodSpec antAdder(
            final TypeName type,
            final String settingName,
            final String description) {
        return MethodSpec.methodBuilder("addConfigured" + upperCase(settingName))
                .addJavadoc(description)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(type, settingName, Modifier.FINAL)
                .addStatement(CodeBlock.of("this.$L.add($L)", settingName, settingName))
                .build();
    }

    protected static MethodSpec gradleConstructor() {
        return MethodSpec.constructorBuilder()
                .addJavadoc("Required by Gradle")
                .addAnnotation(TypicalTypes.INJECT)
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    protected static MethodSpec gradleStringProperty(final String name, final String description) {
        return gradleProperty(gradlePropertyOf(ClassName.get(String.class)), name, description);
    }

    protected static MethodSpec gradleNamedProperty(final TypeName type, final String name, final String description) {
        return gradleProperty(TypicalTypes.gradleContainerOf(type), name, description);
    }

    protected static MethodSpec gradleProperty(final TypeName type, final String name, final String description) {
        return gradleProperty(type, name, description, TypicalTypes.GRADLE_INPUT);
    }

    protected static MethodSpec gradleProperty(
            final TypeName type,
            final String name,
            final String description,
            final ClassName annotation) {
        return MethodSpec.methodBuilder(gradlePropertyName(name))
                .addJavadoc("@return " + description)
                .addAnnotation(annotation)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(type)
                .build();
    }

    protected static String gradlePropertyName(final String name) {
        return "get" + upperCase(name);
    }

    protected static ImmutableConfigurationSetting.BuildFinal enumSetting(
            final String group,
            final String name,
            final String description,
            final Enum<?> value,
            final TypeName type) {
        return settingBuilder(name, description)
                .setGradleConvention(CodeBlock.of("$L().convention($T.$L)", gradlePropertyName(name), value.getClass(), value.name()))
                .addAntFields(antField(type, name, description, CodeBlock.of("$T.$L", value.getClass(), value.name())))
                .addAntMethods(antSetter(type, name, description))
                .addCliFields(picocliOption(type, group, name, description, value.name()))
                .addGradleMethods(gradleProperty(gradlePropertyOf(type), name, description))
                .addImmutableMethods(immutableMethod(type, name, description, CodeBlock.of("$T.$L", value.getClass(), value.name())))
                .addMavenFields(mavenParameter(type, name, description, value.name(), CodeBlock.of("$T.$L", value.getClass(), value.name())));
    }

    protected static ImmutableConfigurationSetting.BuildFinal setting(
            final String group,
            final String name,
            final String description,
            final boolean value) {
        final var type = TypeName.BOOLEAN;
        return settingBuilder(name, description)
                .setGradleConvention(CodeBlock.of("$L().convention($L)", gradlePropertyName(name), value))
                .addAntFields(antField(name, description, value))
                .addAntMethods(antSetter(type, name, description))
                .addCliFields(picocliOption(group, name, description, value))
                .addGradleMethods(gradleProperty(gradlePropertyOf(type.box()), name, description))
                .addImmutableMethods(immutableMethod(name, description, value))
                .addMavenFields(mavenParameter(name, description, value));
    }

    protected static ImmutableConfigurationSetting.BuildFinal setting(
            final String group,
            final String name,
            final String description,
            final int value) {
        final var type = TypeName.INT;
        return settingBuilder(name, description)
                .setGradleConvention(CodeBlock.of("$L().convention($L)", gradlePropertyName(name), value))
                .addAntFields(antField(name, description, value))
                .addAntMethods(antSetter(type, name, description))
                .addCliFields(picocliOption(group, name, description, value))
                .addGradleMethods(gradleProperty(gradlePropertyOf(type.box()), name, description))
                .addImmutableMethods(immutableMethod(name, description, value))
                .addMavenFields(mavenParameter(name, description, value));
    }

    protected static ImmutableConfigurationSetting.BuildFinal setting(
            final String group,
            final String name,
            final String description,
            final String value) {
        final var type = ClassName.get(String.class);
        return settingBuilder(name, description)
                .setGradleConvention(CodeBlock.of("$L().convention($S)", gradlePropertyName(name), value))
                .addAntFields(antField(name, description, value))
                .addAntMethods(antSetter(type, name, description))
                .addCliFields(picocliOption(group, name, description, value))
                .addGradleMethods(gradleProperty(gradlePropertyOf(type), name, description))
                .addImmutableMethods(immutableMethod(name, description, value))
                .addMavenFields(mavenParameter(name, description, value));
    }

    private static ImmutableConfigurationSetting.BuildFinal settingBuilder(final String name, final String description) {
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setAntInitializer(CodeBlock.of(".set$L($L)\n", upperCase(name), name))
                .setCliInitializer(CodeBlock.of(".set$L($L)\n", upperCase(name), name))
                .setGradleInitializer(CodeBlock.of(".set$L($L().get())\n", upperCase(name), gradlePropertyName(name)))
                .setMavenInitializer(CodeBlock.of(".set$L($L)\n", upperCase(name), name));
    }

}
