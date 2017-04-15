package de.xn__ho_hia.yosql.generator.logging.noop;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Marks a type as related to a no-op implementation.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, PARAMETER, METHOD })
public @interface NoOp {

    // marker annotation

}
