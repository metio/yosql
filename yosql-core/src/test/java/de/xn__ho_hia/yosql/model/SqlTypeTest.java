package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

final class SqlTypeTest implements EnumTCK<SqlType> {

    @Override
    public Class<SqlType> getEnumClass() {
        return SqlType.class;
    }

    @Override
    @SuppressWarnings("nls")
    public Stream<String> validValues() {
        return Stream.of("READING", "WRITING", "CALLING");
    }

}
