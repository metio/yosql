package de.xn__ho_hia.yosql;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.dagger.Delegating;
import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.api.TypeWriter;
import de.xn__ho_hia.yosql.generator.api.UtilitiesGenerator;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

/**
 * Dagger2 module for YoSql.
 */
@Module
@SuppressWarnings("static-method")
public class YoSqlModule {

    @Provides
    YoSql provideYoSql(
            final SqlFileResolver fileResolver,
            final SqlFileParser sqlFileParser,
            final @Delegating RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilsGenerator,
            final ExecutionErrors errors,
            final Timer timer,
            final TypeWriter typeWriter) {
        return new YoSqlImplementation(fileResolver, sqlFileParser, repositoryGenerator, utilsGenerator, errors, timer,
                typeWriter);
    }

}
