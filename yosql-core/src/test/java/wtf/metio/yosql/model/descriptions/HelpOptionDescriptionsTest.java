package wtf.metio.yosql.model.descriptions;

import org.junit.jupiter.api.DisplayName;
import wtf.metio.yosql.testutils.EnumTCK;
import wtf.metio.yosql.testutils.PropertiesTCK;

import java.util.stream.Stream;

@DisplayName("HelpOptionDescriptions")
final class HelpOptionDescriptionsTest implements EnumTCK<HelpOptionDescriptions>, PropertiesTCK<HelpOptionDescriptions> {

    @Override
    public Class<HelpOptionDescriptions> getEnumClass() {
        return HelpOptionDescriptions.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("COMMAND_DESCRIPTION");
    }

}
