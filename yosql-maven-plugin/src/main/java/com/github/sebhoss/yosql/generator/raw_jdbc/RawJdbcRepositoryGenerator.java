package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.generator.AnnotationGenerator;
import com.github.sebhoss.yosql.generator.RepositoryGenerator;
import com.github.sebhoss.yosql.generator.TypeWriter;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.generator.utils.DefaultUtilitiesGenerator;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.plugin.PluginConfig;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class RawJdbcRepositoryGenerator implements RepositoryGenerator {

    private final TypeWriter                      typeWriter;
    private final PluginConfig                    runtimeConfig;
    private final RawJdbcMethodGenerator          methodGenerator;
    private final RawJdbcRepositoryFieldGenerator fieldGenerator;
    private final AnnotationGenerator             annotationGenerator;

    @Inject
    public RawJdbcRepositoryGenerator(
            final TypeWriter typeWriter,
            final PluginConfig runtimeConfig,
            final AnnotationGenerator annotationGenerator,
            final RawJdbcMethodGenerator methodGenerator,
            final DefaultUtilitiesGenerator utilsGenerator,
            final RawJdbcRepositoryFieldGenerator fieldGenerator) {
        this.typeWriter = typeWriter;
        this.runtimeConfig = runtimeConfig;
        this.annotationGenerator = annotationGenerator;
        this.methodGenerator = methodGenerator;
        this.fieldGenerator = fieldGenerator;
    }

    @Override
    public void generateRepository(
            final String repositoryName,
            final List<SqlStatement> sqlStatements) {
        final String className = TypicalNames.getClassName(repositoryName);
        final String packageName = TypicalNames.getPackageName(repositoryName);
        final TypeSpec repository = TypicalTypes.publicClass(className)
                .addFields(fieldGenerator.asFields(sqlStatements))
                .addMethods(methodGenerator.asMethods(sqlStatements))
                .addAnnotations(annotationGenerator.generatedClass(RawJdbcRepositoryGenerator.class))
                .addStaticBlock(fieldGenerator.staticInitializer(sqlStatements))
                .build();
        typeWriter.writeType(runtimeConfig.getOutputBaseDirectory().toPath(), packageName, repository);
    }

}
