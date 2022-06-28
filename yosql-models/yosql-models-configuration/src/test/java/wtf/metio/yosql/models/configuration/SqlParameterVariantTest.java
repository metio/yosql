/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.configuration;

import wtf.metio.yosql.internals.junit5.EnumTCK;

import java.util.stream.Stream;

class SqlParameterVariantTest implements EnumTCK<SqlParameterVariant> {

    @Override
    public Class<SqlParameterVariant> getEnumClass() {
        return SqlParameterVariant.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("IN", "OUT", "INOUT");
    }

    @Override
    public Stream<String> invalidValues() {
        return Stream.of("IN_OUT", "INPUT", "OUTPUT");
    }

}
