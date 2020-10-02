/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

@Value.Immutable
public interface ResourceConfiguration {

    static ResourceConfiguration.Builder builder() {
        return new ResourceConfiguration.Builder();
    }

    /**
     * @return The maximum number of threads to use while working.
     */
    int maxThreads();

    class Builder extends ImmutableResourceConfiguration.Builder {
    }

}
