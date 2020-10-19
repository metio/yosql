/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.cli;

import picocli.CommandLine;
import wtf.metio.yosql.model.configuration.MethodConfiguration;

import java.util.List;

/**
 * Configures how methods are generated.
 */
public class Methods {

    @CommandLine.Option(
            names = "--methods-generate-standard",
            description = "Controls whether the generated repositories should contain standard methods.",
            defaultValue = "true")
    boolean standardApi;

    @CommandLine.Option(
            names = "--methods-generate-batch",
            description = "Controls whether the generated repositories should contain batch methods for SQL INSERT/UPDATE/DELETE statements.",
            defaultValue = "true")
    boolean batchApi;

    @CommandLine.Option(
            names = "--methods-batch-prefix",
            description = "The method name prefix to apply to all batch methods.",
            defaultValue = "")
    String batchPrefix;

    @CommandLine.Option(
            names = "--methods-batch-suffix",
            description = "The method name suffix to apply to all batch methods.",
            defaultValue = "Batch")
    String batchSuffix = "Batch";

    @CommandLine.Option(
            names = "--methods-generate-stream-eager",
            description = "Controls whether eager Stream based methods should be generated",
            defaultValue = "false")
    boolean streamEagerApi;

    @CommandLine.Option(
            names = "--methods-generate-stream-lazy",
            description = "Controls whether lazy Stream based methods should be generated",
            defaultValue = "false")
    boolean streamLazyApi;

    @CommandLine.Option(
            names = "--methods-generate-rxjava",
            description = "Controls whether RxJava based methods should be generated",
            defaultValue = "false")
    boolean rxJavaApi;

    @CommandLine.Option(
            names = "--methods-stream-prefix",
            description = "The method name prefix to apply to all stream methods.",
            defaultValue = "")
    String streamPrefix;

    @CommandLine.Option(
            names = "--methods-stream-suffix",
            description = "The method name suffix to apply to all stream methods.",
            defaultValue = "Stream")
    String streamSuffix;

    @CommandLine.Option(
            names = "--methods-rxjava-prefix",
            description = "The method name prefix to apply to all RxJava methods.",
            defaultValue = "")
    String rxJavaPrefix;

    @CommandLine.Option(
            names = "--methods-rxjava-suffix",
            description = "The method name suffix to apply to all RxJava methods.",
            defaultValue = "Flow")
    String rxJavaSuffix;

    @CommandLine.Option(
            names = "--methods-lazy-name",
            description = "The method name extra to apply to all lazy stream methods.",
            defaultValue = "Lazy")
    String lazyName;

    @CommandLine.Option(
            names = "--methods-eager-name",
            description = "The method name extra to apply to all eager stream methods.",
            defaultValue = "Eager")
    String eagerName = "Eager";

    @CommandLine.Option(
            names = "--methods-catch-and-rethrow",
            description = "Whether generated methods should catch SqlExceptions and rethrow them as RuntimeExceptions.",
            defaultValue = "true")
    boolean catchAndRethrow;

    @CommandLine.Option(
            names = "--methods-validate-names",
            description = "Controls whether method names are validated.",
            defaultValue = "true")
    boolean validateNamePrefixes;

    @CommandLine.Option(
            names = "--methods-allowed-write-prefixes",
            description = "The allow method name prefixes for writing methods",
            defaultValue = "update,insert,delete,create,write,add,remove,merge,drop",
            split = ",")
    List<String> allowedWritePrefixes;

    @CommandLine.Option(
            names = "--methods-allowed-read-prefixes",
            description = "The allow method name prefixes for reading methods",
            defaultValue = "select,read,query.find",
            split = ",")
    List<String> allowedReadPrefixes;

    @CommandLine.Option(
            names = "--methods-allowed-call-prefixes",
            description = "The allow method name prefixes for calling methods",
            defaultValue = "call,execute",
            split = ",")
    List<String> allowedCallPrefixes;

    MethodConfiguration asConfiguration() {
        return MethodConfiguration.builder()
                .setGenerateBatchApi(batchApi)
                .setGenerateStandardApi(standardApi)
                .setGenerateStreamEagerApi(streamEagerApi)
                .setGenerateStreamLazyApi(streamLazyApi)
                .setGenerateRxJavaApi(rxJavaApi)
                .setMethodBatchPrefix(batchPrefix)
                .setMethodBatchSuffix(batchSuffix)
                .setMethodStreamPrefix(streamPrefix)
                .setMethodStreamSuffix(streamSuffix)
                .setMethodRxJavaPrefix(rxJavaPrefix)
                .setMethodRxJavaSuffix(rxJavaSuffix)
                .setMethodEagerName(eagerName)
                .setMethodLazyName(lazyName)
                .setMethodCatchAndRethrow(catchAndRethrow)
                .setAllowedReadPrefixes(allowedReadPrefixes)
                .setAllowedWritePrefixes(allowedWritePrefixes)
                .setAllowedCallPrefixes(allowedCallPrefixes)
                .setValidateMethodNamePrefixes(validateNamePrefixes)
                .build();
    }

}
