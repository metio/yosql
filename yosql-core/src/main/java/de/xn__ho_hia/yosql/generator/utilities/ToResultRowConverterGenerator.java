/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.utilities;

import java.sql.SQLException;

import javax.inject.Inject;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;

@SuppressWarnings("nls")
final class ToResultRowConverterGenerator {

    static final String                  TO_RESULT_ROW_CONVERTER_CLASS_NAME = "ToResultRowConverter";

    private final AnnotationGenerator    annotations;
    private final ExecutionConfiguration configuration;

    @Inject
    ToResultRowConverterGenerator(
            final AnnotationGenerator annotations,
            final ExecutionConfiguration configuration) {
        this.annotations = annotations;
        this.configuration = configuration;
    }

    public PackageTypeSpec generateToResultRowConverterClass() {
        final String packageName = configuration.basePackageName() + "." + configuration.converterPackageName();
        final TypeSpec type = TypicalTypes.publicClass(TO_RESULT_ROW_CONVERTER_CLASS_NAME)
                .addMethod(asUserType())
                .addAnnotations(annotations.generatedClass(ToResultRowConverterGenerator.class))
                .build();
        return new PackageTypeSpec(type, packageName);
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
