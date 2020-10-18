/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.generic;

import com.squareup.javapoet.ClassName;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.api.FieldsGenerator;
import wtf.metio.yosql.generator.api.MethodsGenerator;
import wtf.metio.yosql.generator.api.RepositoryGenerator;
import wtf.metio.yosql.generator.blocks.generic.Classes;
import wtf.metio.yosql.generator.blocks.generic.Javadoc;
import wtf.metio.yosql.model.internal.ApplicationEvents;
import wtf.metio.yosql.model.sql.PackagedTypeSpec;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

/**
 * Generic implementation of a {@link RepositoryGenerator}. Delegates most of its work to its injected members.
 */
public final class GenericRepositoryGenerator implements RepositoryGenerator {

    private final LocLogger logger;
    private final AnnotationGenerator annotations;
    private final FieldsGenerator fields;
    private final MethodsGenerator methods;
    private final Javadoc javadoc;
    private final Classes classes;

    /**
     * @param logger      The logger to use.
     * @param annotations The annotation generator to use.
     * @param classes     The classes builder to use.
     * @param javadoc     The javadoc generator to use.
     * @param fields      The fields generator to use.
     * @param methods     The methods generator to use.
     */
    public GenericRepositoryGenerator(
            final LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final FieldsGenerator fields,
            final MethodsGenerator methods) {
        this.annotations = annotations;
        this.methods = methods;
        this.fields = fields;
        this.javadoc = javadoc;
        this.logger = logger;
        this.classes = classes;
    }

    @Override
    public PackagedTypeSpec generateRepository(
            final String repositoryName,
            final List<SqlStatement> sqlStatements) {
        final var className = ClassName.bestGuess(repositoryName);
        final var repository = classes.publicClass(className)
                .addJavadoc(javadoc.repositoryJavadoc(sqlStatements))
                .addFields(fields.asFields(sqlStatements))
                .addMethods(methods.asMethods(sqlStatements))
                .addAnnotations(annotations.generatedClass())
                .addStaticBlock(fields.staticInitializer(sqlStatements))
                .build();
        logger.debug(ApplicationEvents.TYPE_GENERATED, className.packageName(), className.simpleName());
        return PackagedTypeSpec.of(repository, className.packageName());
    }

}
