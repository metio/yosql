/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.dao.generic;

import java.util.List;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;
import com.squareup.javapoet.TypeSpec;

import org.slf4j.cal10n.LocLogger;

import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.api.FieldsGenerator;
import de.xn__ho_hia.yosql.generator.api.MethodsGenerator;
import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ApplicationEvents;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;
import de.xn__ho_hia.yosql.model.SqlStatement;

/**
 * Generic implementation of a {@link RepositoryGenerator}. Delegates most of its work to its injected members.
 */
public final class GenericRepositoryGenerator implements RepositoryGenerator {

    private final AnnotationGenerator annotations;
    private final FieldsGenerator     fields;
    private final MethodsGenerator    methods;
    private final LocLogger           logger;

    /**
     * @param annotations
     *            The annotation generator to use.
     * @param fields
     *            The fields generator to use.
     * @param methods
     *            The methods generator to use.
     * @param logger
     *            The logger to use.
     */
    public GenericRepositoryGenerator(
            final AnnotationGenerator annotations,
            final FieldsGenerator fields,
            final MethodsGenerator methods,
            final LocLogger logger) {
        this.annotations = annotations;
        this.methods = methods;
        this.fields = fields;
        this.logger = logger;
    }

    @Override
    public PackageTypeSpec generateRepository(
            final String repositoryName,
            final List<SqlStatement> sqlStatements) {
        final String className = TypicalNames.getClassName(repositoryName);
        final String packageName = TypicalNames.getPackageName(repositoryName);
        final TypeSpec repository = TypicalTypes.publicClass(className)
                .addJavadoc(javadoc(sqlStatements))
                .addFields(fields.asFields(sqlStatements))
                .addMethods(methods.asMethods(sqlStatements))
                .addAnnotations(annotations.generatedClass(GenericRepositoryGenerator.class))
                .addStaticBlock(fields.staticInitializer(sqlStatements))
                .build();
        logger.debug(ApplicationEvents.TYPE_GENERATED, packageName, repository.name);
        return new PackageTypeSpec(repository, packageName);
    }

    @SuppressWarnings("nls")
    private static CodeBlock javadoc(final List<SqlStatement> statements) {
        final Builder builder = CodeBlock.builder()
                .add("Generated based on the following files:\n")
                .add("<ul>\n");
        statements.stream()
                .map(SqlStatement::getSourcePath)
                .distinct()
                .forEach(path -> builder.add("<li>$L</li>\n", path));
        builder.add("</ul>\n");
        return builder.build();
    }

}
