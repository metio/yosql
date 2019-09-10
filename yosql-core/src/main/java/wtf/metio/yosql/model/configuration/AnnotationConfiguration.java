package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;
import wtf.metio.yosql.model.options.AnnotationClassOptions;
import wtf.metio.yosql.model.options.AnnotationMemberOptions;

@AutoValue
public abstract class AnnotationConfiguration {

    public static AnnotationConfiguration.Builder builder() {
        return new AutoValue_AnnotationConfiguration.Builder();
    }

    public abstract AnnotationClassOptions classAnnotation();
    public abstract AnnotationClassOptions fieldAnnotation();
    public abstract AnnotationClassOptions methodAnnotation();

    public abstract AnnotationMemberOptions classMembers();
    public abstract AnnotationMemberOptions fieldMembers();
    public abstract AnnotationMemberOptions methodMembers();

    public abstract String classComment();
    public abstract String fieldComment();
    public abstract String methodComment();

    public abstract String generatorName();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setClassAnnotation(AnnotationClassOptions classAnnotation);
        public abstract Builder setFieldAnnotation(AnnotationClassOptions fieldAnnotation);
        public abstract Builder setMethodAnnotation(AnnotationClassOptions methodAnnotation);

        public abstract Builder setClassMembers(AnnotationMemberOptions classMembers);
        public abstract Builder setFieldMembers(AnnotationMemberOptions fieldMembers);
        public abstract Builder setMethodMembers(AnnotationMemberOptions methodMembers);

        public abstract Builder setClassComment(String classComment);
        public abstract Builder setFieldComment(String fieldComment);
        public abstract Builder setMethodComment(String methodComment);

        public abstract Builder setGeneratorName(String generatorName);

        public abstract AnnotationConfiguration build();

    }

}
