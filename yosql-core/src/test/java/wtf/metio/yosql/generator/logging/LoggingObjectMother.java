package wtf.metio.yosql.generator.logging;

import wtf.metio.yosql.generator.api.LoggingGenerator;

import static wtf.metio.yosql.generator.logging.jdk.JdkLoggingObjectMother.jdkLoggingGenerator;
import static wtf.metio.yosql.generator.logging.log4j.Log4jLoggingObjectMother.log4jLoggingGenerator;
import static wtf.metio.yosql.generator.logging.noop.NoOpLoggingObjectMother.noOpLoggingGenerator;
import static wtf.metio.yosql.generator.logging.slf4j.Slf4jLoggingObjectMother.slf4jLoggingGenerator;

public final class LoggingObjectMother {

    public static LoggingGenerator loggingGenerator() {
        return new DelegatingLoggingGenerator(
                null,
                jdkLoggingGenerator(),
                log4jLoggingGenerator(),
                noOpLoggingGenerator(),
                slf4jLoggingGenerator()
        );
    }

    private LoggingObjectMother() {
        // factory class
    }

}
