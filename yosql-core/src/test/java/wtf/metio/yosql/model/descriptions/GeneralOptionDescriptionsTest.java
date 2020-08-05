package wtf.metio.yosql.model.descriptions;

import java.util.stream.Stream;

import wtf.metio.yosql.testutils.EnumTCK;

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
