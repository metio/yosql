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
import wtf.metio.yosql.models.constants.sql.ReturningMode;
import wtf.metio.yosql.models.constants.sql.SqlType;

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
        assertEquals("com.example.MyRepository", config.repository());
    }

    @Test
    void shouldParseName() {
        final var yaml = """
                name: findItemByName
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("findItemByName", config.name());
    }

    @Test
    void shouldParseDescription() {
        final var yaml = """
                description: some descriptive message about this statement
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("some descriptive message about this statement", config.description());
    }

    @Test
    void shouldParseVendor() {
        final var yaml = """
                vendor: Postgres
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("Postgres", config.vendor());
    }

    @Test
    void shouldParseTypeUnknown() {
        final var yaml = """
                type: UNKNOWN
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(SqlType.UNKNOWN, config.type());
    }

    @Test
    void shouldParseTypeReading() {
        final var yaml = """
                type: READING
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(SqlType.READING, config.type());
    }

    @Test
    void shouldParseTypeWriting() {
        final var yaml = """
                type: WRITING
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(SqlType.WRITING, config.type());
    }

    @Test
    void shouldParseTypeCalling() {
        final var yaml = """
                type: CALLING
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(SqlType.CALLING, config.type());
    }

    @Test
    void shouldParseTypeUnknownCaseInsensitive() {
        final var yaml = """
                type: unknown
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(SqlType.UNKNOWN, config.type());
    }

    @Test
    void shouldParseTypeReadingCaseInsensitive() {
        final var yaml = """
                type: reading
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(SqlType.READING, config.type());
    }

    @Test
    void shouldParseTypeWritingCaseInsensitive() {
        final var yaml = """
                type: writing
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(SqlType.WRITING, config.type());
    }

    @Test
    void shouldParseTypeCallingCaseInsensitive() {
        final var yaml = """
                type: calling
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(SqlType.CALLING, config.type());
    }

    @Test
    void shouldParseReturningModeNone() {
        final var yaml = """
                returningMode: NONE
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(ReturningMode.NONE, config.returningMode());
    }

    @Test
    void shouldParseReturningModeOne() {
        final var yaml = """
                returningMode: ONE
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(ReturningMode.ONE, config.returningMode());
    }

    @Test
    void shouldParseReturningModeFirst() {
        final var yaml = """
                returningMode: FIRST
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(ReturningMode.FIRST, config.returningMode());
    }

    @Test
    void shouldParseReturningModeList() {
        final var yaml = """
                returningMode: LIST
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(ReturningMode.LIST, config.returningMode());
    }

    @Test
    void shouldParseReturningModeNoneCaseInsensitive() {
        final var yaml = """
                returningMode: none
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(ReturningMode.NONE, config.returningMode());
    }

    @Test
    void shouldParseReturningModeOneCaseInsensitive() {
        final var yaml = """
                returningMode: one
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(ReturningMode.ONE, config.returningMode());
    }

    @Test
    void shouldParseReturningModeFirstCaseInsensitive() {
        final var yaml = """
                returningMode: first
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(ReturningMode.FIRST, config.returningMode());
    }

    @Test
    void shouldParseReturningModeListCaseInsensitive() {
        final var yaml = """
                returningMode: list
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals(ReturningMode.LIST, config.returningMode());
    }

    @Test
    void shouldParseGenerateBlockingApiEnabled() {
        final var yaml = """
                generateBlockingApi: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateBlockingApi().get());
    }

    @Test
    void shouldParseGenerateBlockingApiDisabled() {
        final var yaml = """
                generateBlockingApi: false
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.generateBlockingApi().get());
    }

    @Test
    void shouldParseGenerateBatchApiEnabled() {
        final var yaml = """
                generateBatchApi: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateBatchApi().get());
    }

    @Test
    void shouldParseGenerateBatchApiDisabled() {
        final var yaml = """
                generateBatchApi: false
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.generateBatchApi().get());
    }

    @Test
    void shouldParseGenerateMutinyApiEnabled() {
        final var yaml = """
                generateMutinyApi: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateMutinyApi().get());
    }

    @Test
    void shouldParseGenerateMutinyApiDisabled() {
        final var yaml = """
                generateMutinyApi: false
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.generateMutinyApi().get());
    }

    @Test
    void shouldParseGenerateReactorApiEnabled() {
        final var yaml = """
                generateReactorApi: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateReactorApi().get());
    }

    @Test
    void shouldParseGenerateReactorApiDisabled() {
        final var yaml = """
                generateReactorApi: false
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.generateReactorApi().get());
    }

    @Test
    void shouldParseGenerateRxJavaApiEnabled() {
        final var yaml = """
                generateRxJavaApi: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateRxJavaApi().get());
    }

    @Test
    void shouldParseGenerateRxJavaApiDisabled() {
        final var yaml = """
                generateRxJavaApi: false
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.generateRxJavaApi().get());
    }

    @Test
    void shouldParseGenerateStreamApiEnabled() {
        final var yaml = """
                generateStreamApi: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateStreamApi().get());
    }

    @Test
    void shouldParseGenerateStreamApiDisabled() {
        final var yaml = """
                generateStreamApi: false
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.generateStreamApi().get());
    }

    @Test
    void shouldParseCatchAndRethrowEnabled() {
        final var yaml = """
                catchAndRethrow: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.catchAndRethrow().get());
    }

    @Test
    void shouldParseCatchAndRethrowDisabled() {
        final var yaml = """
                catchAndRethrow: false
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.catchAndRethrow().get());
    }

    @Test
    void shouldParseInjectConvertersEnabled() {
        final var yaml = """
                injectConverters: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.injectConverters().get());
    }

    @Test
    void shouldParseInjectConvertersDisabled() {
        final var yaml = """
                injectConverters: false
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.injectConverters().get());
    }

    @Test
    void shouldParseBlockingPrefix() {
        final var yaml = """
                blockingPrefix: prefix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("prefix", config.blockingPrefix());
    }

    @Test
    void shouldParseBlockingSuffix() {
        final var yaml = """
                blockingSuffix: suffix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("suffix", config.blockingSuffix());
    }

    @Test
    void shouldParseBatchPrefix() {
        final var yaml = """
                batchPrefix: prefix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("prefix", config.batchPrefix());
    }

    @Test
    void shouldParseBatchSuffix() {
        final var yaml = """
                batchSuffix: suffix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("suffix", config.batchSuffix());
    }

    @Test
    void shouldParseMutinyPrefix() {
        final var yaml = """
                mutinyPrefix: prefix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("prefix", config.mutinyPrefix());
    }

    @Test
    void shouldParseMutinySuffix() {
        final var yaml = """
                mutinySuffix: suffix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("suffix", config.mutinySuffix());
    }

    @Test
    void shouldParseReactorPrefix() {
        final var yaml = """
                reactorPrefix: prefix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("prefix", config.reactorPrefix());
    }

    @Test
    void shouldParseReactorSuffix() {
        final var yaml = """
                reactorSuffix: suffix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("suffix", config.reactorSuffix());
    }

    @Test
    void shouldParseRxJavaPrefix() {
        final var yaml = """
                rxJavaPrefix: prefix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("prefix", config.rxJavaPrefix());
    }

    @Test
    void shouldParseRxJavaSuffix() {
        final var yaml = """
                rxJavaSuffix: suffix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("suffix", config.rxJavaSuffix());
    }

    @Test
    void shouldParseStreamPrefix() {
        final var yaml = """
                streamPrefix: prefix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("prefix", config.streamPrefix());
    }

    @Test
    void shouldParseStreamSuffix() {
        final var yaml = """
                streamSuffix: suffix
                """;
        final var config = parser.parseConfig(yaml);
        assertEquals("suffix", config.streamSuffix());
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