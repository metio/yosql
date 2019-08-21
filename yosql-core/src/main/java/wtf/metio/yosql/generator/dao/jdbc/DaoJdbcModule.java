/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.jdbc;

import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.dagger.Delegating;
import wtf.metio.yosql.model.annotations.Generator;
import wtf.metio.yosql.model.ExecutionConfiguration;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.generator.api.*;
import wtf.metio.yosql.generator.dao.generic.GenericMethodsGenerator;
import wtf.metio.yosql.generator.dao.generic.GenericRepositoryGenerator;
import wtf.metio.yosql.generator.helpers.TypicalCodeBlocks;
import wtf.metio.yosql.generator.helpers.TypicalFields;

/**
 * Dagger module for the JDBC based DAO implementation.
 */
@Module
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
