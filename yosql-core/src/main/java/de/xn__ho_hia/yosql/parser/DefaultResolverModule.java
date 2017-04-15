package de.xn__ho_hia.yosql.parser;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * Provides the default parser.
 */
@Module
@SuppressWarnings("static-method")
public final class DefaultResolverModule {

    @Provides
    SqlFileResolver provideSqlFileResolver(
            final ParserPreconditions preconditions,
            final ExecutionErrors errors,
            final ExecutionConfiguration configuration) {
        return new PathBasedSqlFileResolver(preconditions, errors, configuration);
    }

}
