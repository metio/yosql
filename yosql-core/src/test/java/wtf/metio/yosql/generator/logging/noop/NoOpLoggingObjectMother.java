package wtf.metio.yosql.generator.logging.noop;

import wtf.metio.yosql.generator.api.LoggingGenerator;

public final class NoOpLoggingObjectMother {

    public static LoggingGenerator noOpLoggingGenerator() {
        return new NoOpLoggingGenerator();
    }

    private NoOpLoggingObjectMother() {
        // factory class
    }

}
