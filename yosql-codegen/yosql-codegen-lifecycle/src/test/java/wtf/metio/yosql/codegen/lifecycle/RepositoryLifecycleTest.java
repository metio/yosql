/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.lifecycle;

import wtf.metio.yosql.internals.utils.test.PropertiesTCK;

final class RepositoryLifecycleTest implements PropertiesTCK<RepositoryLifecycle> {

    @Override
    public Class<RepositoryLifecycle> getEnumClass() {
        return RepositoryLifecycle.class;
    }

}
