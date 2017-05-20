/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import static de.xn__ho_hia.yosql.generator.helpers.TypicalParameters.connection;
import static de.xn__ho_hia.yosql.generator.helpers.TypicalParameters.dataSource;

import org.junit.jupiter.api.Test;

import de.xn__ho_hia.yosql.testutils.ValidationFile;
import de.xn__ho_hia.yosql.testutils.ValidationFileTest;

@SuppressWarnings("static-method")
final class TypicalParametersTest extends ValidationFileTest {

    @Test
    public void shouldSpecifyDataSource(final ValidationFile validationFile) {
        validate(dataSource(), validationFile);
    }

    @Test
    public void shouldSpecifyConnection(final ValidationFile validationFile) {
        validate(connection(), validationFile);
    }

}
