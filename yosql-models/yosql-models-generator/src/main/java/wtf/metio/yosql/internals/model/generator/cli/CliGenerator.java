/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator.cli;

import com.squareup.javapoet.*;
import picocli.CommandLine;
import wtf.metio.yosql.internals.javapoet.StandardClasses;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.internals.model.generator.api.AbstractFieldsGenerator;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;

public final class CliGenerator extends AbstractFieldsGenerator {

    private final String immutablesBasePackage;

    public CliGenerator(final String immutablesBasePackage) {
        this.immutablesBasePackage = immutablesBasePackage;
    }

    @Override
    public TypeSpec apply(final ConfigurationGroup group) {
        return StandardClasses.openClass(ClassName.bestGuess(group.name()))
                .addAnnotations(annotationsFor(group))
                .addJavadoc(group.description())
                .addModifiers(Modifier.PUBLIC)
                .addFields(defaultFields(group))
                .addFields(optionalFields(group))
                .addMethod(asConfiguration(group))
                .addMethods(defaultConverters(group))
                .build();
    }

    private MethodSpec asConfiguration(final ConfigurationGroup group) {
        return asConfiguration(group, immutablesBasePackage);
    }

    protected Iterable<AnnotationSpec> annotations(final ConfigurationGroup group, final ConfigurationSetting setting) {
        final var builder = AnnotationSpec.builder(CommandLine.Option.class)
                .addMember("names", "\"--$L-$L\"", Strings.lowerCase(group.name()), Strings.kebabCase(setting.name()))
                .addMember("description", "$S", setting.description());
        valueOf(setting).ifPresent(value -> builder
                .addMember("defaultValue", "$S", value));
        return List.of(builder.build());
    }

    @Override
    public TypeName typeOf(final ConfigurationSetting setting) {
        return setting.cliType().orElse(setting.type());
    }

    @Override
    public Optional<Object> valueOf(final ConfigurationSetting setting) {
        return setting.cliValue().or(setting::value);
    }

    @Override
    public List<AnnotationSpec> annotationsFor(final ConfigurationGroup group) {
        return group.cliAnnotations();
    }

    @Override
    public List<AnnotationSpec> annotationsFor(final ConfigurationSetting setting) {
        return setting.cliAnnotations();
    }

    @Override
    public List<MethodSpec> methodsFor(final ConfigurationGroup group) {
        return group.cliMethods();
    }

}
