/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc.utilities;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.api.AnnotationGenerator;
import wtf.metio.yosql.codegen.api.Classes;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.codegen.lifecycle.CodegenLifecycle;
import wtf.metio.yosql.codegen.logging.Utilities;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;

import javax.inject.Inject;
import java.sql.SQLException;

public final class ToResultRowConverterGenerator {

    public static final String TO_RESULT_ROW_CONVERTER_CLASS_NAME = "ToResultRowConverter";

    private final LocLogger logger;
    private final JdbcConfiguration jdbcConfiguration;
    private final NamesConfiguration names;
    private final AnnotationGenerator annotations;
    private final Classes classes;
    private final Methods methods;
    private final Parameters parameters;

    @Inject
    public ToResultRowConverterGenerator(
            final @Utilities LocLogger logger,
            final JdbcConfiguration jdbcConfiguration,
            final NamesConfiguration names,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Methods methods,
            final Parameters parameters) {
        this.logger = logger;
        this.jdbcConfiguration = jdbcConfiguration;
        this.names = names;
        this.annotations = annotations;
        this.classes = classes;
        this.methods = methods;
        this.parameters = parameters;
    }

    public PackagedTypeSpec generateToResultRowConverterClass() {
        final var resultRowConverterClass = ClassName.get(
                jdbcConfiguration.utilityPackageName(),
                TO_RESULT_ROW_CONVERTER_CLASS_NAME);
        // TODO: add 'implements Function<ResultRow, USER_TYPE>' when running in Java8+
        final var type = classes.publicClass(resultRowConverterClass)
                .addMethod(apply())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, resultRowConverterClass.packageName(),
                resultRowConverterClass.simpleName());
        return PackagedTypeSpec.of(type, resultRowConverterClass.packageName());
    }

    private MethodSpec apply() {
        return methods.publicMethod("apply")
                .addParameters(parameters.resultState(jdbcConfiguration.resultStateClass()))
                .addException(SQLException.class)
                .returns(jdbcConfiguration.resultRowClass())
                // TODO: handle final/var usage here with Variables interface
                .addStatement("final $T $N = new $T($N.getColumnCount())", jdbcConfiguration.resultRowClass(),
                        names.row(),
                        jdbcConfiguration.resultRowClass(),
                        names.result())
                // TODO: use ControlFlows interface here
                .beginControlFlow("for (int $N = 1; $N <= $N.getColumnCount(); $N++)",
                        names.indexVariable(),
                        names.indexVariable(),
                        names.result(),
                        names.indexVariable())
                .addStatement("$N.setColumnValue($N.getColumnName($N), $N.getResultSet().getObject($N))",
                        names.row(),
                        names.result(),
                        names.indexVariable(),
                        names.result(),
                        names.indexVariable())
                .endControlFlow()
                .addStatement("return $N", names.row())
                .build();
    }

}
