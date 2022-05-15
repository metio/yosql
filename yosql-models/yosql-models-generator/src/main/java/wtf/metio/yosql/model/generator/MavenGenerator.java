/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.generator;

import com.squareup.javapoet.*;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class MavenGenerator extends AbstractFieldsBasedGenerator {

    private final String immutablesBasePackage;

    public MavenGenerator(final String immutablesBasePackage) {
        this.immutablesBasePackage = immutablesBasePackage;
    }

    @Override
    public Stream<TypeSpec> apply(final ConfigurationGroup group) {
        if ("Converter".equalsIgnoreCase(group.name())) {
            return Stream.of(configGroupClass(group), rowConverterClass());
        }
        return Stream.of(configGroupClass(group));
    }

    private TypeSpec configGroupClass(final ConfigurationGroup group) {
        return TypeSpec.classBuilder(ClassName.bestGuess(group.name()))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotations(annotationsFor(group))
                .addJavadoc(group.description())
                .addFields(defaultFields(group, Modifier.PRIVATE))
                .addMethod(asConfiguration(group))
                .addMethods(methodsFor(group))
                .build();
    }

    private TypeSpec rowConverterClass() {
        final var mavenParameter = ClassName.bestGuess("org.apache.maven.plugins.annotations.Parameter");
        return TypeSpec.classBuilder(ClassName.bestGuess("RowConverter"))
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(String.class, "alias")
                        .addAnnotation(AnnotationSpec.builder(mavenParameter)
                                .addMember("required", "true")
                                .addMember("defaultValue", "$S", "")
                                .build())
                        .initializer("$S", "")
                        .build())
                .addField(FieldSpec.builder(String.class, "converterType")
                        .addAnnotation(AnnotationSpec.builder(mavenParameter)
                                .addMember("required", "true")
                                .addMember("defaultValue", "$S", "")
                                .build())
                        .initializer("$S", "")
                        .build())
                .addField(FieldSpec.builder(String.class, "methodName")
                        .addAnnotation(AnnotationSpec.builder(mavenParameter)
                                .addMember("required", "true")
                                .addMember("defaultValue", "$S", "")
                                .build())
                        .initializer("$S", "")
                        .build())
                .addField(FieldSpec.builder(String.class, "resultType")
                        .addAnnotation(AnnotationSpec.builder(mavenParameter)
                                .addMember("required", "true")
                                .addMember("defaultValue", "$S", "")
                                .build())
                        .initializer("$S", "")
                        .build())
                .addMethod(MethodSpec.methodBuilder("asRowConverter")
                        .addModifiers(Modifier.FINAL)
                        .returns(ResultRowConverter.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()", ResultRowConverter.class)
                                .add("$>\n.setAlias(alias)")
                                .add("\n.setConverterType(converterType)")
                                .add("\n.setMethodName(methodName)")
                                .add("\n.setResultType(resultType)")
                                .add("\n.build()$<")
                                .build())
                        .build())
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
