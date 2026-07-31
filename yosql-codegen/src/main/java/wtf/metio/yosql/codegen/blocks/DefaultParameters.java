/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.immutables.JavaConfiguration;

import javax.lang.model.element.Modifier;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultParameters implements Parameters {

    private final JavaConfiguration java;
    private final ConcurrentHashMap<TypeName, Integer> args = new ConcurrentHashMap<>();

    public DefaultParameters(final JavaConfiguration java) {
        this.java = java;
    }

    @Override
    public ParameterSpec parameter(final Class<?> type, final String name) {
        return parameter(ClassName.get(type), name);
    }

    @Override
    public ParameterSpec parameter(final TypeName type, final String name) {
        return java.useFinalParameters()
                ? buildParameter(type, name, Modifier.FINAL)
                : buildParameter(type, name);
    }

    @Override
    public ParameterSpec parameterForInterfaces(final TypeName type, final String name) {
        return buildParameter(type, name);
    }

    private ParameterSpec buildParameter(final TypeName type, final String name, final Modifier... modifier) {
        final var safeName = name == null || name.isBlank() ? "arg" + args.compute(type, (k, v) -> {
            if (v == null) {
                return 1;
            }
            return v + 1;
        }) : name;
        return ParameterSpec.builder(type, safeName, modifier).build();
    }

}
