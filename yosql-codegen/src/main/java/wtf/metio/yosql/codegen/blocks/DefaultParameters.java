/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.immutables.JavaConfiguration;

import javax.lang.model.element.Modifier;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultParameters implements Parameters {

    private final JavaConfiguration javaConfiguration;
    private final ConcurrentHashMap<TypeName, Integer> args = new ConcurrentHashMap<>();

    public DefaultParameters(final JavaConfiguration javaConfiguration) {
        this.javaConfiguration = javaConfiguration;
    }

    @Override
    public ParameterSpec parameter(final Class<?> type, final String name) {
        return parameter(ClassName.get(type), name);
    }

    @Override
    public ParameterSpec parameter(final TypeName type, final String name) {
        final var safeName = name == null || name.isBlank() ? "arg" + args.compute(type, (k, v) -> {
            if (v == null) {
                return 1;
            }
            return v + 1;
        }) : name;
        if (javaConfiguration.useFinal()) {
            return ParameterSpec.builder(type, safeName, Modifier.FINAL).build();
        }
        return ParameterSpec.builder(type, safeName).build();
    }

}
