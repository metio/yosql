/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Named;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import javax.inject.Inject;

/**
 * Configures a single ResultRowConverter.
 */
public abstract class UserResultRowConverter implements Named {

    /**
     * @return The fully-qualified name of the converter class.
     */
    @Input
    public abstract Property<String> getConverterType();

    /**
     * @return The name of the method to call.
     */
    @Input
    public abstract Property<String> getMethodName();

    /**
     * @return The fully-qualified name of the result class.
     */
    @Input
    public abstract Property<String> getResultType();

    /**
     * Provided for Gradle for unknown reasons.
     */
    @Inject
    public UserResultRowConverter() {
    }

    ResultRowConverter asResultRowConverter() {
        return ResultRowConverter.builder()
                .setAlias(getName())
                .setConverterType(getConverterType().get())
                .setMethodName(getMethodName().get())
                .setResultType(getResultType().get())
                .build();
    }

}
