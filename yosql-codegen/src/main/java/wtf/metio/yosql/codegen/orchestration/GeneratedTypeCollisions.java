/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.orchestration;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeSpec;
import wtf.metio.yosql.codegen.exceptions.DuplicateGeneratedTypeException;
import wtf.metio.yosql.codegen.exceptions.GeneratedTypeExistsException;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Two types that would end up as one file.
 *
 * <p>Every generated type is one file, named after itself, and the names that can meet are settled
 * by rules that never see each other: a repository interface is the repository's name without its
 * suffix, a generated record is whatever {@code resultRowType} says, a converter is a prefix and a
 * suffix around a record's name — and a class somebody wrote by hand is subject to no rule at all.
 * Nothing about any one of those names is wrong on its own, which is why no generator can catch
 * this and why it is caught here, where the whole set exists at once and nothing is on disk yet.</p>
 *
 * <p>Both directions matter. Two generated types agreeing is one file written twice, whichever runs
 * second surviving. A generated type agreeing with a file the project already has is two files
 * claiming one name, which {@code javac} reports as a duplicate class in whichever of them it
 * reaches first — pointing at neither the statement nor the setting that chose the name.</p>
 */
public final class GeneratedTypeCollisions {

    /**
     * What our own output says about itself.
     *
     * <p>A project is free to point {@code sourceDirectory} at somewhere that already holds
     * generated code — the output of an earlier run, or a module that keeps both together. Finding
     * our own answer there and calling it a collision would fail a build for existing, which is
     * exactly the file this run is about to replace.</p>
     */
    private static final String OUR_OWN = "\"wtf.metio.yosql\"";

    private final RecordScanner scanner;

    public GeneratedTypeCollisions(final RecordScanner scanner) {
        this.scanner = scanner;
    }

    public void reject(final List<PackagedTypeSpec> generated) {
        rejectAgainstEachOther(generated);
        rejectAgainstExistingSources(generated);
    }

    private static void rejectAgainstEachOther(final List<PackagedTypeSpec> generated) {
        final var byName = new LinkedHashMap<String, List<PackagedTypeSpec>>();
        generated.forEach(spec -> byName.computeIfAbsent(nameOf(spec), key -> new ArrayList<>()).add(spec));
        for (final var entry : byName.entrySet()) {
            if (entry.getValue().size() > 1) {
                throw new DuplicateGeneratedTypeException(entry.getKey(), entry.getValue().stream()
                        .map(spec -> describe(spec.getType().kind()))
                        .toList());
            }
        }
    }

    private void rejectAgainstExistingSources(final List<PackagedTypeSpec> generated) {
        for (final var spec : generated) {
            final var existing = scanner.locationOf(ClassName.bestGuess(nameOf(spec)));
            if (Files.isRegularFile(existing) && !isOurOwn(existing)) {
                throw new GeneratedTypeExistsException(nameOf(spec),
                        describe(spec.getType().kind()), existing);
            }
        }
    }

    private static boolean isOurOwn(final java.nio.file.Path source) {
        try {
            return Files.readString(source, StandardCharsets.UTF_8).contains(OUR_OWN);
        } catch (final IOException _) {
            // Unreadable says nothing either way, and a build should not fail over a file it cannot
            // open to answer a question about a name.
            return true;
        }
    }

    private static String nameOf(final PackagedTypeSpec spec) {
        return spec.getPackageName() + "." + spec.getType().name();
    }

    /**
     * JavaPoet spells its kinds in capitals, which reads as shouting in the middle of a sentence.
     */
    private static String describe(final TypeSpec.Kind kind) {
        return switch (kind) {
            case INTERFACE -> "an interface";
            case RECORD -> "a record";
            case ENUM -> "an enum";
            case ANNOTATION -> "an annotation";
            case CLASS -> "a class";
        };
    }

}
