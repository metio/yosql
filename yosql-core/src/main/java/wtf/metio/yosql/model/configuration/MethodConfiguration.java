package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class MethodConfiguration {

    public static MethodConfiguration.Builder builder() {
        return new AutoValue_MethodConfiguration.Builder();
    }

    public abstract boolean generateStandardApi();

    public abstract boolean generateBatchApi();

    public abstract boolean generateRxJavaApi();

    public abstract boolean generateStreamEagerApi();

    public abstract boolean generateStreamLazyApi();

    public abstract String methodBatchPrefix();

    public abstract String methodBatchSuffix();

    public abstract String methodStreamPrefix();

    public abstract String methodStreamSuffix();

    public abstract String methodRxJavaPrefix();

    public abstract String methodRxJavaSuffix();

    public abstract String methodLazyName();

    public abstract String methodEagerName();

    public abstract boolean methodCatchAndRethrow();

    public abstract List<String> allowedWritePrefixes();

    public abstract List<String> allowedReadPrefixes();

    public abstract List<String> allowedCallPrefixes();

    public abstract boolean validateMethodNamePrefixes();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setGenerateStandardApi(boolean generateStandardApi);

        public abstract Builder setGenerateBatchApi(boolean generateBatchApi);

        public abstract Builder setGenerateRxJavaApi(boolean generateRxJavaApi);

        public abstract Builder setGenerateStreamEagerApi(boolean generateStreamEagerApi);

        public abstract Builder setGenerateStreamLazyApi(boolean generateStreamLazyApi);

        public abstract Builder setMethodBatchPrefix(String value);

        public abstract Builder setMethodBatchSuffix(String value);

        public abstract Builder setMethodStreamPrefix(String value);

        public abstract Builder setMethodStreamSuffix(String value);

        public abstract Builder setMethodRxJavaPrefix(String value);

        public abstract Builder setMethodRxJavaSuffix(String value);

        public abstract Builder setMethodLazyName(String value);

        public abstract Builder setMethodEagerName(String value);

        public abstract Builder setAllowedWritePrefixes(List<String> value);

        public abstract Builder setAllowedReadPrefixes(List<String> value);

        public abstract Builder setAllowedCallPrefixes(List<String> value);

        public abstract Builder setValidateMethodNamePrefixes(boolean validateMethodNamePrefixes);

        public abstract Builder setMethodCatchAndRethrow(boolean value);

        public abstract MethodConfiguration build();

    }

}
