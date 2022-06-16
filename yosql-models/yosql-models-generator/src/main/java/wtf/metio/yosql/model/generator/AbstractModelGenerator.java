/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.generator;

import com.squareup.javapoet.*;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static wtf.metio.yosql.models.meta.data.ToolingPackages.IMMUTABLES_PACKAGE;

abstract class AbstractModelGenerator implements Function<ConfigurationGroup, Stream<TypeSpec>> {

    public final Stream<TypeSpec> apply(final ConfigurationGroup group) {
        return Stream.concat(Stream.of(configurationGroupClass(group)), typesFor(group).stream());
    }

    protected abstract List<TypeSpec> typesFor(ConfigurationGroup group);

    protected abstract List<AnnotationSpec> annotationsFor(ConfigurationGroup group);

    protected abstract List<MethodSpec> methodsFor(ConfigurationGroup group);

    protected abstract List<MethodSpec> methodsFor(ConfigurationSetting setting);

    protected abstract List<FieldSpec> fieldsFor(ConfigurationGroup group);

    protected abstract List<FieldSpec> fieldsFor(ConfigurationSetting setting);

    protected abstract CodeBlock initialize(ConfigurationSetting setting);

    protected abstract CodeBlock convention(ConfigurationSetting setting);

    protected abstract List<ParameterSpec> immutableConfigurationParametersFor(ConfigurationGroup group);

    protected abstract List<ParameterSpec> gradleConventionParametersFor(ConfigurationGroup group);

    protected abstract boolean convertsToImmutable();

    protected abstract boolean hasConvention();

    protected abstract Modifier[] configurationGroupClassModifiers();

    protected abstract TypeSpec.Builder configurationGroupClassBuilder(ConfigurationGroup group);

    private TypeSpec configurationGroupClass(final ConfigurationGroup group) {
        return configurationGroupClassBuilder(group)
                .addModifiers(configurationGroupClassModifiers())
                .addAnnotations(annotationsFor(group))
                .addJavadoc(group.description())
                .addFields(fieldsFor(group))
                .addFields(fieldsForSettingsIn(group))
                .addMethods(methodsFor(group))
                .addMethods(methodsForSettingsIn(group))
                .addMethods(asConfiguration(group))
                .addMethods(asConvention(group))
                .build();
    }

    private List<FieldSpec> fieldsForSettingsIn(final ConfigurationGroup group) {
        return group.settings().stream()
                .map(this::fieldsFor)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<MethodSpec> methodsForSettingsIn(final ConfigurationGroup group) {
        return group.settings().stream()
                .map(this::methodsFor)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<MethodSpec> asConfiguration(final ConfigurationGroup group) {
        return convertsToImmutable() ? List.of(asImmutableConfiguration(group)) : List.of();
    }

    private List<MethodSpec> asConvention(final ConfigurationGroup group) {
        return hasConvention() ? List.of(asGradleConvention(group)) : List.of();
    }

    private MethodSpec asImmutableConfiguration(final ConfigurationGroup group) {
        final var returnType = ClassName.get(IMMUTABLES_PACKAGE, group.configurationName());
        final var builder = MethodSpec.methodBuilder("asConfiguration")
                .addModifiers(Modifier.PUBLIC)
                .addParameters(immutableConfigurationParametersFor(group))
                .returns(returnType);
        final var configBuilder = CodeBlock.builder()
                .add("return $T.builder()\n", returnType);
        group.settings().stream()
                .map(this::initialize)
                .forEach(configBuilder::add);
        configBuilder.add(".build()");
        return builder.addStatement(configBuilder.build()).build();
    }

    private MethodSpec asGradleConvention(final ConfigurationGroup group) {
        final var builder = MethodSpec.methodBuilder("configureConventions")
                .addModifiers(Modifier.PUBLIC)
                .addParameters(gradleConventionParametersFor(group));;
        group.settings().stream()
                .map(this::convention)
                .filter(code -> !code.toString().isBlank()) // TODO: use Optional instead?
                .forEach(builder::addStatement);
        return builder.build();
    }

}
