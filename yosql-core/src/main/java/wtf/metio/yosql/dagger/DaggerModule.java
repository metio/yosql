/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dagger;

import dagger.Module;

@Module(includes = {
        DefaultConfigurationModule.class,
        DefaultLocaleModule.class,
        ErrorModule.class,
        LoggerModule.class,
})
// TODO: remove after moving other modules to OrchestrationModule
public class DaggerModule {

    // module aggregate

}
