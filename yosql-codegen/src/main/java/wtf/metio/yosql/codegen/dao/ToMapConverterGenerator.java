/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.codegen.lifecycle.CodegenLifecycle;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import static wtf.metio.yosql.codegen.blocks.CodeBlocks.code;

/**
 * Generates a converter that turns ResultStates into Maps.
 */
public final class ToMapConverterGenerator {

    private final LocLogger logger;
    private final ConverterConfiguration converters;
    private final NamesConfiguration names;
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
        this.names = runtimeConfiguration.names();
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
                // TODO: add javadoc with hint to disable generating this class
                .addAnnotations(annotations.generatedClass())
                .addMethod(toMapMethod())
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, mapConverterClass.packageName(),
                mapConverterClass.simpleName());
        return PackagedTypeSpec.of(type, mapConverterClass.packageName());
    }

    private MethodSpec toMapMethod() {
        return methods.publicMethod(converters.mapConverterMethod())
                .addParameters(jdbcParameters.toMapConverterParameterSpecs())
                .addException(exceptions.thrownException())
                .returns(TypicalTypes.MAP_OF_STRING_AND_OBJECTS)
                .addCode(jdbcBlocks.readMetaData())
                .addStatement(variables.inline(
                        TypicalTypes.MAP_OF_STRING_AND_OBJECTS,
                        names.row(),
                        "new $T($N.getColumnCount())",
                        TypicalTypes.LINKED_MAP_OF_STRING_AND_OBJECTS,
                        names.resultSetMetaData()))
                .addCode(controlFlows.forLoop(
                        code("int $N = 1; $N <= $N.getColumnCount(); $N++",
                                names.indexVariable(),
                                names.indexVariable(),
                                names.resultSetMetaData(),
                                names.indexVariable()),
                        CodeBlock.builder()
                                .addStatement("$N.put($N.getColumnName($N), $N.getObject($N))",
                                        names.row(),
                                        names.resultSetMetaData(),
                                        names.indexVariable(),
                                        names.resultSet(),
                                        names.indexVariable())
                                .build()))
                .addStatement("return $N", names.row())
                .build();
    }

}
