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
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.lifecycle.CodegenLifecycle;
import wtf.metio.yosql.codegen.logging.Utilities;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Inject;
import java.sql.SQLException;

public final class ToResultRowConverterGenerator {

    public static final String TO_RESULT_ROW_CONVERTER_CLASS_NAME = "ToResultRowConverter";

    private final LocLogger logger;
    private final RuntimeConfiguration runtimeConfiguration;
    private final Names names;
    private final AnnotationGenerator annotations;
    private final Classes classes;
    private final Methods methods;
    private final Parameters parameters;

    @Inject
    public ToResultRowConverterGenerator(
            final @Utilities LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final Names names,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Methods methods,
            final Parameters parameters) {
        this.logger = logger;
        this.runtimeConfiguration = runtimeConfiguration;
        this.names = names;
        this.annotations = annotations;
        this.classes = classes;
        this.methods = methods;
        this.parameters = parameters;
    }

    public PackagedTypeSpec generateToResultRowConverterClass() {
        final var resultRowConverterClass = ClassName.get(
                runtimeConfiguration.jdbc().utilityPackageName(),
                TO_RESULT_ROW_CONVERTER_CLASS_NAME);
        final var type = classes.publicClass(resultRowConverterClass)
                .addMethod(asUserType())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, resultRowConverterClass.packageName(),
                resultRowConverterClass.simpleName());
        return PackagedTypeSpec.of(type, resultRowConverterClass.packageName());
    }

    private MethodSpec asUserType() {
        return methods.publicMethod("asUserType")
                .addParameters(parameters.resultState(runtimeConfiguration.jdbc().resultStateClass()))
                .addException(SQLException.class)
                .returns(runtimeConfiguration.jdbc().resultRowClass())
                .addStatement("final $T $N = new $T($N.getColumnCount())", runtimeConfiguration.jdbc().resultRowClass(),
                        runtimeConfiguration.jdbc().row(),
                        runtimeConfiguration.jdbc().resultRowClass(),
                        names.result())
                .beginControlFlow("for (int $N = 1; $N <= $N.getColumnCount(); $N++)",
                        runtimeConfiguration.jdbc().indexVariable(),
                        runtimeConfiguration.jdbc().indexVariable(),
                        names.result(),
                        runtimeConfiguration.jdbc().indexVariable())
                .addStatement("$N.setColumnValue($N.getColumnName($N), $N.getResultSet().getObject($N))",
                        runtimeConfiguration.jdbc().row(),
                        names.result(),
                        runtimeConfiguration.jdbc().indexVariable(),
                        names.result(),
                        runtimeConfiguration.jdbc().indexVariable())
                .endControlFlow()
                .addStatement("return $N", runtimeConfiguration.jdbc().row())
                .build();
    }

}
