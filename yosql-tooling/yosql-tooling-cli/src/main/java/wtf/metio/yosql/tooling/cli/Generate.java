/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.cli;

import picocli.CommandLine;
import wtf.metio.yosql.models.cli.*;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

import java.util.List;
import java.util.Locale;
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

    private static final List<Locale> SUPPORTED_LOCALES = List.of(Locale.ENGLISH, Locale.GERMAN);

    @CommandLine.Mixin
    public Files files;

    @CommandLine.Mixin
    public Java java;

    @CommandLine.Mixin
    public Api api;

    @CommandLine.Mixin
    public Annotations annotations;

    @CommandLine.Mixin
    public Repositories repositories;

    @CommandLine.Mixin
    public Resources resources;

    @Override
    public Integer call() {
        DaggerYoSQLComponent.builder()
                .runtimeConfiguration(createConfiguration())
                .locale(determineLocale())
                .build()
                .yosql()
                .generateCode();
        return 0;
    }

    private RuntimeConfiguration createConfiguration() {
        return RuntimeConfiguration.usingDefaults()
                .setAnnotations(annotations.asConfiguration())
                .build();
    }

    private Locale determineLocale() {
        if (SUPPORTED_LOCALES.contains(Locale.getDefault())) {
            return Locale.getDefault();
        }
        return Locale.ENGLISH;
    }

}
