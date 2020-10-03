/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.utilities;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.blocks.api.Classes;
import wtf.metio.yosql.generator.blocks.api.Methods;
import wtf.metio.yosql.generator.blocks.api.Names;
import wtf.metio.yosql.generator.blocks.api.Parameters;
import wtf.metio.yosql.model.annotations.Utilities;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.internal.ApplicationEvents;
import wtf.metio.yosql.model.sql.PackageTypeSpec;

import javax.inject.Inject;
import java.sql.SQLException;

final class ToResultRowConverterGenerator {

    public static final String TO_RESULT_ROW_CONVERTER_CLASS_NAME = "ToResultRowConverter";

    private final LocLogger logger;
    private final RuntimeConfiguration runtimeConfiguration;
    private final Names names;
    private final AnnotationGenerator annotations;
    private final Classes classes;
    private final Methods methods;
    private final Parameters parameters;

    @Inject
    ToResultRowConverterGenerator(
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

    public PackageTypeSpec generateToResultRowConverterClass() {
        final var resultRowConverterClass = ClassName.get(
                runtimeConfiguration.names().basePackageName() + "." + runtimeConfiguration.names().converterPackageName(),
                TO_RESULT_ROW_CONVERTER_CLASS_NAME);
        final var type = classes.publicClass(resultRowConverterClass)
                .addMethod(asUserType())
                .addAnnotations(annotations.generatedClass())
                .build();
        logger.debug(ApplicationEvents.TYPE_GENERATED, resultRowConverterClass.packageName(),
                resultRowConverterClass.simpleName());
        return new PackageTypeSpec(type, resultRowConverterClass.packageName());
    }

    private MethodSpec asUserType() {
        return methods.publicMethod("asUserType")
                .addParameters(parameters.resultState(runtimeConfiguration.result().resultStateClass()))
                .addException(SQLException.class)
                .returns(runtimeConfiguration.result().resultRowClass())
                .addStatement("final $T $N = new $T($N.getColumnCount())", runtimeConfiguration.result().resultRowClass(),
                        runtimeConfiguration.jdbcNames().row(),
                        runtimeConfiguration.result().resultRowClass(),
                        names.result())
                .beginControlFlow("for (int $N = 1; $N <= $N.getColumnCount(); $N++)",
                        runtimeConfiguration.jdbcNames().index(),
                        runtimeConfiguration.jdbcNames().index(),
                        names.result(),
                        runtimeConfiguration.jdbcNames().index())
                .addStatement("$N.setColumnValue($N.getColumnName($N), $N.getResultSet().getObject($N))",
                        runtimeConfiguration.jdbcNames().row(),
                        names.result(),
                        runtimeConfiguration.jdbcNames().index(),
                        names.result(),
                        runtimeConfiguration.jdbcNames().index())
                .endControlFlow()
                .addStatement("return $N", runtimeConfiguration.jdbcNames().row())
                .build();
    }

}
