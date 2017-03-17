/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.utils;

import java.sql.SQLException;

import javax.inject.Inject;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.yosql.generator.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.TypeWriter;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;

@SuppressWarnings({ "nls", "javadoc" })
public class ToResultRowConverterGenerator {

    public static final String        TO_RESULT_ROW_CONVERTER_CLASS_NAME = "ToResultRowConverter";
    public static final String        RESULT_ROW_CONVERTER_ALIAS         = "resultRowConverter";

    private final AnnotationGenerator annotations;
    private final TypeWriter          typeWriter;
    private final ExecutionConfiguration        configuration;

    @Inject
    public ToResultRowConverterGenerator(
            final AnnotationGenerator annotations,
            final TypeWriter typeWriter,
            final ExecutionConfiguration configuration) {
        this.annotations = annotations;
        this.typeWriter = typeWriter;
        this.configuration = configuration;
    }

    public void generateToResultRowConverterClass() {
        final String packageName = configuration.getBasePackageName() + "." + configuration.getConverterPackageName();
        final TypeSpec type = TypicalTypes.publicClass(TO_RESULT_ROW_CONVERTER_CLASS_NAME)
                .addMethod(asUserType())
                .addAnnotations(annotations.generatedClass(ToResultRowConverterGenerator.class))
                .build();
        typeWriter.writeType(configuration.getOutputBaseDirectory().toPath(), packageName, type);
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
