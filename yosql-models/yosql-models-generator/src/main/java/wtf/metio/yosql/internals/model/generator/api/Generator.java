/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator.api;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Generator extends Function<ConfigurationGroup, Stream<TypeSpec>> {

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
            final var split = ((String) value).split(",");
            final var stringJoiner = new StringJoiner("\", \"", "\"", "\"");
            Arrays.stream(split)
                    .map(String::strip)
                    .forEach(stringJoiner::add);
            builder.add("$T.of($L)", List.class, stringJoiner.toString());
        }  else if ("java.nio.file.Path".equals(type.toString())) {
            builder.add("$T.get($S)", Paths.class, value);
        } else if ("java.nio.charset.Charset".equals(type.toString())) {
            builder.add("$T.forName($S)", Charset.class, value);
        } else if (value.getClass().isEnum()) {
            builder.add("$T.$L", value.getClass(), value);
        } else if (type.isPrimitive() || type.isBoxedPrimitive()) {
            builder.add("$L", value);
        } else {
            System.out.println("typeName: " + type);
        }
        return builder.build();
    }

    @Deprecated // TODO: remove usage in Gradle
    default boolean resultRowConverter(final ConfigurationSetting setting) {
        return usesResultRowConverter(setting) || usesResultRowConverters(setting);
    }

    default boolean usesCharset(final ConfigurationSetting setting) {
        return TypicalTypes.CHARSET.equals(setting.type());
    }

    default boolean usesNioPath(final ConfigurationSetting setting) {
        return setting.type().equals(TypeName.get(Path.class));
    }

    default boolean usesResultRowConverter(final ConfigurationSetting setting) {
        return setting.type().equals(TypeName.get(ResultRowConverter.class));
    }

    default boolean usesResultRowConverters(final ConfigurationSetting setting) {
        return setting.type().equals(TypicalTypes.listOf(ResultRowConverter.class));
    }

}
