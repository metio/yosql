/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.files;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.errors.ExecutionErrors;
import wtf.metio.yosql.codegen.files.*;
import wtf.metio.yosql.codegen.logging.Parser;
import wtf.metio.yosql.codegen.logging.Reader;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module that provides all necessary classes to parse files.
 */
@Module
public class DefaultFilesModule {

    @Provides
    public SqlConfigurationFactory provideSqlConfigurationFactory(
            @Parser final LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        return new DefaultSqlConfigurationFactory(logger, runtimeConfiguration, errors, messages);
    }

    @Provides
    public SqlFileResolver provideSqlFileResolver(
            @Reader final LocLogger logger,
            final ParserPreconditions preconditions,
            final RuntimeConfiguration runtimeConfiguration,
            final ExecutionErrors errors) {
        return new DefaultSqlFileResolver(logger, preconditions, runtimeConfiguration.files(), errors);
    }

    @Provides
    public SqlFileParser provideSqlFileParser(
            @Parser final LocLogger logger,
            final SqlConfigurationFactory factory,
            final RuntimeConfiguration runtimeConfiguration,
            final ExecutionErrors errors) {
        return new DefaultSqlFileParser(logger, factory, runtimeConfiguration.files(), errors);
    }

    @Provides
    public FileParser provideFileParser(
            final SqlFileResolver fileResolver,
            final SqlFileParser fileParser) {
        return new DefaultFileParser(fileResolver, fileParser);
    }

    @Provides
    public ParserPreconditions provideParserPreconditions(
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        return new DefaultParserPreconditions(errors, messages);
    }

}
