package de.xn__ho_hia.yosql.benchmark.parse_file;

import java.io.PrintStream;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.parser.SqlConfigurationFactory;
import de.xn__ho_hia.yosql.parser.SqlFileParser;

/**
 * Provides the alternative parser.
 */
@Module
@SuppressWarnings("static-method")
final class AlternativeParserModule {

    @Provides
    SqlFileParser provideSqlFilePaser(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final SqlConfigurationFactory factory,
            final PrintStream out) {
        return new AlternativeSqlFileParser(errors, config, factory, out);
    }

}
