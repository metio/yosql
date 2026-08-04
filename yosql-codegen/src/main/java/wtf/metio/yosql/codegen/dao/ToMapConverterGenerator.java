/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.codegen.lifecycle.CodegenLifecycle;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import static wtf.metio.yosql.codegen.blocks.CodeBlocks.code;

/**
 * Generates a converter that turns ResultStates into Maps.
 */
public final class ToMapConverterGenerator {

    private final LocLogger logger;
    private final ConverterConfiguration converters;
    private final Annotations annotations;
    private final Classes classes;
    private final Methods methods;
    private final Variables variables;
    private final ControlFlows controlFlows;
    private final JdbcParameters jdbcParameters;
    private final JdbcBlocks jdbcBlocks;
    private final MethodExceptionHandler exceptions;

    public ToMapConverterGenerator(
            final LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final Annotations annotations,
            final Classes classes,
            final Methods methods,
            final Variables variables,
            final ControlFlows controlFlows,
            final JdbcParameters jdbcParameters,
            final JdbcBlocks jdbcBlocks,
            final MethodExceptionHandler exceptions) {
        this.logger = logger;
        this.converters = runtimeConfiguration.converter();
        this.annotations = annotations;
        this.classes = classes;
        this.methods = methods;
        this.variables = variables;
        this.controlFlows = controlFlows;
        this.jdbcParameters = jdbcParameters;
        this.jdbcBlocks = jdbcBlocks;
        this.exceptions = exceptions;
    }

    public PackagedTypeSpec generateToMapConverterClass() {
        final var mapConverterClass = ClassName.bestGuess(converters.mapConverterClass());
        final var type = classes.publicClass(mapConverterClass)
                .addJavadoc("Disable generating this class by setting 'generateMapConverter' to 'false'.")
                .addAnnotations(annotations.generatedClass())
                .addMethod(toMapMethod())
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, mapConverterClass.packageName(),
                mapConverterClass.simpleName());
        return PackagedTypeSpec.of(type, mapConverterClass.packageName());
    }

    private MethodSpec toMapMethod() {
        return methods.publicMethod(converters.mapConverterMethod())
                .addJavadoc("Reads the current row into a map from column name to value.\n")
                .addJavadoc("\n@param $L The result set, positioned on the row to read.", GeneratedNames.RESULT_SET)
                .addJavadoc("\n@return The row, keyed by column name.")
                .addParameters(jdbcParameters.toMapConverterParameterSpecs())
                .addException(exceptions.thrownException())
                .returns(TypicalTypes.MAP_OF_STRING_AND_OBJECTS)
                .addCode(jdbcBlocks.getMetaDataStatement())
                .addStatement(variables.inline(
                        TypicalTypes.MAP_OF_STRING_AND_OBJECTS,
                        GeneratedNames.ROW,
                        "new $T($N.getColumnCount())",
                        TypicalTypes.LINKED_MAP_OF_STRING_AND_OBJECTS,
                        GeneratedNames.RESULT_SET_META_DATA))
                .addCode(controlFlows.forLoop(
                        code("int $N = 1; $N <= $N.getColumnCount(); $N++",
                                GeneratedNames.INDEX,
                                GeneratedNames.INDEX,
                                GeneratedNames.RESULT_SET_META_DATA,
                                GeneratedNames.INDEX),
                        CodeBlock.builder()
                                // The label, not the name: JDBC defines the label as the alias where
                                // one was given, so `select slug as handle` is reachable under the
                                // name the statement chose. getColumnName answers the underlying
                                // column, which on some drivers is the alias and on others is not.
                                .addStatement("$N.put($N.getColumnLabel($N), $N.getObject($N))",
                                        GeneratedNames.ROW,
                                        GeneratedNames.RESULT_SET_META_DATA,
                                        GeneratedNames.INDEX,
                                        GeneratedNames.RESULT_SET,
                                        GeneratedNames.INDEX)
                                .build()))
                .addStatement("return $N", GeneratedNames.ROW)
                .build();
    }

}
