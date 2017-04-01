package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

class LoggingAPITest implements EnumTCK<LoggingAPI> {

    @Override
    public Class<LoggingAPI> getEnumClass() {
        return LoggingAPI.class;
    }

    @Override
    @SuppressWarnings("nls")
    public Stream<String> validValues() {
        return Stream.of("NONE", "JDK", "LOG4J", "SLF4J", "AUTO");
    }

}
