package wtf.metio.yosql.model;

import java.util.stream.Stream;

import wtf.metio.yosql.testutils.EnumTCK;

final class HelpOptionDescriptionsTest implements EnumTCK<HelpOptionDescriptions> {

    @Override
    public Class<HelpOptionDescriptions> getEnumClass() {
        return HelpOptionDescriptions.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("COMMAND_DESCRIPTION");
    }

}
