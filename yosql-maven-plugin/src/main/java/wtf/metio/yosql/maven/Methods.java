/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.MethodConfiguration;

import java.util.List;
import java.util.stream.Stream;

/**
 * Configures how methods are generated.
 */
public class Methods {

    /**
     * Controls whether the generated repositories should contain <em>generic</em> methods that. Standard methods
     * execute depending on the type of the query and could either be a single 'executeQuery' on a PreparedStatement in
     * case of SQL SELECT statements or a single call to 'executeUpdate' for SQL UPDATE statements. (default:
     * <strong>true</strong>).
     */
    private final boolean standardApi = true;

    /**
     * Controls whether the generated repositories should contain batch methods for SQL INSERT/UPDATE/DELETE statements.
     * (default: <strong>true</strong>).
     */
    private final boolean batchApi = true;

    /**
     * The method name prefix to apply to all batch methods (default: <strong>""</strong>).
     */
    private final String batchPrefix = "";

    /**
     * The method name suffix to apply to all batch methods (default: <strong>"Batch"</strong>).
     */
    private final String batchSuffix = "Batch";

    /**
     * Controls whether an eager {@link Stream} based method should be generated (default: <strong>true</strong>). If
     * the target Java version is set to anything below 1.8, defaults to <strong>false</strong>
     */
    private final boolean streamEagerApi = true;

    /**
     * Controls whether a lazy {@link Stream} based method should be generated (default: <strong>true</strong>). If the
     * target Java version is set to anything below 1.8, defaults to <strong>false</strong>
     */
    private final boolean streamLazyApi = true;

    /**
     * Controls whether a RxJava {@link io.reactivex.Flowable} based method should be generated (default:
     * <strong>false</strong>). In case <strong>io.reactivex.rxjava2:rxjava</strong> is a declared dependency, defaults
     * to <strong>true</strong>.
     */
    private final boolean rxJavaApi = false;

    /**
     * The method name prefix to apply to all stream methods (default: <strong>""</strong>).
     */
    private final String streamPrefix = "";

    /**
     * The method name suffix to apply to all stream methods (default: <strong>Stream</strong>).
     */
    private final String streamSuffix = "Stream";

    /**
     * The method name prefix to apply to all RxJava methods (default: <strong>""</strong>).
     */
    private final String rxJavaPrefix = "";

    /**
     * The method name suffix to apply to all RxJava methods (default: <strong>Flow</strong>).
     */
    private final String rxJavaSuffix = "Flow";

    /**
     * The method name extra to apply to all lazy stream methods (default: <strong>Lazy</strong>).
     */
    private final String lazyName = "Lazy";

    /**
     * The method name extra to apply to all eager stream methods (default: <strong>Eager</strong>).
     */
    private final String eagerName = "Eager";

    /**
     * Whether generated methods should catch SqlExceptions and rethrow them as RuntimeExceptions (default:
     * <strong>true</strong>). If set to <strong>false</strong>, this will cause methods to declare that they throw a
     * checked exception which in turn will force all its users to handle the exception.
     */
    private final boolean catchAndRethrow = true;

    /**
     * Controls whether method names are validated according to <strong>allowedReadPrefixes</strong> and
     * <strong>allowedWritePrefixes</strong> (default: <strong>true</strong>).
     */
    private final boolean validateNamePrefixes = true;

    /**
     * The allow method name prefixes for writing methods (default: <strong>update, insert, delete, create, write, add,
     * remove, merge, drop</strong>).
     */
    private final List<String> allowedWritePrefixes = List.of("update", "insert", "delete", "create", "write", "add", "remove", "merge", "drop");

    /**
     * The allow method name prefixes for reading methods (default: <strong>select, read, query, find</strong>).
     */
    private final List<String> allowedReadPrefixes = List.of("select", "read", "query", "find");

    /**
     * The allow method name prefixes for calling methods (default: <strong>call, execute</strong>).
     */
    private final List<String> allowedCallPrefixes = List.of("call", "execute");

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
