/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator.immutables;

import com.squareup.javapoet.*;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.model.generator.api.AbstractMethodsGenerator;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;

public final class ImmutablesGenerator extends AbstractMethodsGenerator {

    private final String basePackageName;

    public ImmutablesGenerator(final String basePackageName) {
        this.basePackageName = basePackageName;
    }

    @Override
    public TypeSpec apply(final ConfigurationGroup group) {
        return TypeSpec.interfaceBuilder(group.configurationName())
                .addJavadoc(group.description())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Value.Immutable.class)
                .addAnnotations(annotationsFor(group))
                .addMethods(staticMethods(group))
                .addMethods(defaultMethods(group))
                .addMethods(optionalMethods(group))
                .addMethods(methodsFor(group))
                .build();
    }

    private Iterable<MethodSpec> staticMethods(final ConfigurationGroup group) {
        return List.of(builder(group), usingDefaults(group), copyOf(group));
    }

    private MethodSpec builder(final ConfigurationGroup group) {
        final var configName = ClassName.get(basePackageName, group.immutableConfigurationName());
        return MethodSpec.methodBuilder("builder")
                .addJavadoc("@return Builder for new $L", group.configurationName())
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(configName.nestedClass("Builder"))
                .addStatement("return $T.builder()", configName)
                .build();
    }

    private MethodSpec usingDefaults(final ConfigurationGroup group) {
        final var configName = ClassName.get(basePackageName, group.immutableConfigurationName());
        return MethodSpec.methodBuilder("usingDefaults")
                .addJavadoc("@return A file configuration using default values.")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(configName.nestedClass("Builder"))
                .addStatement("return $T.builder()", configName)
                .build();
    }

    private MethodSpec copyOf(final ConfigurationGroup group) {
        final var name = ClassName.get(basePackageName, group.configurationName());
        final var immutable = ClassName.get(basePackageName, group.immutableConfigurationName());
        return MethodSpec.methodBuilder("copyOf")
                .addJavadoc("@return A file configuration using default values.")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(name, "configuration", Modifier.FINAL).build())
                .returns(immutable)
                .addStatement("return $T.copyOf($L)", immutable, "configuration")
                .build();
    }

    @Override
    protected MethodSpec defaultMethod(final ConfigurationSetting setting) {
        return MethodSpec.methodBuilder(setting.name())
                .addJavadoc(setting.description())
                .addAnnotation(Value.Default.class)
                .addAnnotations(setting.immutableAnnotations())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .returns(typeOf(setting))
                .addCode(defaultReturn(setting))
                .build();
    }

    @Override
    public TypeName typeOf(final ConfigurationSetting setting) {
        return setting.type();
    }

    @Override
    public Optional<Object> valueOf(final ConfigurationSetting setting) {
        return setting.value();
    }

    @Override
    public List<AnnotationSpec> annotationsFor(final ConfigurationGroup group) {
        return group.immutableAnnotations();
    }

    @Override
    public List<AnnotationSpec> annotationsFor(final ConfigurationSetting setting) {
        return setting.immutableAnnotations();
    }

    @Override
    public List<MethodSpec> methodsFor(final ConfigurationGroup group) {
        return group.immutableMethods();
    }

}
