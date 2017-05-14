/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.dagger.Delegating;
import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.api.TypeWriter;
import de.xn__ho_hia.yosql.generator.api.UtilitiesGenerator;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.Translator;
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
            final TypeWriter typeWriter,
            final Translator messages,
            final ExecutionConfiguration configuration) {
        return new YoSqlImplementation(fileResolver, sqlFileParser, repositoryGenerator, utilsGenerator, errors, timer,
                typeWriter, messages, configuration);
    }

}
