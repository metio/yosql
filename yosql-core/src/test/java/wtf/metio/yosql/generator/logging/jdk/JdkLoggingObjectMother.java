package wtf.metio.yosql.generator.logging.jdk;

import wtf.metio.yosql.generator.api.LoggingGenerator;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.fields;
import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

public final class JdkLoggingObjectMother {
    
    public static LoggingGenerator jdkLoggingGenerator() {
        return new JdkLoggingGenerator(names(), fields());
    }

    private JdkLoggingObjectMother() {
        // factory class
    }

}
