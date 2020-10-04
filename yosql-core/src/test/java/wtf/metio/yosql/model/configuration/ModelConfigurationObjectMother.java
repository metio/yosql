/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import wtf.metio.yosql.tests.ObjectMother;

public final class ModelConfigurationObjectMother extends ObjectMother {

    public static AnnotationConfiguration annotationConfig() {
        return yoSqlComponent().annotationConfiguration();
    }

    public static VariableConfiguration variableConfiguration() {
        return yoSqlComponent().variableConfiguration();
    }

    public static JdbcNamesConfiguration jdbcNamesConfiguration() {
        return yoSqlComponent().jdbcNamesConfiguration();
    }

    public static RuntimeConfiguration runtimeConfiguration() {
        return yoSqlComponent().runtimeConfiguration();
    }

}
