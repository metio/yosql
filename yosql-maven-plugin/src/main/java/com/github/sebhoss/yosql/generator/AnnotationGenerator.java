package com.github.sebhoss.yosql.generator;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.plugin.PluginConfig;
import com.squareup.javapoet.AnnotationSpec;

@Named
@Singleton
public class AnnotationGenerator {

    private final PluginConfig pluginConfig;

    @Inject
    public AnnotationGenerator(final PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    public Iterable<AnnotationSpec> generatedClass(final Class<?> generatorClass) {
        final List<AnnotationSpec> annotations = new ArrayList<>(1);
        if (pluginConfig.isClassGeneratedAnnotation()) {
            annotations.add(generated(generatorClass));
        }
        return annotations;
    }

    public Iterable<AnnotationSpec> generatedField(final Class<?> generatorClass) {
        final List<AnnotationSpec> annotations = new ArrayList<>(1);
        if (pluginConfig.isFieldGeneratedAnnotation()) {
            annotations.add(generated(generatorClass));
        }
        return annotations;
    }

    public Iterable<AnnotationSpec> generatedMethod(final Class<?> generatorClass) {
        final List<AnnotationSpec> annotations = new ArrayList<>(1);
        if (pluginConfig.isMethodGeneratedAnnotation()) {
            annotations.add(generated(generatorClass));
        }
        return annotations;
    }

    public AnnotationSpec generated(final Class<?> generatorClass) {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", generatorClass.getName())
                .addMember("date", "$S", ZonedDateTime.now().toString())
                .addMember("comments", "$S", "DO NOT EDIT")
                .build();
    }

}
