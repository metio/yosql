package wtf.metio.yosql.model.options;

import wtf.metio.yosql.testutils.EnumTCK;

import java.util.stream.Stream;

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
