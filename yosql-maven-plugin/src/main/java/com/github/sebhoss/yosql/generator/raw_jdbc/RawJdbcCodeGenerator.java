package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.generator.AnnotationGenerator;
import com.github.sebhoss.yosql.generator.CodeGenerator;
import com.github.sebhoss.yosql.generator.TypeWriter;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.generator.utils.DefaultUtilitiesGenerator;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.plugin.PluginRuntimeConfig;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class RawJdbcCodeGenerator implements CodeGenerator {

    private final TypeWriter                      typeWriter;
    private final PluginRuntimeConfig             runtimeConfig;
    private final DefaultUtilitiesGenerator       utilsGenerator;
    private final RawJdbcMethodGenerator          methodGenerator;
    private final RawJdbcRepositoryFieldGenerator fieldGenerator;
    private final AnnotationGenerator             annotationGenerator;

    @Inject
    public RawJdbcCodeGenerator(
            final TypeWriter typeWriter,
            final PluginRuntimeConfig runtimeConfig,
            final AnnotationGenerator annotationGenerator,
            final RawJdbcMethodGenerator methodGenerator,
            final DefaultUtilitiesGenerator utilsGenerator,
            final RawJdbcRepositoryFieldGenerator fieldGenerator) {
        this.typeWriter = typeWriter;
        this.runtimeConfig = runtimeConfig;
        this.annotationGenerator = annotationGenerator;
        this.methodGenerator = methodGenerator;
        this.utilsGenerator = utilsGenerator;
        this.fieldGenerator = fieldGenerator;
    }

    @Override
    public void generateUtilities(final List<SqlStatement> allStatements) {
        utilsGenerator.generateUtilities(allStatements);
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
                .addAnnotation(annotationGenerator.generated(RawJdbcCodeGenerator.class))
                .addStaticBlock(fieldGenerator.staticInitializer(sqlStatements))
                .build();
        typeWriter.writeType(runtimeConfig.getOutputBaseDirectory().toPath(), packageName, repository);
    }

}
