/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.javapoet.ClassName;

@AutoValue
@SuppressWarnings("javadoc")
public abstract class ExecutionConfiguration {

    public static final Builder builder() {
        return new AutoValue_ExecutionConfiguration.Builder();
    }

    /**
     * @return Whether log statements should be generated
     */
    public final boolean shouldLog() {
        return LoggingAPI.NONE != loggingApi();
    }

    /**
     * @return The name of the flow-state class
     */
    public ClassName getFlowStateClass() {
        return Optional.ofNullable(flowStateClass())
                .orElse(ClassName.get(basePackageName() + "." + utilityPackageName(), //$NON-NLS-1$
                        defaulFlowStateClassName()));
    }

    /**
     * @return The name of the result-state class
     */
    public ClassName getResultStateClass() {
        return Optional.ofNullable(resultStateClass())
                .orElse(ClassName.get(basePackageName() + "." + utilityPackageName(), //$NON-NLS-1$
                        defaultResultStateClassName()));
    }

    /**
     * @return The name of the result-row class
     */
    public ClassName getResultRowClass() {
        return Optional.ofNullable(resultRowClass())
                .orElse(ClassName.get(basePackageName() + "." + utilityPackageName(), //$NON-NLS-1$
                        defaultResultRowClassName()));
    }

    abstract String defaulFlowStateClassName();

    abstract String defaultResultStateClassName();

    abstract String defaultResultRowClassName();

    public abstract String repositorySqlStatements();

    public abstract Path inputBaseDirectory();

    public abstract Path outputBaseDirectory();

    public abstract boolean generateStandardApi();

    public abstract boolean generateBatchApi();

    public abstract boolean generateRxJavaApi();

    public abstract String methodBatchPrefix();

    public abstract String methodBatchSuffix();

    public abstract boolean generateStreamEagerApi();

    public abstract boolean generateStreamLazyApi();

    public abstract String methodStreamPrefix();

    public abstract String methodStreamSuffix();

    public abstract String methodRxJavaPrefix();

    public abstract String methodRxJavaSuffix();

    public abstract String methodLazyName();

    public abstract String methodEagerName();

    public abstract String repositoryNameSuffix();

    public abstract String utilityPackageName();

    public abstract String converterPackageName();

    public abstract String basePackageName();

    public abstract String sqlStatementSeparator();

    public abstract String sqlFilesCharset();

    public abstract List<String> allowedWritePrefixes();

    public abstract List<String> allowedReadPrefixes();

    public abstract List<String> allowedCallPrefixes();

    public abstract boolean validateMethodNamePrefixes();

    public abstract boolean methodCatchAndRethrow();

    public abstract boolean classGeneratedAnnotation();

    public abstract boolean fieldGeneratedAnnotation();

    public abstract boolean methodGeneratedAnnotation();

    public abstract boolean repositoryGenerateInterface();

    public abstract String generatedAnnotationComment();

    public abstract LoggingAPI loggingApi();

    @Nullable
    public abstract ClassName flowStateClass();

    @Nullable
    public abstract ClassName resultStateClass();

    @Nullable
    public abstract ClassName resultRowClass();

    public abstract String defaultRowConverter();

    public abstract List<ResultRowConverter> resultRowConverters();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setDefaulFlowStateClassName(String value);

        public abstract Builder setDefaultResultStateClassName(String value);

        public abstract Builder setDefaultResultRowClassName(String value);

        public abstract Builder setDefaultRowConverter(String value);

        public abstract Builder setResultRowConverters(List<ResultRowConverter> value);

        public abstract Builder setFlowStateClass(ClassName value);

        public abstract Builder setResultStateClass(ClassName value);

        public abstract Builder setResultRowClass(ClassName value);

        public abstract Builder setRepositorySqlStatements(String value);

        public abstract Builder setInputBaseDirectory(Path value);

        public abstract Builder setOutputBaseDirectory(Path value);

        public abstract Builder setRepositoryGenerateInterface(boolean value);

        public abstract Builder setGeneratedAnnotationComment(String value);

        public abstract Builder setGenerateStandardApi(boolean value);

        public abstract Builder setGenerateBatchApi(boolean value);

        public abstract Builder setGenerateRxJavaApi(boolean value);

        public abstract Builder setGenerateStreamEagerApi(boolean value);

        public abstract Builder setGenerateStreamLazyApi(boolean value);

        public abstract Builder setMethodBatchPrefix(String value);

        public abstract Builder setMethodBatchSuffix(String value);

        public abstract Builder setMethodStreamPrefix(String value);

        public abstract Builder setMethodStreamSuffix(String value);

        public abstract Builder setMethodRxJavaPrefix(String value);

        public abstract Builder setMethodRxJavaSuffix(String value);

        public abstract Builder setMethodLazyName(String value);

        public abstract Builder setMethodEagerName(String value);

        public abstract Builder setRepositoryNameSuffix(String value);

        public abstract Builder setUtilityPackageName(String value);

        public abstract Builder setConverterPackageName(String value);

        public abstract Builder setBasePackageName(String value);

        public abstract Builder setSqlStatementSeparator(String value);

        public abstract Builder setSqlFilesCharset(String value);

        public abstract Builder setAllowedWritePrefixes(List<String> value);

        public abstract Builder setAllowedReadPrefixes(List<String> value);

        public abstract Builder setAllowedCallPrefixes(List<String> value);

        public abstract Builder setValidateMethodNamePrefixes(boolean value);

        public abstract Builder setMethodCatchAndRethrow(boolean value);

        public abstract Builder setClassGeneratedAnnotation(boolean value);

        public abstract Builder setFieldGeneratedAnnotation(boolean value);

        public abstract Builder setMethodGeneratedAnnotation(boolean value);

        public abstract Builder setLoggingApi(LoggingAPI value);

        public abstract ExecutionConfiguration build();
    }

}
