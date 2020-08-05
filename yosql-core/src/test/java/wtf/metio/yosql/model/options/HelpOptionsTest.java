package wtf.metio.yosql.model.options;

import java.util.stream.Stream;

import wtf.metio.yosql.testutils.EnumTCK;

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
