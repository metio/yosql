/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import com.squareup.javapoet.ClassName;
import wtf.metio.yosql.model.options.*;

import javax.lang.model.element.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

import static wtf.metio.yosql.model.options.AnnotationMemberOptions.WITHOUT_DATE;

// TODO: rename to ConfigurationObjectMother?
public final class ModelConfigurationObjectMother {

    public static AnnotationConfiguration annotationConfig() {
        return annotationConfig(AnnotationClassOptions.PROCESSING_API);
    }

    public static AnnotationConfiguration annotationConfig(final AnnotationClassOptions classOptions) {
        return annotationConfig(classOptions, WITHOUT_DATE);
    }

    public static AnnotationConfiguration annotationConfig(
            final AnnotationClassOptions classOptions,
            final AnnotationMemberOptions memberOptions) {
        return AnnotationConfiguration.builder()
                .setClassAnnotation(classOptions)
                .setFieldAnnotation(classOptions)
                .setMethodAnnotation(classOptions)
                .setClassComment("class")
                .setFieldComment("field")
                .setMethodComment("method")
                .setGeneratorName("test")
                .setClassMembers(memberOptions)
                .setFieldMembers(memberOptions)
                .setMethodMembers(memberOptions)
                .build();
    }

    public static VariableConfiguration variableConfiguration() {
        return variableConfiguration(VariableTypeOptions.TYPE);
    }

    public static VariableConfiguration variableConfiguration(final VariableTypeOptions options) {
        return VariableConfiguration.builder()
                .addModifiers(Modifier.FINAL)
                .setVariableType(options)
                .build();
    }

    public static FileConfiguration fileConfiguration() {
        return FileConfiguration.builder()
                .setInputBaseDirectory(Paths.get("."))
                .setOutputBaseDirectory(Paths.get("."))
                .setSqlFilesCharset(StandardCharsets.UTF_8)
                .setSqlFilesSuffix(".sql")
                .setSqlStatementSeparator(";")
                .build();
    }

    public static JavaConfiguration javaConfiguration() {
        return JavaConfiguration.builder()
                .setTargetVersion(15)
                .setUseGenerics(true)
                .setUseDiamondOperator(true)
                .setUseStreamAPI(true)
                .setUseProcessingApi(true)
                .setUseVar(true)
                .setUseTextBlocks(true)
                .setUseRecords(true)
                .setUseFinal(true)
                .build();
    }

    public static LoggingConfiguration loggingConfiguration() {
        return LoggingConfiguration.builder()
                .setApi(LoggingApiOptions.JDK)
                .build();
    }
    
    public static MethodConfiguration methodConfiguration() {
        return MethodConfiguration.builder()
                .addAllowedCallPrefixes("call")
                .addAllowedReadPrefixes("read")
                .addAllowedWritePrefixes("write")
                .setGenerateBatchApi(true)
                .setGenerateStandardApi(true)
                .setGenerateRxJavaApi(true)
                .setGenerateStreamEagerApi(true)
                .setGenerateStreamLazyApi(true)
                .setMethodBatchPrefix("")
                .setMethodBatchSuffix("batch")
                .setMethodCatchAndRethrow(true)
                .setMethodEagerName("eager")
                .setMethodLazyName("lazy")
                .setMethodRxJavaPrefix("")
                .setMethodRxJavaSuffix("rx")
                .setMethodStreamPrefix("")
                .setMethodStreamSuffix("stream")
                .setValidateMethodNamePrefixes(true)
                .build();
    }
    
    public static NameConfiguration nameConfiguration() {
        return NameConfiguration.builder()
                .setBasePackageName("com.example")
                .setConverterPackageName("converter")
                .setUtilityPackageName("utilities")
                .build();
    }
    
    public static ResourceConfiguration resourceConfiguration() {
        return ResourceConfiguration.builder()
                .setMaxThreads(2)
                .build();
    }
    
    public static RepositoryConfiguration repositoryConfiguration() {
        return RepositoryConfiguration.builder()
                .setRepositoryGenerateInterface(true)
                .setRepositoryNameSuffix("repository")
                .build();
    }
    
    public static StatementConfiguration statementConfiguration() {
        return StatementConfiguration.builder()
                .setEmbed(StatementInRepositoryOptions.INLINE_CONCAT)
                .build();
    }

    public static JdbcNamesConfiguration jdbcNamesConfiguration() {
        return JdbcNamesConfiguration.builder()
                .setBatch("batch")
                .setColumnCount("columnCount")
                .setColumnLabel("columnLabel")
                .setConnection("connection")
                .setDataSource("dataSource")
                .setIndex("index")
                .setJdbcIndex("jdbcIndex")
                .setList("list")
                .setMetaData("metaData")
                .setResultSet("resultSet")
                .setRow("row")
                .setStatement("statement")
                .build();
    }
    
    public static RxJavaConfiguration rxJavaConfiguration() {
        return RxJavaConfiguration.builder()
                .setFlowStateClass(ClassName.bestGuess("com.example.state.FlowState"))
                .build();
    }
    
    public static ResultConfiguration resultConfiguration() {
        return ResultConfiguration.builder()
                .setResultRowClass(ClassName.bestGuess("com.example.state.ResultRow"))
                .setResultStateClass(ClassName.bestGuess("com.example.state.ResultState"))
                .build();
    }

    public static RuntimeConfiguration runtimeConfiguration() {
        return RuntimeConfiguration.builder()
                .setAnnotations(annotationConfig())
                .setFiles(fileConfiguration())
                .setJava(javaConfiguration())
                .setJdbcNames(jdbcNamesConfiguration())
                .setLogging(loggingConfiguration())
                .setMethods(methodConfiguration())
                .setNames(nameConfiguration())
                .setRepositories(repositoryConfiguration())
                .setResources(resourceConfiguration())
                .setResult(resultConfiguration())
                .setRxJava(rxJavaConfiguration())
                .setStatements(statementConfiguration())
                .setVariables(variableConfiguration())
                .build();
    }

    private ModelConfigurationObjectMother() {
        // factory class
    }

}
