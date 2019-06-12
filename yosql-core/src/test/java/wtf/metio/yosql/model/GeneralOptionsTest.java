package wtf.metio.yosql.model;

import java.util.stream.Stream;

import wtf.metio.yosql.testutils.EnumTCK;

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
