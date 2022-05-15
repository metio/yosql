/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ClassName;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.blocks.Annotations;
import wtf.metio.yosql.codegen.blocks.Classes;
import wtf.metio.yosql.codegen.blocks.Javadoc;
import wtf.metio.yosql.codegen.lifecycle.CodegenLifecycle;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generic implementation of a {@link RepositoryGenerator} that delegates most of its work to other interfaces/classes.
 */
public final class DefaultRepositoryGenerator implements RepositoryGenerator {

    private final LocLogger logger;
    private final Annotations annotations;
    private final FieldsGenerator fields;
    private final MethodsGenerator methods;
    private final Javadoc javadoc;
    private final Classes classes;

    /**
     * @param logger        The logger to use.
     * @param annotations   The annotation generator to use.
     * @param classes       The classes builder to use.
     * @param javadoc       The javadoc generator to use.
     * @param fields        The fields generator to use.
     * @param methods       The methods generator to use.
     */
    public DefaultRepositoryGenerator(
            final LocLogger logger,
            final Annotations annotations,
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
        final var classBuilder = classes.publicClass(className)
                .addJavadoc(javadoc.repositoryJavadoc(sqlStatements))
                .addFields(fields.asFields(sqlStatements))
                .addMethods(methods.asMethods(sqlStatements))
                .addAnnotations(annotations.generatedClass());
        fields.staticInitializer(sqlStatements).ifPresent(classBuilder::addStaticBlock);
        final var repository = classBuilder.build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, className.packageName(), className.simpleName());
        return PackagedTypeSpec.of(repository, className.packageName());
    }

}
