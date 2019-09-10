/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.testutils;

import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.options.LoggingApiOptions;
import wtf.metio.yosql.model.sql.ResultRowConverter;

import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Test mother for {@link RuntimeConfiguration}s.
 */
public final class TestExecutionConfigurations {

    private TestExecutionConfigurations() {
        // factory class
    }

    /**
     * @return Default test configuration.
     */
    public static RuntimeConfiguration testExecutionConfiguration() {
        final ResultRowConverter converter = new ResultRowConverter();
        converter.setAlias("defaultRowConverter");
        converter.setConverterType("com.example.test.converter.ToResultRowConverter");
        converter.setResultType("ResultState");
        return RuntimeConfiguration.builder()
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
                .setLoggingApi(LoggingApiOptions.JDK)
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
