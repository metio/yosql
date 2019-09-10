package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;
import com.squareup.javapoet.ClassName;

import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class RxJavaConfiguration {

    public static Builder builder() {
        return new AutoValue_RxJavaConfiguration.Builder();
    }

    public abstract ClassName flowStateClass();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setFlowStateClass(ClassName value);

        public abstract RxJavaConfiguration build();

    }

}
