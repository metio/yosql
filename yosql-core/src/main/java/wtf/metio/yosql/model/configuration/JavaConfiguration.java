/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

@Value.Immutable
public interface JavaConfiguration {

    static JavaConfiguration.Builder builder() {
        return new JavaConfiguration.Builder();
    }

    int targetVersion();

    boolean useVar();

    boolean useRecords();

    class Builder extends ImmutableJavaConfiguration.Builder {
    }

}
