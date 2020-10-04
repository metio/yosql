/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface MethodConfiguration {

    static ImmutableMethodConfiguration.Builder builder() {
        return ImmutableMethodConfiguration.builder();
    }

    boolean generateStandardApi();

    boolean generateBatchApi();

    boolean generateRxJavaApi();

    boolean generateStreamEagerApi();

    boolean generateStreamLazyApi();

    String methodBatchPrefix();

    String methodBatchSuffix();

    String methodStreamPrefix();

    String methodStreamSuffix();

    String methodRxJavaPrefix();

    String methodRxJavaSuffix();

    String methodLazyName();

    String methodEagerName();

    boolean methodCatchAndRethrow();

    List<String> allowedWritePrefixes();

    List<String> allowedReadPrefixes();

    List<String> allowedCallPrefixes();

    boolean validateMethodNamePrefixes();

}
