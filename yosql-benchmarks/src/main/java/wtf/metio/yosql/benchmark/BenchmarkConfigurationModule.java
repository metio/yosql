/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.sql.ResultRowConverter;

import java.io.PrintStream;
import java.nio.file.Path;

import static wtf.metio.yosql.model.options.GenerateOptions.*;

/**
 * Module for {@link PrintStream}.
 */
@Module
public class BenchmarkConfigurationModule {

    private final Path inputDirectory;
    private final Path outputDirectory;

    /**
     * @param inputDirectory  The input directory to use.
     * @param outputDirectory The output directory to use.
     */
    public BenchmarkConfigurationModule(
            final Path inputDirectory,
            final Path outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    @Provides
    RuntimeConfiguration provideExecutionConfiguration(final Translator translator) {
        final String basePackageName = translator.localized(BASE_PACKAGE_NAME_DEFAULT);
        final String utilityPackageName = translator.localized(UTILITY_PACKAGE_NAME_DEFAULT);
        final String converterPackageName = translator.localized(CONVERTER_PACKAGE_NAME_DEFAULT);
        final String defaultRowConverterAlias = translator.localized(DEFAULT_ROW_CONVERTER_DEFAULT);
        final String defaultResultRowClassName = translator.localized(DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT);

        final ResultRowConverter toResultRow = new ResultRowConverter();
        toResultRow.setAlias(defaultRowConverterAlias);
        toResultRow.setResultType(String.join(".", basePackageName, utilityPackageName, defaultResultRowClassName));
        toResultRow.setConverterType(String.join(".", basePackageName, converterPackageName,
                translator.localized(TO_RESULT_ROW_CONVERTER_CLASS_NAME)));

        return RuntimeConfiguration.builder()
                .build();
    }

}
