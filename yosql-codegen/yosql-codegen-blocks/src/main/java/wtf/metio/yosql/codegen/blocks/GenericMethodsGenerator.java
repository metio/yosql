/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generic implementation of a {@link MethodsGenerator}. Delegates most of its work to the injected members.
 */
public final class GenericMethodsGenerator extends AbstractMethodsGenerator {

    private final ConstructorGenerator constructor;
    private final StandardMethodGenerator standardMethods;
    private final BatchMethodGenerator batchMethods;
    private final Java8StreamMethodGenerator streamMethods;
    private final RxJavaMethodGenerator rxjavaMethods;

    /**
     * @param constructor     The constructor generator to use.
     * @param standardMethods The generic methods generator to use.
     * @param batchMethods    The batch methods generator to use.
     * @param streamMethods   The Java8 stream methods generator to use.
     * @param rxjavaMethods   The RxJava methods generator to use.
     */
    public GenericMethodsGenerator(
            final ConstructorGenerator constructor,
            final StandardMethodGenerator standardMethods,
            final BatchMethodGenerator batchMethods,
            final Java8StreamMethodGenerator streamMethods,
            final RxJavaMethodGenerator rxjavaMethods) {
        this.constructor = constructor;
        this.standardMethods = standardMethods;
        this.batchMethods = batchMethods;
        this.streamMethods = streamMethods;
        this.rxjavaMethods = rxjavaMethods;
    }

    @Override
    public MethodSpec constructor(final List<SqlStatement> statements) {
        return constructor.forRepository(statements);
    }

    @Override
    public MethodSpec standardReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return standardMethods.standardReadMethod(configuration, statements);
    }

    @Override
    public MethodSpec standardWriteMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return standardMethods.standardWriteMethod(configuration, statements);
    }

    @Override
    public MethodSpec standardCallMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return standardMethods.standardCallMethod(configuration, statements);
    }

    @Override
    public MethodSpec batchWriteMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return batchMethods.batchWriteMethod(configuration, statements);
    }

    @Override
    public MethodSpec streamEagerReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return streamMethods.streamEagerMethod(configuration, statements);
    }

    @Override
    public MethodSpec streamLazyReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return streamMethods.streamLazyMethod(configuration, statements);
    }

    @Override
    public MethodSpec rxJavaReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return rxjavaMethods.rxJava2ReadMethod(configuration, statements);
    }

}
