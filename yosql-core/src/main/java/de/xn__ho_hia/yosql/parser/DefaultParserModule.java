package de.xn__ho_hia.yosql.parser;

import java.io.PrintStream;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * Provides the default parser.
 */
@Module
@SuppressWarnings("static-method")
public final class DefaultParserModule {

    @Provides
    SqlFileParser provideSqlFilePaser(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final SqlConfigurationFactory factory,
            final PrintStream out) {
        return new DefaultSqlFileParser(errors, config, factory, out);
    }

}
