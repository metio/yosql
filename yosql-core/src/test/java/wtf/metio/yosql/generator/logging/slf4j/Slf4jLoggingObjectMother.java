package wtf.metio.yosql.generator.logging.slf4j;

import wtf.metio.yosql.generator.api.LoggingGenerator;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.fields;
import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

public final class Slf4jLoggingObjectMother {

    public static LoggingGenerator slf4jLoggingGenerator() {
        return new Slf4jLoggingGenerator(names(), fields());
    }

    private Slf4jLoggingObjectMother() {
        // factory class
    }

}
