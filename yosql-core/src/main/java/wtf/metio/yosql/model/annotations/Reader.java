package wtf.metio.yosql.model.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
   * Marker annotation for loggers intended for file reader.
   */
  @Qualifier
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ TYPE, PARAMETER, METHOD })
  public @interface Reader {
      // marker annotation
  }
