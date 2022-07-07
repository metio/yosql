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
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlStatementType;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultSqlConfigurationParser")
class DefaultSqlConfigurationParserTest {

    private DefaultSqlConfigurationParser parser;

    @BeforeEach
    void setUp() {
        parser = new DefaultSqlConfigurationParser(OrchestrationObjectMother.executionErrors());
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
    void shouldParseTypeReading() {
        final var yaml = """
                type: READING
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlStatementType.READING, config.type().get());
    }

    @Test
    void shouldParseTypeWriting() {
        final var yaml = """
                type: WRITING
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlStatementType.WRITING, config.type().get());
    }

    @Test
    void shouldParseTypeCalling() {
        final var yaml = """
                type: CALLING
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlStatementType.CALLING, config.type().get());
    }

    @Test
    void shouldParseTypeReadingCaseInsensitive() {
        final var yaml = """
                type: reading
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlStatementType.READING, config.type().get());
    }

    @Test
    void shouldParseTypeWritingCaseInsensitive() {
        final var yaml = """
                type: writing
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlStatementType.WRITING, config.type().get());
    }

    @Test
    void shouldParseTypeCallingCaseInsensitive() {
        final var yaml = """
                type: calling
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.type().isPresent());
        assertEquals(SqlStatementType.CALLING, config.type().get());
    }

    @Test
    void shouldParseReturningModeNone() {
        final var yaml = """
                returning: NONE
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.NONE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeFirst() {
        final var yaml = """
                returning: SINGLE
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.SINGLE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeList() {
        final var yaml = """
                returning: MULTIPLE
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.MULTIPLE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeNoneCaseInsensitive() {
        final var yaml = """
                returning: none
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.NONE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeFirstCaseInsensitive() {
        final var yaml = """
                returning: single
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.SINGLE, config.returningMode().get());
    }

    @Test
    void shouldParseReturningModeListCaseInsensitive() {
        final var yaml = """
                returning: multiple
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.returningMode().isPresent());
        assertEquals(ReturningMode.MULTIPLE, config.returningMode().get());
    }

    @Test
    void shouldParseGenerateStandardApiEnabled() {
        final var yaml = """
                generateStandardApi: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateStandardApi().isPresent());
        assertTrue(config.generateStandardApi().get());
    }

    @Test
    void shouldParseGenerateStandardApiDisabled() {
        final var yaml = """
                generateStandardApi: false
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.generateStandardApi().isPresent());
        assertFalse(config.generateStandardApi().get());
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
    void shouldParseThrowOnMultipleResultsForSingleEnabled() {
        final var yaml = """
                throwOnMultipleResultsForSingle: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.throwOnMultipleResultsForSingle().isPresent());
        assertTrue(config.throwOnMultipleResultsForSingle().get());
    }

    @Test
    void shouldParseThrowOnMultipleResultsForSingleDisabled() {
        final var yaml = """
                throwOnMultipleResultsForSingle: false
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.throwOnMultipleResultsForSingle().isPresent());
        assertFalse(config.throwOnMultipleResultsForSingle().get());
    }

    @Test
    void shouldParseWritesReturnUpdateCountEnabled() {
        final var yaml = """
                writesReturnUpdateCount: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.writesReturnUpdateCount().isPresent());
        assertTrue(config.writesReturnUpdateCount().get());
    }

    @Test
    void shouldParseWritesReturnUpdateCountDisabled() {
        final var yaml = """
                writesReturnUpdateCount: false
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.writesReturnUpdateCount().isPresent());
        assertFalse(config.writesReturnUpdateCount().get());
    }

    @Test
    void shouldParseUsePreparedStatementEnabled() {
        final var yaml = """
                usePreparedStatement: true
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.usePreparedStatement().isPresent());
        assertTrue(config.usePreparedStatement().get());
    }

    @Test
    void shouldParseUsePreparedStatementDisabled() {
        final var yaml = """
                usePreparedStatement: false
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.usePreparedStatement().isPresent());
        assertFalse(config.usePreparedStatement().get());
    }

    @Test
    void shouldParseStandardPrefix() {
        final var yaml = """
                standardPrefix: prefix
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.standardPrefix().isPresent());
        assertEquals("prefix", config.standardPrefix().get());
    }

    @Test
    void shouldParseStandardSuffix() {
        final var yaml = """
                standardSuffix: suffix
                """;
        final var config = parser.parseConfig(yaml);
        assertTrue(config.standardSuffix().isPresent());
        assertEquals("suffix", config.standardSuffix().get());
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
                () -> assertEquals("name", config.parameters().get(0).name().get()),
                () -> assertEquals("java.lang.String", config.parameters().get(0).type().get()));
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
                () -> assertEquals("string", config.parameters().get(0).name().get()),
                () -> assertEquals("java.lang.String", config.parameters().get(0).type().get()),
                () -> assertEquals("bool", config.parameters().get(1).name().get()),
                () -> assertEquals("boolean", config.parameters().get(1).type().get()),
                () -> assertEquals("number", config.parameters().get(2).name().get()),
                () -> assertEquals("int", config.parameters().get(2).type().get()));
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
                () -> assertEquals("converterAlias", config.resultRowConverter().get().alias().get()),
                () -> assertTrue(config.resultRowConverter().get().methodName().isEmpty()),
                () -> assertTrue(config.resultRowConverter().get().converterType().isEmpty()),
                () -> assertTrue(config.resultRowConverter().get().resultType().isEmpty()));
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
                () -> assertTrue(config.resultRowConverter().get().alias().isEmpty()),
                () -> assertEquals("someMethod", config.resultRowConverter().get().methodName().get()),
                () -> assertTrue(config.resultRowConverter().get().converterType().isEmpty()),
                () -> assertTrue(config.resultRowConverter().get().resultType().isEmpty()));
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
                () -> assertTrue(config.resultRowConverter().get().alias().isEmpty()),
                () -> assertTrue(config.resultRowConverter().get().methodName().isEmpty()),
                () -> assertEquals("com.example.MyConverter", config.resultRowConverter().get().converterType().get()),
                () -> assertTrue(config.resultRowConverter().get().resultType().isEmpty()));
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
                () -> assertTrue(config.resultRowConverter().get().alias().isEmpty()),
                () -> assertTrue(config.resultRowConverter().get().methodName().isEmpty()),
                () -> assertTrue(config.resultRowConverter().get().converterType().isEmpty()),
                () -> assertEquals("com.example.MyResult", config.resultRowConverter().get().resultType().get()));
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
                () -> assertEquals("converterAlias", config.resultRowConverter().get().alias().get()),
                () -> assertEquals("someMethod", config.resultRowConverter().get().methodName().get()),
                () -> assertEquals("com.example.MyConverter", config.resultRowConverter().get().converterType().get()),
                () -> assertEquals("com.example.MyResult", config.resultRowConverter().get().resultType().get()));
    }

    @Test
    void shouldParseAnnotation() {
        final var yaml = """
                annotations:
                  - type: com.example.MyAnnotation
                    members:
                      - key: some
                        value: here
                        type: java.lang.String
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.annotations().isEmpty());
        assertAll("annotation",
                () -> assertEquals("com.example.MyAnnotation", config.annotations().get(0).type()),
                () -> assertAll("members",
                        () -> assertFalse(config.annotations().get(0).members().isEmpty()),
                        () -> assertEquals("some", config.annotations().get(0).members().get(0).key()),
                        () -> assertEquals("here", config.annotations().get(0).members().get(0).value()),
                        () -> assertEquals("java.lang.String", config.annotations().get(0).members().get(0).type())));
    }

    @Test
    void shouldParseAnnotations() {
        final var yaml = """
                annotations:
                  - type: com.example.MyAnnotation
                    members:
                      - key: some
                        value: here
                        type: java.lang.String
                  - type: com.example.OtherAnnotation
                    members:
                      - key: another
                        value: 12345
                        type: int
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.annotations().isEmpty());
        assertAll("annotation",
                () -> assertEquals("com.example.MyAnnotation", config.annotations().get(0).type()),
                () -> assertEquals("com.example.OtherAnnotation", config.annotations().get(1).type()),
                () -> assertAll("members",
                        () -> assertFalse(config.annotations().get(0).members().isEmpty()),
                        () -> assertEquals("some", config.annotations().get(0).members().get(0).key()),
                        () -> assertEquals("here", config.annotations().get(0).members().get(0).value()),
                        () -> assertEquals("java.lang.String", config.annotations().get(0).members().get(0).type()),
                        () -> assertEquals("another", config.annotations().get(1).members().get(0).key()),
                        () -> assertEquals("12345", config.annotations().get(1).members().get(0).value()),
                        () -> assertEquals("int", config.annotations().get(1).members().get(0).type())));
    }

    @Test
    void shouldParseAnnotationMembers() {
        final var yaml = """
                annotations:
                  - type: com.example.MyAnnotation
                    members:
                      - key: some
                        value: here
                        type: java.lang.String
                      - key: another
                        value: 12345
                        type: int
                """;
        final var config = parser.parseConfig(yaml);
        assertFalse(config.annotations().isEmpty());
        assertAll("annotation",
                () -> assertEquals("com.example.MyAnnotation", config.annotations().get(0).type()),
                () -> assertAll("members",
                        () -> assertFalse(config.annotations().get(0).members().isEmpty()),
                        () -> assertEquals("some", config.annotations().get(0).members().get(0).key()),
                        () -> assertEquals("here", config.annotations().get(0).members().get(0).value()),
                        () -> assertEquals("java.lang.String", config.annotations().get(0).members().get(0).type()),
                        () -> assertEquals("another", config.annotations().get(0).members().get(1).key()),
                        () -> assertEquals("12345", config.annotations().get(0).members().get(1).value()),
                        () -> assertEquals("int", config.annotations().get(0).members().get(1).type())));
    }

}
