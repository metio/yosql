package wtf.metio.yosql.model.descriptions;

import wtf.metio.yosql.testutils.EnumTCK;

import java.util.stream.Stream;

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
