/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.validation;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.codegen.validation.*;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.annotations.Delegating;

import javax.inject.Singleton;
import java.util.Set;

@Module
public class DefaultValidationModule {

    @Provides
    @Singleton
    RuntimeValidator provideRuntimeValidator(
            final RuntimeConfiguration runtimeConfiguration,
            @Delegating final RuntimeConfigurationValidator runtimeConfigurationValidator) {
        return new DefaultRuntimeValidator(runtimeConfiguration, runtimeConfigurationValidator);
    }

    @Provides
    @Delegating
    @Singleton
    RuntimeConfigurationValidator provideRuntimeConfigurationValidator(final Set<RuntimeConfigurationValidator> validators) {
        return new DelegatingRuntimeConfigurationValidator(validators);
    }

    @IntoSet
    @Provides
    @Singleton
    RuntimeConfigurationValidator provideNamesConfigurationValidator(
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        return new NamesConfigurationValidator(errors, messages);
    }

    @IntoSet
    @Provides
    @Singleton
    RuntimeConfigurationValidator provideConverterConfigurationValidator(
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        return new ConverterConfigurationValidator(errors, messages);
    }

    @IntoSet
    @Provides
    @Singleton
    RuntimeConfigurationValidator provideFilesConfigurationValidator(
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        return new FilesConfigurationValidator(errors, messages);
    }

}
