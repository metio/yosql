package wtf.metio.yosql.generator.blocks;

import dagger.Module;
import wtf.metio.yosql.generator.blocks.javadoc.JavadocModule;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcBlocksModule;
import wtf.metio.yosql.generator.blocks.generic.GenericBlocksModule;

@Module(includes = {
        JavadocModule.class,
        GenericBlocksModule.class,
        JdbcBlocksModule.class,
})
public class BlocksModule {
}
