/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
