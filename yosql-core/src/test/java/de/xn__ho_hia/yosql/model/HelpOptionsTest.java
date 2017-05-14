package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

@SuppressWarnings("nls")
final class HelpOptionsTest implements EnumTCK<HelpOptions> {

    @Override
    public Class<HelpOptions> getEnumClass() {
        return HelpOptions.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("COMMAND");
    }

}
