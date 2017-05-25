/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.dao.jdbc;

import org.slf4j.cal10n.LocLogger;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.dagger.Delegating;
import de.xn__ho_hia.yosql.dagger.LoggerModule.Generator;
import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.api.BatchMethodGenerator;
import de.xn__ho_hia.yosql.generator.api.FieldsGenerator;
import de.xn__ho_hia.yosql.generator.api.Java8StreamMethodGenerator;
import de.xn__ho_hia.yosql.generator.api.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.api.MethodsGenerator;
import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.api.RxJavaMethodGenerator;
import de.xn__ho_hia.yosql.generator.api.StandardMethodGenerator;
import de.xn__ho_hia.yosql.generator.dao.generic.GenericMethodsGenerator;
import de.xn__ho_hia.yosql.generator.dao.generic.GenericRepositoryGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;

/**
 * Dagger2 module for the JDBC based DAO implementation.
 */
@Module
@SuppressWarnings("static-method")
public class DaoJdbcModule {

    @JDBC
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @JDBC FieldsGenerator fields,
            final @JDBC MethodsGenerator methods,
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations) {
        return new GenericRepositoryGenerator(annotations, fields, methods, logger);
    }

    @JDBC
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @JDBC BatchMethodGenerator batchMethods,
            final @JDBC Java8StreamMethodGenerator streamMethods,
            final @JDBC RxJavaMethodGenerator rxjavaMethods,
            final @JDBC StandardMethodGenerator standardMethods,
            final AnnotationGenerator annotations) {
        return new GenericMethodsGenerator(batchMethods, streamMethods, rxjavaMethods, standardMethods, annotations);
    }

    @JDBC
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final @Delegating LoggingGenerator logging,
            final TypicalFields fields) {
        return new JdbcFieldsGenerator(fields, logging);
    }

    @JDBC
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        return new JdbcBatchMethodGenerator(codeBlocks, annotations);
    }

    @JDBC
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        return new JdbcJava8StreamMethodGenerator(codeBlocks, annotations);
    }

    @JDBC
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final ExecutionConfiguration configuration,
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        return new JdbcRxJavaMethodGenerator(configuration, codeBlocks, annotations);
    }

    @JDBC
    @Provides
    StandardMethodGenerator provideStandardMethodGenerator(
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        return new JdbcStandardMethodGenerator(codeBlocks, annotations);
    }

}
