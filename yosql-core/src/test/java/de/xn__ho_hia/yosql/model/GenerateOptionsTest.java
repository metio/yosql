package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

class GenerateOptionsTest implements EnumTCK<GenerateOptions> {

    @Override
    public Class<GenerateOptions> getEnumClass() {
        return GenerateOptions.class;
    }

    @Override
    @SuppressWarnings("nls")
    public Stream<String> validValues() {
        return Stream.of(
                "INPUT_BASE_DIRECTORY",
                "OUTPUT_BASE_DIRECTORY");
    }

}
