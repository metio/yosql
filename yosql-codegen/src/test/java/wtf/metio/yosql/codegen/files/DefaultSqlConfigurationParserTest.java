/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlType;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultSqlConfigurationParser")
class DefaultSqlConfigurationParserTest {

    private DefaultSqlConfigurationParser parser;

    @BeforeEach
    void setUp() {
        parser = new DefaultSqlConfigurationParser();
    }

    @Test
    void shouldParseRepository() {
        final var yaml = """
                repository: com.example.MyRepository
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.repository().isPresent());
        assertEquals("com.example.MyRepository", config.repository().get());
    }

    @Test
    void shouldParseName() {
        final var yaml = """
                name: findItemByName
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.name().isPresent());
        assertEquals("findItemByName", config.name().get());
    }

    @Test
    void shouldParseDescription() {
        final var yaml = """
                description: some descriptive message about this statement
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.description().isPresent());
        assertEquals("some descriptive message about this statement", config.description().get());
    }

    @Test
    void shouldParseVendor() {
        final var yaml = """
                vendor: Postgres
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.vendor().isPresent());
        assertEquals("Postgres", config.vendor().get());
    }

    @Test
    void shouldParseTypeUnknown() {
        final var yaml = """
                type: UNKNOWN
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlType.UNKNOWN, config.type().get());
    }

    @Test
    void shouldParseTypeReading() {
        final var yaml = """
                type: READING
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlType.READING, config.type().get());
    }

    @Test
    void shouldParseTypeWriting() {
        final var yaml = """
                type: WRITING
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlType.WRITING, config.type().get());
    }

    @Test
    void shouldParseTypeCalling() {
        final var yaml = """
                type: CALLING
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlType.CALLING, config.type().get());
    }

    @Test
    void shouldParseTypeUnknownCaseInsensitive() {
        final var yaml = """
                type: unknown
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlType.UNKNOWN, config.type().get());
    }

    @Test
    void shouldParseTypeReadingCaseInsensitive() {
        final var yaml = """
                type: reading
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlType.READING, config.type().get());
    }

    @Test
    void shouldParseTypeWritingCaseInsensitive() {
        final var yaml = """
                type: writing
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlType.WRITING, config.type().get());
    }

    @Test
    void shouldParseTypeCallingCaseInsensitive() {
        final var yaml = """
                type: calling
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlType.CALLING, config.type().get());
    }

    @Test
    void shouldParseReturningModeNone() {
        final var yaml = """
                returningMode: NONE
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.NONE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeFirst() {
        final var yaml = """
                returningMode: SINGLE
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.SINGLE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeList() {
        final var yaml = """
                returningMode: MULTIPLE
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.MULTIPLE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeNoneCaseInsensitive() {
        final var yaml = """
                returningMode: none
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.NONE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeFirstCaseInsensitive() {
        final var yaml = """
                returningMode: single
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.SINGLE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeListCaseInsensitive() {
        final var yaml = """
                returningMode: multiple
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.MULTIPLE, config.returningMode().get());
    }

    @Test
    void shouldParseGenerateBlockingApiEnabled() {
        final var yaml = """
                generateBlockingApi: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateBlockingApi().isPresent());
        assertTrue(config.generateBlockingApi().get());
    }

    @Test
    void shouldParseGenerateBlockingApiDisabled() {
        final var yaml = """
                generateBlockingApi: false
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateBlockingApi().isPresent());
        assertFalse(config.generateBlockingApi().get());
    }

    @Test
    void shouldParseGenerateBatchApiEnabled() {
        final var yaml = """
                generateBatchApi: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateBatchApi().isPresent());
        assertTrue(config.generateBatchApi().get());
    }

    @Test
    void shouldParseGenerateBatchApiDisabled() {
        final var yaml = """
                generateBatchApi: false
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateBatchApi().isPresent());
        assertFalse(config.generateBatchApi().get());
    }

    @Test
    void shouldParseCatchAndRethrowEnabled() {
        final var yaml = """
                catchAndRethrow: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.catchAndRethrow().isPresent());
        assertTrue(config.catchAndRethrow().get());
    }

    @Test
    void shouldParseCatchAndRethrowDisabled() {
        final var yaml = """
                catchAndRethrow: false
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.catchAndRethrow().isPresent());
        assertFalse(config.catchAndRethrow().get());
    }

    @Test
    void shouldParseInjectConvertersEnabled() {
        final var yaml = """
                injectConverters: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.injectConverters().isPresent());
        assertTrue(config.injectConverters().get());
    }

    @Test
    void shouldParseInjectConvertersDisabled() {
        final var yaml = """
                injectConverters: false
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.injectConverters().isPresent());
        assertFalse(config.injectConverters().get());
    }

    @Test
    void shouldParseBlockingPrefix() {
        final var yaml = """
                blockingPrefix: prefix
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.blockingPrefix().isPresent());
        assertEquals("prefix", config.blockingPrefix().get());
    }

    @Test
    void shouldParseBlockingSuffix() {
        final var yaml = """
                blockingSuffix: suffix
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.blockingSuffix().isPresent());
        assertEquals("suffix", config.blockingSuffix().get());
    }

    @Test
    void shouldParseBatchPrefix() {
        final var yaml = """
                batchPrefix: prefix
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.batchPrefix().isPresent());
        assertEquals("prefix", config.batchPrefix().get());
    }

    @Test
    void shouldParseBatchSuffix() {
        final var yaml = """
                batchSuffix: suffix
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.batchSuffix().isPresent());
        assertEquals("suffix", config.batchSuffix().get());
    }

    @Test
    void shouldParseParameter() {
        final var yaml = """
                parameters:
                  - name: name
                    type: java.lang.String
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(1, config.parameters().size());
        assertAll(
                () -> assertEquals("name", config.parameters().get(0).name()),
                () -> assertEquals("java.lang.String", config.parameters().get(0).type()));
    }

    @Test
    void shouldParseParameters() {
        final var yaml = """
                parameters:
                  - name: string
                    type: java.lang.String
                  - name: bool
                    type: boolean
                  - name: number
                    type: int
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(3, config.parameters().size());
        assertAll(
                () -> assertEquals("string", config.parameters().get(0).name()),
                () -> assertEquals("java.lang.String", config.parameters().get(0).type()),
                () -> assertEquals("bool", config.parameters().get(1).name()),
                () -> assertEquals("boolean", config.parameters().get(1).type()),
                () -> assertEquals("number", config.parameters().get(2).name()),
                () -> assertEquals("int", config.parameters().get(2).type()));
    }

    @Test
    void shouldParseResultRowConverterAlias() {
        final var yaml = """
                resultRowConverter:
                  alias: converterAlias
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.resultRowConverter().isPresent());
        assertAll(
                () -> assertEquals("converterAlias", config.resultRowConverter().get().alias()),
                () -> assertEquals("", config.resultRowConverter().get().methodName()),
                () -> assertEquals("", config.resultRowConverter().get().converterType()),
                () -> assertEquals("", config.resultRowConverter().get().resultType()));
    }

    @Test
    void shouldParseResultRowConverterMethodName() {
        final var yaml = """
                resultRowConverter:
                  methodName: someMethod
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.resultRowConverter().isPresent());
        assertAll(
                () -> assertEquals("", config.resultRowConverter().get().alias()),
                () -> assertEquals("someMethod", config.resultRowConverter().get().methodName()),
                () -> assertEquals("", config.resultRowConverter().get().converterType()),
                () -> assertEquals("", config.resultRowConverter().get().resultType()));
    }

    @Test
    void shouldParseResultRowConverterConverterType() {
        final var yaml = """
                resultRowConverter:
                  converterType: com.example.MyConverter
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.resultRowConverter().isPresent());
        assertAll(
                () -> assertEquals("", config.resultRowConverter().get().alias()),
                () -> assertEquals("", config.resultRowConverter().get().methodName()),
                () -> assertEquals("com.example.MyConverter", config.resultRowConverter().get().converterType()),
                () -> assertEquals("", config.resultRowConverter().get().resultType()));
    }

    @Test
    void shouldParseResultRowConverterResultType() {
        final var yaml = """
                resultRowConverter:
                  resultType: com.example.MyResult
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.resultRowConverter().isPresent());
        assertAll(
                () -> assertEquals("", config.resultRowConverter().get().alias()),
                () -> assertEquals("", config.resultRowConverter().get().methodName()),
                () -> assertEquals("", config.resultRowConverter().get().converterType()),
                () -> assertEquals("com.example.MyResult", config.resultRowConverter().get().resultType()));
    }

    @Test
    void shouldParseResultRowConverter() {
        final var yaml = """
                resultRowConverter:
                  alias: converterAlias
                  methodName: someMethod
                  converterType: com.example.MyConverter
                  resultType: com.example.MyResult
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.resultRowConverter().isPresent());
        assertAll(
                () -> assertEquals("converterAlias", config.resultRowConverter().get().alias()),
                () -> assertEquals("someMethod", config.resultRowConverter().get().methodName()),
                () -> assertEquals("com.example.MyConverter", config.resultRowConverter().get().converterType()),
                () -> assertEquals("com.example.MyResult", config.resultRowConverter().get().resultType()));
    }

}
