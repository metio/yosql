/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.cli;

import picocli.CommandLine;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.cli.*;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "generate",
        description = "Generate Java code using SQL files as sources",
        versionProvider = VersionProvider.class,
        mixinStandardHelpOptions = true,
        showAtFileInUsageHelp = true,
        usageHelpAutoWidth = true,
        showDefaultValues = true
)
public class Generate implements Callable<Integer> {

    @CommandLine.Mixin
    public Annotations annotations;

    @CommandLine.Mixin
    public Converter converter;

    @CommandLine.Mixin
    public Files files;

    @CommandLine.Mixin
    public Java java;

    @CommandLine.Mixin
    public Logging logging;

    @CommandLine.Mixin
    public Names names;

    @CommandLine.Mixin
    public Repositories repositories;

    @CommandLine.Mixin
    public Resources resources;

    @Override
    public Integer call() {
        DaggerYoSQLComponent.builder()
                .runtimeConfiguration(createConfiguration())
                .locale(SupportedLocales.defaultLocale())
                .build()
                .yosql()
                .generateCode();
        return 0;
    }

    private RuntimeConfiguration createConfiguration() {
        return RuntimeConfiguration.usingDefaults()
                .setAnnotations(annotations.asConfiguration())
                .setFiles(files.asConfiguration(System.getProperty("user.dir")))
                .setJava(java.asConfiguration())
                .setLogging(logging.asConfiguration())
                .setNames(names.asConfiguration())
                .setRepositories(repositories.asConfiguration())
                .setResources(resources.asConfiguration())
                .setConverter(converter.asConfiguration())
                .build();
    }

}
