/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

@Value.Immutable
public interface RuntimeConfiguration {

    static RuntimeConfiguration.Builder builder() {
        return new RuntimeConfiguration.Builder();
    }

    FileConfiguration files();

    JavaConfiguration java();

    LoggingConfiguration logging();

    MethodConfiguration methods();

    PackagesConfiguration names();

    ResourceConfiguration resources();

    RepositoryConfiguration repositories();

    StatementConfiguration statements();

    VariableConfiguration variables();

    JdbcNamesConfiguration jdbcNames();

    RxJavaConfiguration rxJava();

    ResultConfiguration result();

    AnnotationConfiguration annotations();

    class Builder extends ImmutableRuntimeConfiguration.Builder {
    }

}
