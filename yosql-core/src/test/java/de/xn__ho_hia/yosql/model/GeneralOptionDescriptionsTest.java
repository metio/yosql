package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

@SuppressWarnings("nls")
final class GeneralOptionDescriptionsTest implements EnumTCK<GeneralOptionDescriptions> {

    @Override
    public Class<GeneralOptionDescriptions> getEnumClass() {
        return GeneralOptionDescriptions.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("LOCALE_DESCRIPTION");
    }

}
