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
    private final PluginConfig                    pluginConfig;
    private final RawJdbcMethodGenerator          methodGenerator;
    private final RawJdbcRepositoryFieldGenerator fieldGenerator;
    private final AnnotationGenerator             annotations;

    @Inject
    public RawJdbcRepositoryGenerator(
            final TypeWriter typeWriter,
            final PluginConfig pluginConfig,
            final AnnotationGenerator annotations,
            final RawJdbcMethodGenerator methodGenerator,
            final DefaultUtilitiesGenerator utilsGenerator,
            final RawJdbcRepositoryFieldGenerator fieldGenerator) {
        this.typeWriter = typeWriter;
        this.pluginConfig = pluginConfig;
        this.annotations = annotations;
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
                .addAnnotations(annotations.generatedClass(RawJdbcRepositoryGenerator.class))
                .addStaticBlock(fieldGenerator.staticInitializer(sqlStatements))
                .build();
        typeWriter.writeType(pluginConfig.getOutputBaseDirectory().toPath(), packageName, repository);
    }

}
