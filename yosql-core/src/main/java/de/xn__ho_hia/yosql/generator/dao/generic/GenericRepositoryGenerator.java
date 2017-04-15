/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.dao.generic;

import java.util.List;

import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.api.FieldsGenerator;
import de.xn__ho_hia.yosql.generator.api.MethodsGenerator;
import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.api.TypeWriter;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;

/**
 * Generic standard implementation of a {@link RepositoryGenerator}.
 */
public final class GenericRepositoryGenerator implements RepositoryGenerator {

    private final TypeWriter             typeWriter;
    private final ExecutionConfiguration configuration;
    private final MethodsGenerator       methods;
    private final FieldsGenerator        fields;
    private final AnnotationGenerator    annotations;

    /**
     * @param typeWriter
     *            The type writer to use.
     * @param configuration
     *            The configuration to use.
     * @param annotations
     *            The annotation generator to use.
     * @param methods
     *            The methods generator to use.
     * @param fields
     *            The fields generator to use.
     */
    public GenericRepositoryGenerator(
            final TypeWriter typeWriter,
            final ExecutionConfiguration configuration,
            final AnnotationGenerator annotations,
            final MethodsGenerator methods,
            final FieldsGenerator fields) {
        this.typeWriter = typeWriter;
        this.configuration = configuration;
        this.annotations = annotations;
        this.methods = methods;
        this.fields = fields;
    }

    @Override
    public void generateRepository(
            final String repositoryName,
            final List<SqlStatement> sqlStatements) {
        final String className = TypicalNames.getClassName(repositoryName);
        final String packageName = TypicalNames.getPackageName(repositoryName);
        final TypeSpec repository = TypicalTypes.publicClass(className)
                .addFields(fields.asFields(sqlStatements))
                .addMethods(methods.asMethods(sqlStatements))
                .addAnnotations(annotations.generatedClass(GenericRepositoryGenerator.class))
                .addStaticBlock(fields.staticInitializer(sqlStatements))
                .build();
        typeWriter.writeType(configuration.outputBaseDirectory(), packageName, repository);
    }

}
