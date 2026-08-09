/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.example.maven.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import wtf.metio.yosql.example.common.StatementShapes;
import wtf.metio.yosql.example.maven.jdbc.persistence.CallcenterRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.CompanyRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.ItemRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.PersonRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.SchemaRepository;
import wtf.metio.yosql.example.maven.jdbc.persistence.UserRepository;

import java.util.List;
import java.util.stream.Stream;

/**
 * Every shape of method the generator can write is written by one of these examples.
 *
 * <p>What counts as a shape, and which of them these repositories reach, is
 * {@link StatementShapes} — the Gradle example asks the same question of the same statements, and
 * one reading of the rules serves both. The assertion stays here so that a failure names the build
 * tool whose repositories lost the shape.</p>
 */
@DisplayName("the generated example repositories")
class GeneratedMatrixTest {

    private static final List<Class<?>> REPOSITORIES = List.of(
            CallcenterRepository.class, CompanyRepository.class, ItemRepository.class,
            PersonRepository.class, SchemaRepository.class, UserRepository.class);

    static Stream<String> combinations() {
        return StatementShapes.combinations().stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("combinations")
    @DisplayName("cover every shape a statement can take")
    void isCovered(final String combination) {
        final var covered = StatementShapes.coveredBy(REPOSITORIES);

        Assertions.assertTrue(covered.contains(combination),
                () -> "no example statement generates " + combination + ", so nothing runs that "
                        + "method against a database.\ncovered: " + covered);
    }

}
