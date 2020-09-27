package wtf.metio.yosql.model.options;

import org.junit.jupiter.api.DisplayName;
import wtf.metio.yosql.testutils.EnumTCK;
import wtf.metio.yosql.testutils.PropertiesTCK;

import java.util.stream.Stream;

@DisplayName("GeneralOptions")
final class GeneralOptionsTest implements EnumTCK<GeneralOptions>, PropertiesTCK<GeneralOptions> {

    @Override
    public Class<GeneralOptions> getEnumClass() {
        return GeneralOptions.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("LOCALE");
    }

}
