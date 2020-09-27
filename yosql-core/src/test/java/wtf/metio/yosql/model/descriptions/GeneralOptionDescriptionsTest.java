package wtf.metio.yosql.model.descriptions;

import org.junit.jupiter.api.DisplayName;
import wtf.metio.yosql.testutils.EnumTCK;
import wtf.metio.yosql.testutils.PropertiesTCK;

import java.util.stream.Stream;

@DisplayName("GeneralOptionDescriptions")
final class GeneralOptionDescriptionsTest implements EnumTCK<GeneralOptionDescriptions>, PropertiesTCK<GeneralOptionDescriptions> {

    @Override
    public Class<GeneralOptionDescriptions> getEnumClass() {
        return GeneralOptionDescriptions.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("LOCALE_DESCRIPTION");
    }

}
