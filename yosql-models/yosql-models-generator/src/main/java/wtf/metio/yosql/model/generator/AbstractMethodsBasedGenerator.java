/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generator that exposes configuration groups/settings as methods.
 */
public abstract class AbstractMethodsBasedGenerator implements Generator {

    protected final List<MethodSpec> defaultMethods(final ConfigurationGroup group) {
        return group.settings().stream()
                .filter(setting -> valueOf(setting).isPresent())
                .map(this::defaultMethod)
                .collect(Collectors.toList());
    }

    protected abstract MethodSpec defaultMethod(ConfigurationSetting setting);

    protected final List<MethodSpec> optionalMethods(final ConfigurationGroup group) {
        return group.settings().stream()
                .filter(setting -> valueOf(setting).isEmpty())
                .map(this::optionalMethod)
                .collect(Collectors.toList());
    }

    protected final MethodSpec optionalMethod(final ConfigurationSetting setting) {
        return MethodSpec.methodBuilder(setting.name())
                .addJavadoc(setting.description())
                .addAnnotations(setting.immutableAnnotations())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypicalTypes.optionalOf(typeOf(setting)))
                .build();
    }

    protected final CodeBlock defaultReturn(final ConfigurationSetting setting) {
        return returnDefaultValue(valueOf(setting).orElseThrow(), typeOf(setting));
    }

    private CodeBlock returnDefaultValue(final Object value, final TypeName type) {
        return CodeBlock.builder()
                .addStatement(CodeBlock.builder().add("return $L", defaultValue(value, type)).build())
                .build();
    }

}
