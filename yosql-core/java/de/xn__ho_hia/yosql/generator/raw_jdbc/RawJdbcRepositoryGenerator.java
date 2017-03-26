/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.raw_jdbc;

import java.util.List;

import javax.inject.Inject;

import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.yosql.generator.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.TypeWriter;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;

@SuppressWarnings({ "javadoc" })
public class RawJdbcRepositoryGenerator implements RepositoryGenerator {

    private final TypeWriter                      typeWriter;
    private final ExecutionConfiguration                    configuration;
    private final RawJdbcMethodGenerator          methodGenerator;
    private final RawJdbcRepositoryFieldGenerator fieldGenerator;
    private final AnnotationGenerator             annotations;

    @Inject
    public RawJdbcRepositoryGenerator(
            final TypeWriter typeWriter,
            final ExecutionConfiguration configuration,
            final AnnotationGenerator annotations,
            final RawJdbcMethodGenerator methodGenerator,
            final RawJdbcRepositoryFieldGenerator fieldGenerator) {
        this.typeWriter = typeWriter;
        this.configuration = configuration;
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
        typeWriter.writeType(configuration.getOutputBaseDirectory(), packageName, repository);
    }

}
