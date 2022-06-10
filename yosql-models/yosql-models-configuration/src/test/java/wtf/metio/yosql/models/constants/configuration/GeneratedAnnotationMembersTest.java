/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.constants.configuration;

import wtf.metio.yosql.internals.junit5.EnumTCK;
import wtf.metio.yosql.models.configuration.GeneratedAnnotationMembers;

import java.util.stream.Stream;

final class GeneratedAnnotationMembersTest implements EnumTCK<GeneratedAnnotationMembers> {

    @Override
    public Class<GeneratedAnnotationMembers> getEnumClass() {
        return GeneratedAnnotationMembers.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("ALL", "NONE", "VALUE", "DATE", "COMMENT", "WITHOUT_DATE");
    }

}
