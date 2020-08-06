package wtf.metio.yosql.model.options;

import wtf.metio.yosql.testutils.EnumTCK;

import java.util.stream.Stream;

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
