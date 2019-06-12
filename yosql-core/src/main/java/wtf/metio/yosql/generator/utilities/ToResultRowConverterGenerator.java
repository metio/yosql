/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.utilities;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.model.annotations.Utilities;
import wtf.metio.yosql.model.ApplicationEvents;
import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.model.PackageTypeSpec;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.helpers.TypicalMethods;
import wtf.metio.yosql.generator.helpers.TypicalNames;
import wtf.metio.yosql.generator.helpers.TypicalParameters;
import wtf.metio.yosql.generator.helpers.TypicalTypes;

import javax.inject.Inject;
import java.sql.SQLException;

public final class ToResultRowConverterGenerator {

    public static final String                  TO_RESULT_ROW_CONVERTER_CLASS_NAME = "ToResultRowConverter";

    private final AnnotationGenerator annotations;
    private final ExecutionConfiguration configuration;
    private final LocLogger              logger;

    @Inject
    ToResultRowConverterGenerator(
            final AnnotationGenerator annotations,
            final ExecutionConfiguration configuration,
            final @Utilities LocLogger logger) {
        this.annotations = annotations;
        this.configuration = configuration;
        this.logger = logger;
    }

    public PackageTypeSpec generateToResultRowConverterClass() {
        final ClassName resultRowConverterClass = ClassName.get(
                configuration.basePackageName() + "." + configuration.converterPackageName(),
                TO_RESULT_ROW_CONVERTER_CLASS_NAME);
        final TypeSpec type = TypicalTypes.publicClass(resultRowConverterClass)
                .addMethod(asUserType())
                .addAnnotations(annotations.generatedClass(ToResultRowConverterGenerator.class))
                .build();
        logger.debug(ApplicationEvents.TYPE_GENERATED, resultRowConverterClass.packageName(),
                resultRowConverterClass.simpleName());
        return new PackageTypeSpec(type, resultRowConverterClass.packageName());
    }

    private MethodSpec asUserType() {
        return TypicalMethods.publicMethod("asUserType")
                .addParameters(TypicalParameters.resultState(configuration.getResultStateClass()))
                .addException(SQLException.class)
                .returns(configuration.getResultRowClass())
                .addStatement("final $T $N = new $T($N.getColumnCount())", configuration.getResultRowClass(),
                        TypicalNames.ROW, configuration.getResultRowClass(), TypicalNames.RESULT)
                .beginControlFlow("for (int $N = 1; $N <= $N.getColumnCount(); $N++)",
                        TypicalNames.INDEX, TypicalNames.INDEX, TypicalNames.RESULT, TypicalNames.INDEX)
                .addStatement("$N.setColumnValue($N.getColumnName($N), $N.getResultSet().getObject($N))",
                        TypicalNames.ROW, TypicalNames.RESULT, TypicalNames.INDEX, TypicalNames.RESULT,
                        TypicalNames.INDEX)
                .endControlFlow()
                .addStatement("return $N", TypicalNames.ROW)
                .build();
    }

}
