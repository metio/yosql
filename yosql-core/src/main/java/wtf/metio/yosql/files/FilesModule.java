package wtf.metio.yosql.files;

import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.annotations.Parser;
import wtf.metio.yosql.model.annotations.Reader;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.errors.ExecutionErrors;

/**
 * Dagger module that provides all necessary classes to parse files.
 */
@Module
public class FilesModule {

    @Provides
    SqlConfigurationFactory provideSqlConfigurationFactory(
            @Parser final LocLogger logger,
            final RuntimeConfiguration config,
            final ExecutionErrors errors) {
        return new DefaultSqlConfigurationFactory(logger, config, errors);
    }

    @Provides
    SqlFileResolver provideSqlFileResolver(
            @Reader final LocLogger logger,
            final ParserPreconditions preconditions,
            final RuntimeConfiguration configuration,
            final ExecutionErrors errors) {
        return new DefaultSqlFileResolver(logger, preconditions, configuration, errors);
    }

    @Provides
    SqlFileParser provideSqlFileParser(
            @Parser final LocLogger logger,
            final SqlConfigurationFactory factory,
            final RuntimeConfiguration config,
            final ExecutionErrors errors) {
        return new DefaultSqlFileParser(logger, factory, config, errors);
    }

    @Provides
    FileParser provideFileParser(
            final SqlFileResolver fileResolver,
            final SqlFileParser fileParser) {
        return new DefaultFileParser(fileResolver, fileParser);
    }

    @Provides
    ParserPreconditions provideParserPreconditions(
            final ExecutionErrors errors,
            final Translator translator) {
        return new DefaultParserPreconditions(errors, translator);
    }

}
