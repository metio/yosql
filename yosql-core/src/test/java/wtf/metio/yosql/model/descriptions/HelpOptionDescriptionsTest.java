package wtf.metio.yosql.model.descriptions;

import wtf.metio.yosql.testutils.EnumTCK;

import java.util.stream.Stream;

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
