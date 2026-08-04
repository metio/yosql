/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.example.maven.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wtf.metio.yosql.example.maven.jdbc.persistence.CallcenterRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.CompanyRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.ItemRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.PersonRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.SchemaRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.UserRepository;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Every shape of method the generator can write is written by one of these examples.
 *
 * <p>A statement's type and its returning mode decide which method the generator emits, and for a
 * long time the examples reached seven of the eleven combinations — the ones that happened to be
 * useful for demonstrating something else. Four of the missing four emitted Java that does not
 * compile, and nothing noticed for as long as nobody wrote a statement of that shape.</p>
 *
 * <p>Compiling every combination is held by a test in {@code yosql-codegen}. What these examples add
 * is a database: {@code ExampleApp} runs them, and the two failures that compile perfectly — a
 * cursor handing back a stream over resources it has already closed, and a write executed twice —
 * only show up with a driver on the other end. So the examples have to keep covering the shapes,
 * and this is what says so when one is dropped.</p>
 *
 * <p>Read off the generated classes rather than the {@code .sql} files, because what is generated
 * depends on defaults the front matter does not repeat — and a second reading of those rules here
 * would be a second implementation to drift.</p>
 */
@DisplayName("the generated example repositories")
class GeneratedMatrixTest {

    private static final List<Class<?>> REPOSITORIES = List.of(
            CallcenterRepository.class, CompanyRepository.class, ItemRepository.class,
            PersonRepository.class, SchemaRepository.class, UserRepository.class);

    /** Batch answers with update counts, which a statement returning rows has nothing to do with. */
    private static final Set<String> WRITE_ONLY = Set.of("BATCH");

    private static final List<String> KINDS = List.of("READING", "WRITING", "CALLING");
    private static final List<String> MODES = List.of("NONE", "SINGLE", "MULTIPLE", "CURSOR", "BATCH");

    private static final List<String> READ_PREFIXES =
            List.of("read", "select", "find", "query", "lookup", "get");

    static Stream<Arguments> combinations() {
        final var covered = covered();
        final var cases = new ArrayList<Arguments>();
        for (final var kind : KINDS) {
            for (final var mode : MODES) {
                if (WRITE_ONLY.contains(mode) && !"WRITING".equals(kind)) {
                    continue;
                }
                cases.add(Arguments.of(kind + " + " + mode, covered));
            }
        }
        return cases.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("combinations")
    @DisplayName("cover every shape a statement can take")
    void isCovered(final String combination, final Set<String> covered) {
        Assertions.assertTrue(covered.contains(combination),
                () -> "no example statement generates " + combination + ", so nothing runs that "
                        + "method against a database.\ncovered: " + covered);
    }

    private static Set<String> covered() {
        return REPOSITORIES.stream()
                .flatMap(repository -> Stream.of(repository.getDeclaredMethods()))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(GeneratedMatrixTest::shapeOf)
                .flatMap(Optional::stream)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private static Optional<String> shapeOf(final Method method) {
        return modeOf(method).map(mode -> kindOf(method.getName()) + " + " + mode);
    }

    private static Optional<String> modeOf(final Method method) {
        final var returned = method.getReturnType();
        if (returned.equals(int[].class)) {
            return Optional.of("BATCH");
        }
        if (returned.equals(Optional.class)) {
            return Optional.of("SINGLE");
        }
        if (List.class.isAssignableFrom(returned)) {
            return Optional.of("MULTIPLE");
        }
        if (Stream.class.isAssignableFrom(returned)) {
            return Optional.of("CURSOR");
        }
        if (returned.equals(void.class) || returned.equals(int.class)) {
            return Optional.of("NONE");
        }
        return Optional.empty();
    }

    private static String kindOf(final String name) {
        final var lower = name.toLowerCase(Locale.ROOT);
        if (lower.startsWith("call")) {
            return "CALLING";
        }
        return READ_PREFIXES.stream().anyMatch(lower::startsWith) ? "READING" : "WRITING";
    }

}
