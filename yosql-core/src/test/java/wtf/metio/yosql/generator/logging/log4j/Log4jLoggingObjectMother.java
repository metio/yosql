package wtf.metio.yosql.generator.logging.log4j;

import wtf.metio.yosql.generator.api.LoggingGenerator;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.fields;
import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

public final class Log4jLoggingObjectMother {

    public static LoggingGenerator log4jLoggingGenerator() {
        return new Log4jLoggingGenerator(names(), fields());
    }

    private Log4jLoggingObjectMother() {
        // factory class
    }

}
