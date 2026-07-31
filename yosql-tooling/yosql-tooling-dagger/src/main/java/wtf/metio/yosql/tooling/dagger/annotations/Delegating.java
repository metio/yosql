/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.tooling.dagger.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Marks types as a delegate.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, PARAMETER, METHOD})
public @interface Delegating {

    // marker annotation

}
