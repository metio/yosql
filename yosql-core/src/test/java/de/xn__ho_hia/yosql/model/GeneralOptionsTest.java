package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

@SuppressWarnings("nls")
final class GeneralOptionsTest implements EnumTCK<GeneralOptions> {

    @Override
    public Class<GeneralOptions> getEnumClass() {
        return GeneralOptions.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("LOCALE");
    }

}
