/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class SqlParameterTest {

    @Test
    void mergeParametersFirst() {
        final List<SqlParameter> first = List.of(SqlParameter.builder().setName("first").build());
        final List<SqlParameter> second = List.of();

        final var merged = SqlParameter.mergeParameters(first, second);

        assertIterableEquals(first, merged);
    }

    @Test
    void mergeParametersSecond() {
        final List<SqlParameter> first = List.of();
        final List<SqlParameter> second = List.of(SqlParameter.builder().setName("second").build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertIterableEquals(second, merged);
    }

    @Test
    void mergeParametersMixed() {
        final List<SqlParameter> parameters = List.of(
                SqlParameter.builder().setName("first").build(),
                SqlParameter.builder().setName("second").build());
        final List<SqlParameter> first = List.of(SqlParameter.builder().setName("first").build());
        final List<SqlParameter> second = List.of(SqlParameter.builder().setName("second").build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertIterableEquals(parameters, merged);
    }

    @Test
    void mergeParametersMixedDuplicatedFirst() {
        final List<SqlParameter> parameters = List.of(
                SqlParameter.builder().setName("first").build(),
                SqlParameter.builder().setName("second").build());
        final List<SqlParameter> first = List.of(SqlParameter.builder().setName("first").build());

        final var merged = SqlParameter.mergeParameters(parameters, first);

        assertIterableEquals(parameters, merged);
    }

    @Test
    void mergeParametersMixedDuplicatedSecond() {
        final List<SqlParameter> parameters = List.of(
                SqlParameter.builder().setName("first").build(),
                SqlParameter.builder().setName("second").build());
        final List<SqlParameter> second = List.of(SqlParameter.builder().setName("second").build());

        final var merged = SqlParameter.mergeParameters(parameters, second);

        assertIterableEquals(parameters, merged);
    }

    @Test
    void mergeParametersDuplicated() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .setType("java.lang.String")
                .setIndices(new int[]{1})
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .setType("java.lang.String")
                .setIndices(new int[]{1})
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElse(""));
        assertEquals("java.lang.String", merged.get(0).type().orElse(""));
    }

    @Test
    void mergeParametersDuplicatedNamesMissingTypeFirst() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .setIndices(new int[]{1})
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .setType("java.lang.String")
                .setIndices(new int[]{1})
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElse(""));
        assertEquals("java.lang.String", merged.get(0).type().orElse(""));
    }

    @Test
    void mergeParametersDuplicatedNamesMissingTypeSecond() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .setType("java.lang.String")
                .setIndices(new int[]{1})
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .setIndices(new int[]{1})
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElse(""));
        assertEquals("java.lang.String", merged.get(0).type().orElse(""));
    }

    @Test
    void mergeParametersDuplicatedNamesMissingIndicesFirst() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .setType("java.lang.String")
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .setType("java.lang.String")
                .setIndices(new int[]{7})
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElse(""));
        assertEquals("java.lang.String", merged.get(0).type().orElse(""));
        assertEquals(7, merged.get(0).indices().orElseThrow()[0]);
    }

    @Test
    void mergeParametersDuplicatedNamesMissingIndicesSecond() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .setType("java.lang.String")
                .setIndices(new int[]{7})
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .setType("java.lang.String")
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElse(""));
        assertEquals("java.lang.String", merged.get(0).type().orElse(""));
        assertEquals(7, merged.get(0).indices().orElseThrow()[0]);
    }

    @Test
    void mergeParametersDuplicatedNamesMissingVariantFirst() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .setVariant(SqlParameterVariant.IN)
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElseThrow());
        assertEquals(SqlParameterVariant.IN, merged.get(0).variant().orElseThrow());
    }

    @Test
    void mergeParametersDuplicatedNamesMissingVariantSecond() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .setVariant(SqlParameterVariant.IN)
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElseThrow());
        assertEquals(SqlParameterVariant.IN, merged.get(0).variant().orElseThrow());
    }

    @Test
    void mergeParametersDuplicatedNamesMissingSqlTypeFirst() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .setSqlType(1)
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElseThrow());
        assertEquals(1, merged.get(0).sqlType().orElseThrow());
    }

    @Test
    void mergeParametersDuplicatedNamesMissingSqlTypeSecond() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .setSqlType(1)
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElseThrow());
        assertEquals(1, merged.get(0).sqlType().orElseThrow());
    }

    @Test
    void mergeParametersDuplicatedNamesMissingScaleFirst() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .setScale(2)
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElseThrow());
        assertEquals(2, merged.get(0).scale().orElseThrow());
    }

    @Test
    void mergeParametersDuplicatedNamesMissingScaleSecond() {
        final List<SqlParameter> first = List.of(SqlParameter.builder()
                .setName("name")
                .setScale(2)
                .build());
        final List<SqlParameter> second = List.of(SqlParameter.builder()
                .setName("name")
                .build());

        final var merged = SqlParameter.mergeParameters(first, second);

        assertEquals(1, merged.size());
        assertEquals("name", merged.get(0).name().orElseThrow());
        assertEquals(2, merged.get(0).scale().orElseThrow());
    }

}
