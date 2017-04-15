package de.xn__ho_hia.yosql.generator.logging.jdk;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Marks a type as related to the JDK.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, PARAMETER, METHOD })
public @interface JDK {

    // marker annotation

}
