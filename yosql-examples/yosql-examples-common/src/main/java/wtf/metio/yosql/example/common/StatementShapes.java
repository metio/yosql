/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.common;

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
 * Every shape of method the generator can write, and which of them a set of repositories reaches.
 *
 * <p>A statement's type and its returning mode decide which method the generator emits, and for a
 * long time the examples reached seven of the eleven combinations — the ones that happened to be
 * useful for demonstrating something else. Four of the missing four emitted Java that does not
 * compile, and nothing noticed for as long as nobody wrote a statement of that shape.</p>
 *
 * <p>Compiling every combination is held by a test in {@code yosql-codegen}. What the examples add
 * is a database: they run the methods, and the two failures that compile perfectly — a cursor
 * handing back a stream over resources it has already closed, and a write executed twice — only
 * show up with a driver on the other end.</p>
 *
 * <p>Lives here rather than in either example because both of them ask the same question of the
 * same statements through different build tools, and two copies of this would be two things to keep
 * in step. The assertion stays with each example, which is what names the build tool that lost a
 * shape.</p>
 *
 * <p>Read off the generated classes rather than the {@code .sql} files, because what is generated
 * depends on defaults the front matter does not repeat — and a second reading of those rules here
 * would be a second implementation to drift.</p>
 */
public final class StatementShapes {

    /** Batch answers with update counts, which a statement returning rows has nothing to do with. */
    private static final Set<String> WRITE_ONLY = Set.of("BATCH");

    private static final List<String> KINDS = List.of("READING", "WRITING", "CALLING");
    private static final List<String> MODES = List.of("NONE", "SINGLE", "MULTIPLE", "CURSOR", "BATCH");

    private static final List<String> READ_PREFIXES =
            List.of("read", "select", "find", "query", "lookup", "get");

    /**
     * @return every combination an example is expected to reach, as {@code KIND + MODE}
     */
    public static List<String> combinations() {
        final var cases = new ArrayList<String>();
        for (final var kind : KINDS) {
            for (final var mode : MODES) {
                if (WRITE_ONLY.contains(mode) && !"WRITING".equals(kind)) {
                    continue;
                }
                cases.add(kind + " + " + mode);
            }
        }
        return cases;
    }

    /**
     * @param repositories the generated repositories to read
     * @return the combinations those repositories actually declare a method for
     */
    public static Set<String> coveredBy(final List<Class<?>> repositories) {
        return repositories.stream()
                .flatMap(repository -> Stream.of(repository.getDeclaredMethods()))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(StatementShapes::shapeOf)
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

    private StatementShapes() {
        // utility class, call the static methods directly
    }

}
