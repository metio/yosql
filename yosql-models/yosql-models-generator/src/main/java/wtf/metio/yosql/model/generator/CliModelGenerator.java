/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.generator;

import com.squareup.javapoet.*;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;

final class CliModelGenerator extends AbstractModelGenerator {

    @Override
    protected List<TypeSpec> typesFor(final ConfigurationGroup group) {
        return group.cliTypes();
    }

    @Override
    protected List<AnnotationSpec> annotationsFor(final ConfigurationGroup group) {
        return group.cliAnnotations();
    }

    @Override
    protected List<MethodSpec> methodsFor(final ConfigurationGroup group) {
        return group.cliMethods();
    }

    @Override
    protected List<MethodSpec> methodsFor(final ConfigurationSetting setting) {
        return setting.cliMethods();
    }

    @Override
    protected List<FieldSpec> fieldsFor(final ConfigurationGroup group) {
        return group.cliFields();
    }

    @Override
    protected List<FieldSpec> fieldsFor(final ConfigurationSetting setting) {
        return setting.cliFields();
    }

    @Override
    protected CodeBlock initialize(final ConfigurationSetting setting) {
        return setting.cliInitializer().orElseGet(() -> CodeBlock.of(""));
    }

    @Override
    protected List<ParameterSpec> immutableConfigurationParametersFor(final ConfigurationGroup group) {
        return group.cliParameters();
    }

    @Override
    protected List<ParameterSpec> gradleConventionParametersFor(final ConfigurationGroup group) {
        return List.of();
    }

    @Override
    protected boolean convertsToImmutable() {
        return true;
    }

    @Override
    protected boolean hasConvention() {
        return false;
    }

    @Override
    protected Optional<CodeBlock> convention(final ConfigurationSetting setting) {
        return Optional.empty();
    }

    @Override
    protected Modifier[] configurationGroupClassModifiers() {
        return new Modifier[]{Modifier.PUBLIC};
    }

    @Override
    protected TypeSpec.Builder configurationGroupClassBuilder(final ConfigurationGroup group) {
        return TypeSpec.classBuilder(ClassName.bestGuess(group.name()));
    }

}
