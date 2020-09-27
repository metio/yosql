package wtf.metio.yosql.model.options;

import org.junit.jupiter.api.DisplayName;
import wtf.metio.yosql.testutils.EnumTCK;
import wtf.metio.yosql.testutils.PropertiesTCK;

import java.util.stream.Stream;

@DisplayName("HelpOptions")
final class HelpOptionsTest implements EnumTCK<HelpOptions>, PropertiesTCK<HelpOptions> {

    @Override
    public Class<HelpOptions> getEnumClass() {
        return HelpOptions.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("COMMAND");
    }

}
