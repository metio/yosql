/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.testutils;

import java.nio.file.Paths;
import java.util.Arrays;

import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.LoggingAPI;
import de.xn__ho_hia.yosql.model.ResultRowConverter;

/**
 * Test mother for {@link ExecutionConfiguration}s.
 */
public final class TestExecutionConfigurations {

    private TestExecutionConfigurations() {
        // factory class
    }

    /**
     * @return Default test configuration.
     */
    @SuppressWarnings("nls")
    public static ExecutionConfiguration testExecutionConfiguration() {
        final ResultRowConverter converter = new ResultRowConverter();
        converter.setAlias("defaultRowConverter");
        converter.setConverterType("com.example.test.converter.ToResultRowConverter");
        converter.setResultType("ResultState");
        return ExecutionConfiguration.builder()
                .setAllowedCallPrefixes(Arrays.asList("call"))
                .setAllowedReadPrefixes(Arrays.asList("read"))
                .setAllowedWritePrefixes(Arrays.asList("write"))
                .setBasePackageName("com.example.test")
                .setConverterPackageName("converter")
                .setUtilityPackageName("utils")
                .setDefaulFlowStateClassName("FlowState")
                .setDefaultResultRowClassName("ResultRow")
                .setDefaultResultStateClassName("ResultState")
                .setDefaultRowConverter("ToResultRowConverter")
                .setRepositorySqlStatements("inline")
                .setClassGeneratedAnnotation(false)
                .setMethodGeneratedAnnotation(false)
                .setFieldGeneratedAnnotation(false)
                .setInputBaseDirectory(Paths.get("."))
                .setOutputBaseDirectory(Paths.get("."))
                .setGenerateBatchApi(true)
                .setGenerateRxJavaApi(true)
                .setGenerateStandardApi(true)
                .setGenerateStreamEagerApi(true)
                .setGenerateStreamLazyApi(true)
                .setMethodBatchPrefix("batch")
                .setMethodBatchSuffix("")
                .setMethodCatchAndRethrow(true)
                .setMethodEagerName("eager")
                .setMethodLazyName("lazy")
                .setMethodRxJavaPrefix("rxjava")
                .setMethodRxJavaSuffix("")
                .setMethodStreamPrefix("stream")
                .setMethodStreamSuffix("")
                .setValidateMethodNamePrefixes(true)
                .setRepositoryGenerateInterface(false)
                .setLoggingApi(LoggingAPI.JDK)
                .setResultRowConverters(Arrays.asList(converter))
                .setRepositoryNameSuffix("Repository")
                .setSqlStatementSeparator(";")
                .setSqlFilesCharset("UTF-8")
                .setSqlFilesSuffix(".sql")
                .setGeneratedAnnotationComment("just a test")
                .setMaxThreads(5)
                .build();
    }
}
