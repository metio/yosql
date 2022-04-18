/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator.maven;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.javapoet.StandardClasses;
import wtf.metio.yosql.internals.model.generator.api.AbstractFieldsGenerator;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;

public final class MavenGenerator extends AbstractFieldsGenerator {

    private final String immutablesBasePackage;

    public MavenGenerator(final String immutablesBasePackage) {
        this.immutablesBasePackage = immutablesBasePackage;
    }

    @Override
    public TypeSpec apply(final ConfigurationGroup group) {
        return StandardClasses.openClass(ClassName.bestGuess(group.name()))
                .addAnnotations(annotationsFor(group))
                .addJavadoc(group.description())
                .addModifiers(Modifier.PUBLIC)
                .addFields(defaultFields(group, Modifier.PRIVATE))
                .addMethod(asConfiguration(group))
                .addMethods(resultRowConverters(group))
                .build();
    }

    private MethodSpec asConfiguration(final ConfigurationGroup group) {
        return asConfiguration(group, immutablesBasePackage);
    }

    @Override
    public TypeName typeOf(final ConfigurationSetting setting) {
        return setting.mavenType().orElse(setting.type());
    }

    @Override
    public Optional<Object> valueOf(final ConfigurationSetting setting) {
        return setting.mavenValue().or(setting::value);
    }

    @Override
    public List<AnnotationSpec> annotationsFor(final ConfigurationGroup group) {
        return group.mavenAnnotations();
    }

    @Override
    public List<AnnotationSpec> annotationsFor(final ConfigurationSetting setting) {
        return List.of(mavenParameter(setting));
    }

    private AnnotationSpec mavenParameter(final ConfigurationSetting setting) {
        final var builder = AnnotationSpec.builder(
                ClassName.bestGuess("org.apache.maven.plugins.annotations.Parameter"));
        valueOf(setting).ifPresent(value -> setMembers(value, setting, builder));
        return builder.build();
    }

    private void setMembers(final Object value, final ConfigurationSetting setting, final AnnotationSpec.Builder builder) {
        builder.addMember("required", "true");
        final var type = typeOf(setting);
        if (value instanceof String) {
            builder.addMember("defaultValue", "$S", value);
        } else if (type.isPrimitive() || type.isBoxedPrimitive()) {
            builder.addMember("defaultValue", "$S", value);
        } else if (value.getClass().isEnum()) {
            builder.addMember("defaultValue", "$S", value);
        } else {
            builder.addMember("defaultValue", "$L", defaultValue(value, type));
        }
    }

    @Override
    public List<MethodSpec> methodsFor(final ConfigurationGroup group) {
        return group.mavenMethods();
    }

}
