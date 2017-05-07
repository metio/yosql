package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.model.GenerateOptions;
import de.xn__ho_hia.yosql.testutils.EnumTCK;

class ConfigurationOptionsTest implements EnumTCK<GenerateOptions> {

    @Override
    public Class<GenerateOptions> getEnumClass() {
        return GenerateOptions.class;
    }

    @Override
    @SuppressWarnings("nls")
    public Stream<String> validValues() {
        return Stream.of(
                "INPUT_BASE_DIRECTORY",
                "INPUT_BASE_DIRECTORY_DESCRIPTION",
                "OUTPUT_BASE_DIRECTORY",
                "OUTPUT_BASE_DIRECTORY_DESCRIPTION",
                "CURRENT_DIRECTORY");
    }

}
