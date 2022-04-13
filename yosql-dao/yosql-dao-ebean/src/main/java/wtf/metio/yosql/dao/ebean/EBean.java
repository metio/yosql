/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.ebean;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Marks a type as related to the EBean API.
 *
 * @see <a href="https://ebean.io/">EBean Website</a>
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, PARAMETER, METHOD})
public @interface EBean {

    // marker annotation

}
