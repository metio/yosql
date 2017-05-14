package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

@SuppressWarnings("nls")
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
