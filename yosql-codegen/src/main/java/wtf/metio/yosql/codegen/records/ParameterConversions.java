/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.codegen.exceptions.NonRecordValueTypeException;
import wtf.metio.yosql.codegen.exceptions.UnbindableParameterException;
import wtf.metio.yosql.models.configuration.GeneratedNames;
import wtf.metio.yosql.models.configuration.SqlParameter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Decides what a parameter's declared type has to become before a driver will take it.
 *
 * <p>The mirror of what the generated converters do on the way out, and gated on the same rule: a
 * type is a value when it declares {@code static X valueOf(P)}, in which case its {@code P}-returning
 * accessor is what gets bound. So a type either travels in both directions or in neither, and a
 * repository can take the same {@code TenantId} it hands back.</p>
 *
 * <p>A parameter whose type a driver already accepts is left alone — no local, no conversion, the
 * same code as before this existed.</p>
 */
public final class ParameterConversions {

    private final RecordScanner scanner;
    private final StatementBinders binders;

    public ParameterConversions(final RecordScanner scanner, final StatementBinders binders) {
        this.scanner = scanner;
        this.binders = binders;
    }

    /**
     * @param parameter the declared parameter
     * @param source an expression holding the value as the caller passed it
     * @return the locals to declare and the name to bind, or empty when {@code source} binds as it is
     */
    public Optional<Conversion> convert(final SqlParameter parameter, final CodeBlock source) {
        final var declared = parameter.typeName();
        if (declared.isEmpty()) {
            return Optional.empty();
        }
        return convert(declared.get(), parameter.name().orElseThrow(), source);
    }

    /**
     * The same, for a value that is not a parameter of its own.
     *
     * <p>A collection binds one element per placeholder, and an element is the type the column holds
     * rather than the type that holds the elements — so it converts exactly as the identical scalar
     * would, through a local of its own.</p>
     *
     * @param declared the type of the value being bound
     * @param name what to base the locals on
     * @param source an expression holding the value
     */
    public Optional<Conversion> convert(final TypeName declared, final String name, final CodeBlock source) {
        final var steps = new ArrayList<Step>();
        collect(declared, name, source, steps, new LinkedHashSet<>());
        if (steps.isEmpty()) {
            return Optional.empty();
        }
        final var declarations = CodeBlock.builder();
        for (final var step : steps) {
            declarations.addStatement("final $T $N = $L", step.type(), step.name(), step.value());
        }
        return Optional.of(new Conversion(declarations.build(), steps.get(steps.size() - 1).name()));
    }

    private void collect(
            final TypeName type,
            final String name,
            final CodeBlock source,
            final List<Step> steps,
            final Set<ClassName> unwrapped) {
        final var enumeration = isEnum(type);
        final var builtIn = binders.bind(type, enumeration, source);
        if (builtIn.isPresent()) {
            steps.add(new Step(local(name, steps), binders.boundType(type, enumeration),
                    guard(source, builtIn.get(), binders.dereferences(type, enumeration))));
            return;
        }
        final var value = valueAccessor(type, name, unwrapped);
        if (value.isEmpty()) {
            return;
        }
        final var accessor = value.get();
        final var read = CodeBlock.of("$L.$N()", source, accessor.name());
        final var local = local(name, steps);
        steps.add(new Step(local, boxed(accessor.type()), guard(source, read, true)));
        unwrapped.add((ClassName) type);
        // The value inside may itself need converting — an Instant wrapped in a record still has to
        // become a Timestamp.
        collect(accessor.type(), name, CodeBlock.of("$N", local), steps, unwrapped);
    }

    /**
     * The accessor a value type is written through: the component whose type is the one its
     * {@code valueOf} takes.
     */
    private Optional<JavaSourceComponent> valueAccessor(
            final TypeName type, final String name, final Set<ClassName> unwrapped) {
        if (!(type instanceof ClassName className) || unwrapped.contains(className)) {
            return Optional.empty();
        }
        final var source = scanner.scan(className);
        if (source.isEmpty()) {
            return Optional.empty();
        }
        final var declaration = source.get();
        if (!declaration.hasValueOf()) {
            if (declaration.isRecord()) {
                throw new UnbindableParameterException(name, className,
                        declaration.components().stream().map(JavaSourceComponent::name).toList());
            }
            return Optional.empty();
        }
        if (!declaration.isRecord()) {
            throw new NonRecordValueTypeException(name, className);
        }
        final var candidates = declaration.valueOfParameters().stream()
                .flatMap(parameter -> declaration.components().stream()
                        .filter(component -> component.type().equals(parameter)))
                .distinct()
                .toList();
        if (candidates.size() != 1) {
            throw new UnbindableParameterException(name, className,
                    declaration.components().stream().map(JavaSourceComponent::name).toList());
        }
        return Optional.of(candidates.getFirst());
    }

    private boolean isEnum(final TypeName type) {
        return type instanceof ClassName className
                && scanner.scan(className).map(JavaSourceType::isEnum).orElse(Boolean.FALSE);
    }

    /**
     * A conversion dereferences its input, so a caller passing null would otherwise get a
     * {@link NullPointerException} where it meant to write SQL NULL.
     */
    private static CodeBlock guard(final CodeBlock source, final CodeBlock converted, final boolean needed) {
        return needed ? CodeBlock.of("$L == null ? null : $L", source, converted) : converted;
    }

    private static TypeName boxed(final TypeName type) {
        return type.isPrimitive() ? type.box() : type;
    }

    private static String local(final String name, final List<Step> steps) {
        final var base = name + GeneratedNames.PARAMETER_SUFFIX;
        return steps.isEmpty() ? base : base + (steps.size() + 1);
    }

    /**
     * @param declarations the locals to emit before binding
     * @param boundName the local to bind
     */
    public record Conversion(CodeBlock declarations, String boundName) {
    }

    private record Step(String name, TypeName type, CodeBlock value) {
    }

}
