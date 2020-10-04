/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import com.squareup.javapoet.ClassName;
import wtf.metio.yosql.model.configuration.RxJavaConfiguration;

/**
 * Configures how RxJava is used.
 */
public class RxJava {

    /**
     * The simple name of the generated flow state class (default: <strong>FlowState</strong>).
     */
    private final String flowState = "FlowState";

    /**
     * The groupId to match for automatic RxJava detection (default: <strong>"io.reactivex.rxjava2"</strong>).
     */
    private final String groupId = "io.reactivex.rxjava2";

    /**
     * The artifactId to match for automatic RxJava detection (default: <strong>"rxjava"</strong>).
     */
    private final String artifactId = "rxjava";

    RxJavaConfiguration asConfiguration(final String utilPackage) {
        return RxJavaConfiguration.builder()
                .setFlowStateClass(ClassName.get(utilPackage, flowState))
                .build();
    }

}
