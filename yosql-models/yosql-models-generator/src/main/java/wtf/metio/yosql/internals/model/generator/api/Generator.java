/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator.api;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface Generator extends Function<ConfigurationGroup, TypeSpec> {

    TypeName typeOf(ConfigurationSetting setting);

    Optional<Object> valueOf(ConfigurationSetting setting);

    List<AnnotationSpec> annotationsFor(ConfigurationGroup group);

    List<AnnotationSpec> annotationsFor(ConfigurationSetting setting);

    List<MethodSpec> methodsFor(ConfigurationGroup group);

    default CodeBlock defaultValue(final Object value, final TypeName type) {
        if (value instanceof CodeBlock) {
            return (CodeBlock) value;
        }
        final var builder = CodeBlock.builder();
        if ("java.lang.String".equals(type.toString())) {
            builder.add("$S", value);
        } else if (TypicalTypes.listOf(TypicalTypes.STRING).equals(type) && value instanceof String) {
            builder.add("$T.of($S)", List.class, value);
        }  else if ("java.nio.file.Path".equals(type.toString())) {
            builder.add("$T.get($S)", Paths.class, value);
        } else if ("java.nio.charset.Charset".equals(type.toString())) {
            builder.add("$T.forName($S)", Charset.class, value);
        } else if (value.getClass().isEnum()) {
            builder.add("$T.$L", value.getClass(), value);
        } else if (type.isPrimitive() || type.isBoxedPrimitive()) {
            builder.add("$L", value);
        } else {
            System.out.println("typeName: " + type.toString());
        }
        return builder.build();
    }

}
