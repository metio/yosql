/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultParameters implements Parameters {

    private final ConcurrentHashMap<TypeName, Integer> args = new ConcurrentHashMap<>();

    public DefaultParameters() {
    }

    @Override
    public ParameterSpec parameter(final Class<?> type, final String name) {
        return parameter(ClassName.get(type), name);
    }

    @Override
    public ParameterSpec parameter(final TypeName type, final String name) {
        return buildParameter(type, name, Modifier.FINAL);
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
