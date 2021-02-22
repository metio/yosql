/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.constants.api;

import wtf.metio.yosql.internals.junit5.EnumTCK;

import java.util.stream.Stream;

final class PersistenceApisTest implements EnumTCK<PersistenceApis> {

    @Override
    public Class<PersistenceApis> getEnumClass() {
        return PersistenceApis.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of(
                "AUTO",
                "EBEAN",
                "FLUENT_JDBC",
                "JDBC",
                "JDBI",
                "JOOQ",
                "JPA",
                "MYBATIS",
                "PYRANID",
                "R2DBC",
                "SANS_ORM",
                "SPRING_DATA_JDBC",
                "SPRING_DATA_JPA",
                "SPRING_DATA_R2DBC",
                "SPRING_JDBC"
        );
    }

}
