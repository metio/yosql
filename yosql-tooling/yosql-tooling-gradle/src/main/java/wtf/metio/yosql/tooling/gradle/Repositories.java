/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;

import java.util.List;

/**
 * Configures repository related options.
 */
public abstract class Repositories {

    /**
     * @return The base package name for all repositories
     */
    @Input
    public abstract Property<String> getBasePackageName();

    /**
     * @return The repository name suffix to use.
     */
    @Input
    public abstract Property<String> getRepositoryNameSuffix();

    /**
     * @return Generate interfaces for all repositories.
     */
    @Input
    public abstract Property<Boolean> getGenerateInterfaces();

    /**
     * @return Generate standard methods.
     */
    @Input
    public abstract Property<Boolean> getGenerateStandardApi();

    /**
     * @return Generate batch methods.
     */
    @Input
    public abstract Property<Boolean> getGenerateBatchApi();

    /**
     * @return Generate eager stream methods.
     */
    @Input
    public abstract Property<Boolean> getGenerateStreamEagerApi();

    /**
     * @return Generate lazy stream methods.
     */
    @Input
    public abstract Property<Boolean> getGenerateStreamLazyApi();

    /**
     * @return Generate rxjava methods.
     */
    @Input
    public abstract Property<Boolean> getGenerateRxJavaApi();

    /**
     * @return Catch exceptions during SQL execution and re-throw them as RuntimeExceptions.
     */
    @Input
    public abstract Property<Boolean> getCatchAndRethrow();

    /**
     * @return Should converters be injected as constructor parameters or not?
     */
    @Input
    public abstract Property<Boolean> getInjectConverters();

    /**
     * @return Validate user given names against list of allowed prefixes per type
     */
    @Input
    public abstract Property<Boolean> getValidateMethodNamePrefixes();

    /**
     * @return The batch method name suffix to use.
     */
    @Input
    public abstract Property<String> getBatchPrefix();

    /**
     * @return The batch method name suffix to use.
     */
    @Input
    public abstract Property<String> getBatchSuffix();

    /**
     * @return The stream method name suffix to use.
     */
    @Input
    public abstract Property<String> getStreamPrefix();

    /**
     * @return The stream method name suffix to use.
     */
    @Input
    public abstract Property<String> getStreamSuffix();

    /**
     * @return The rxjava method name suffix to use.
     */
    @Input
    public abstract Property<String> getRxjava2Prefix();

    /**
     * @return The rxjava method name suffix to use.
     */
    @Input
    public abstract Property<String> getRxjava2Suffix();

    /**
     * @return The lazy method name suffix to use.
     */
    @Input
    public abstract Property<String> getLazyName();

    /**
     * @return The eager method name suffix to use.
     */
    @Input
    public abstract Property<String> getEagerName();

    /**
     * @return The allowed method name prefixes for writing statements.
     */
    @Input
    public abstract ListProperty<String> getAllowedWritePrefixes();

    /**
     * @return The allowed method name prefixes for reading statements.
     */
    @Input
    public abstract ListProperty<String> getAllowedReadPrefixes();

    /**
     * @return The allowed method name prefixes for calling statements.
     */
    @Input
    public abstract ListProperty<String> getAllowedCallPrefixes();

    RepositoriesConfiguration asConfiguration() {
        return RepositoriesConfiguration.usingDefaults()
                .setBasePackageName(getBasePackageName().get())
                .setRepositoryNameSuffix(getRepositoryNameSuffix().get())
                .setGenerateInterfaces(getGenerateInterfaces().get())
                .setGenerateStandardApi(getGenerateStandardApi().get())
                .setGenerateBatchApi(getGenerateBatchApi().get())
                .setGenerateStreamEagerApi(getGenerateStreamEagerApi().get())
                .setGenerateStreamLazyApi(getGenerateStreamLazyApi().get())
                .setGenerateRxJavaApi(getGenerateRxJavaApi().get())
                .setCatchAndRethrow(getCatchAndRethrow().get())
                .setInjectConverters(getInjectConverters().get())
                .setValidateMethodNamePrefixes(getValidateMethodNamePrefixes().get())
                .setBatchPrefix(getBatchPrefix().get())
                .setBatchSuffix(getBatchSuffix().get())
                .setStreamPrefix(getStreamPrefix().get())
                .setStreamSuffix(getStreamSuffix().get())
                .setRxjava2Prefix(getRxjava2Prefix().get())
                .setRxjava2Suffix(getRxjava2Suffix().get())
                .setLazyName(getLazyName().get())
                .setEagerName(getEagerName().get())
                .setAllowedCallPrefixes(getAllowedCallPrefixes().get())
                .setAllowedReadPrefixes(getAllowedReadPrefixes().get())
                .setAllowedWritePrefixes(getAllowedWritePrefixes().get())
                .build();
    }

    void configureConventions() {
        getBasePackageName().convention("com.example.persistence");
        getRepositoryNameSuffix().convention("Repository");
        getGenerateInterfaces().convention(false);
        getGenerateStandardApi().convention(true);
        getGenerateBatchApi().convention(true);
        getGenerateStreamEagerApi().convention(true);
        getGenerateStreamLazyApi().convention(true);
        getGenerateRxJavaApi().convention(true);
        getCatchAndRethrow().convention(true);
        getInjectConverters().convention(false);
        getValidateMethodNamePrefixes().convention(false);
        getBatchPrefix().convention("");
        getBatchSuffix().convention("Batch");
        getStreamPrefix().convention("");
        getStreamSuffix().convention("Stream");
        getRxjava2Prefix().convention("");
        getRxjava2Suffix().convention("Flow");
        getLazyName().convention("Lazy");
        getEagerName().convention("Eager");
        getAllowedWritePrefixes().convention(List.of("update", "insert", "delete", "create", "write", "add", "remove", "merge", "drop"));
        getAllowedReadPrefixes().convention(List.of("query", "read", "select", "find", "lookup", "get"));
        getAllowedCallPrefixes().convention(List.of("call", "execute", "evaluate", "eval"));
    }

}
