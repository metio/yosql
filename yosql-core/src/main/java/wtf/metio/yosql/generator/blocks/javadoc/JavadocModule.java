package wtf.metio.yosql.generator.blocks.javadoc;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.generator.blocks.api.Javadoc;

@Module
public class JavadocModule {

    @Provides
    Javadoc javadoc() {
        return new DefaultJavadoc();
    }

}
