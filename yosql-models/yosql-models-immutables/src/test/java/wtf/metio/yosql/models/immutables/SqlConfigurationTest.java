/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.immutables;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.configuration.*;

import java.util.List;

@DisplayName("SqlConfiguration")
class SqlConfigurationTest {

    @Test
    void mergeRepositoryFirst() {
        final var first = SqlConfiguration.builder()
                .setRepository("com.example.Repository")
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.repository(), merged.repository());
    }

    @Test
    void mergeRepositorySecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setRepository("com.example.Repository")
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.repository(), merged.repository());
    }

    @Test
    void mergeRepositoryMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.repository().isEmpty());
    }

    @Test
    void mergeRepositoryInterfaceFirst() {
        final var first = SqlConfiguration.builder()
                .setRepositoryInterface("com.example.Repository")
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.repositoryInterface(), merged.repositoryInterface());
    }

    @Test
    void mergeRepositoryInterfaceSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setRepositoryInterface("com.example.Repository")
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.repositoryInterface(), merged.repositoryInterface());
    }

    @Test
    void mergeRepositoryInterfaceMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.repositoryInterface().isEmpty());
    }

    @Test
    void mergeNameFirst() {
        final var first = SqlConfiguration.builder()
                .setName("name")
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.name(), merged.name());
    }

    @Test
    void mergeNameSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setName("name")
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.name(), merged.name());
    }

    @Test
    void mergeNameMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.name().isEmpty());
    }

    @Test
    void mergeDescriptionFirst() {
        final var first = SqlConfiguration.builder()
                .setDescription("some description")
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.description(), merged.description());
    }

    @Test
    void mergeDescriptionSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setDescription("some description")
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.description(), merged.description());
    }

    @Test
    void mergeDescriptionMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.description().isEmpty());
    }

    @Test
    void mergeVendorFirst() {
        final var first = SqlConfiguration.builder()
                .setVendor("H2 Database")
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.vendor(), merged.vendor());
    }

    @Test
    void mergeVendorSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setVendor("H2 Database")
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.vendor(), merged.vendor());
    }

    @Test
    void mergeVendorMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.vendor().isEmpty());
    }

    @Test
    void mergeTypeFirst() {
        final var first = SqlConfiguration.builder()
                .setType(SqlStatementType.READING)
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.type(), merged.type());
    }

    @Test
    void mergeTypeSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setType(SqlStatementType.READING)
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.type(), merged.type());
    }

    @Test
    void mergeTypeMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.type().isEmpty());
    }

    @Test
    void mergeReturningModeFirst() {
        final var first = SqlConfiguration.builder()
                .setReturningMode(ReturningMode.SINGLE)
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.returningMode(), merged.returningMode());
    }

    @Test
    void mergeReturningModeSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setReturningMode(ReturningMode.SINGLE)
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.returningMode(), merged.returningMode());
    }

    @Test
    void mergeReturningModeMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.returningMode().isEmpty());
    }

    @Test
    void mergeStandardPrefixFirst() {
        final var first = SqlConfiguration.builder()
                .setStandardPrefix("prefix")
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.standardPrefix(), merged.standardPrefix());
    }

    @Test
    void mergeStandardPrefixSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setStandardPrefix("prefix")
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.standardPrefix(), merged.standardPrefix());
    }

    @Test
    void mergeStandardPrefixMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.standardPrefix().isEmpty());
    }

    @Test
    void mergeStandardSuffixFirst() {
        final var first = SqlConfiguration.builder()
                .setStandardSuffix("suffix")
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.standardSuffix(), merged.standardSuffix());
    }

    @Test
    void mergeStandardSuffixSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setStandardSuffix("suffix")
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.standardSuffix(), merged.standardSuffix());
    }

    @Test
    void mergeStandardSuffixMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.standardSuffix().isEmpty());
    }

    @Test
    void mergeBatchPrefixFirst() {
        final var first = SqlConfiguration.builder()
                .setBatchPrefix("prefix")
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.batchPrefix(), merged.batchPrefix());
    }

    @Test
    void mergeBatchPrefixSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setBatchPrefix("prefix")
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.batchPrefix(), merged.batchPrefix());
    }

    @Test
    void mergeBatchPrefixMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.batchPrefix().isEmpty());
    }

    @Test
    void mergeBatchSuffixFirst() {
        final var first = SqlConfiguration.builder()
                .setBatchSuffix("suffix")
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.batchSuffix(), merged.batchSuffix());
    }

    @Test
    void mergeBatchSuffixSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setBatchSuffix("suffix")
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.batchSuffix(), merged.batchSuffix());
    }

    @Test
    void mergeBatchSuffixMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.batchSuffix().isEmpty());
    }

    @Test
    void mergeGenerateStandardApiFirst() {
        final var first = SqlConfiguration.builder()
                .setGenerateStandardApi(true)
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.generateStandardApi(), merged.generateStandardApi());
    }

    @Test
    void mergeGenerateStandardApiSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setGenerateStandardApi(true)
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.generateStandardApi(), merged.generateStandardApi());
    }

    @Test
    void mergeGenerateStandardApiMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.generateStandardApi().isEmpty());
    }

    @Test
    void mergeGenerateBatchApiFirst() {
        final var first = SqlConfiguration.builder()
                .setGenerateBatchApi(true)
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.generateBatchApi(), merged.generateBatchApi());
    }

    @Test
    void mergeGenerateBatchApiSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setGenerateBatchApi(true)
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.generateBatchApi(), merged.generateBatchApi());
    }

    @Test
    void mergeGenerateBatchApiMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.generateStandardApi().isEmpty());
    }

    @Test
    void mergeUsePreparedStatementFirst() {
        final var first = SqlConfiguration.builder()
                .setUsePreparedStatement(true)
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.usePreparedStatement(), merged.usePreparedStatement());
    }

    @Test
    void mergeUsePreparedStatementSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setUsePreparedStatement(true)
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.usePreparedStatement(), merged.usePreparedStatement());
    }

    @Test
    void mergeUsePreparedStatementMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.usePreparedStatement().isEmpty());
    }

    @Test
    void mergeCatchAndRethrowFirst() {
        final var first = SqlConfiguration.builder()
                .setCatchAndRethrow(true)
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.catchAndRethrow(), merged.catchAndRethrow());
    }

    @Test
    void mergeCatchAndRethrowSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setCatchAndRethrow(true)
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.catchAndRethrow(), merged.catchAndRethrow());
    }

    @Test
    void mergeCatchAndRethrowMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.catchAndRethrow().isEmpty());
    }

    @Test
    void mergeThrowOnMultipleResultsForSingleFirst() {
        final var first = SqlConfiguration.builder()
                .setThrowOnMultipleResultsForSingle(true)
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.throwOnMultipleResultsForSingle(), merged.throwOnMultipleResultsForSingle());
    }

    @Test
    void mergeThrowOnMultipleResultsForSingleSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setThrowOnMultipleResultsForSingle(true)
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.throwOnMultipleResultsForSingle(), merged.throwOnMultipleResultsForSingle());
    }

    @Test
    void mergeThrowOnMultipleResultsForSingleMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.throwOnMultipleResultsForSingle().isEmpty());
    }

    @Test
    void mergeWritesReturnUpdateCountFirst() {
        final var first = SqlConfiguration.builder()
                .setWritesReturnUpdateCount(true)
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.writesReturnUpdateCount(), merged.writesReturnUpdateCount());
    }

    @Test
    void mergeWritesReturnUpdateCountSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setWritesReturnUpdateCount(true)
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.writesReturnUpdateCount(), merged.writesReturnUpdateCount());
    }

    @Test
    void mergeWritesReturnUpdateCountMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.writesReturnUpdateCount().isEmpty());
    }

    @Test
    void mergeResultRowConverterFirst() {
        final var first = SqlConfiguration.builder()
                .setResultRowConverter(ResultRowConverter.builder().build())
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.resultRowConverter(), merged.resultRowConverter());
    }

    @Test
    void mergeResultRowConverterSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .setResultRowConverter(ResultRowConverter.builder().build())
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.resultRowConverter(), merged.resultRowConverter());
    }

    @Test
    void mergeResultRowConverterMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.resultRowConverter().isEmpty());
    }

    @Test
    void mergeParametersFirst() {
        final var first = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder().setName("first").build())
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.parameters(), merged.parameters());
    }

    @Test
    void mergeParametersSecond() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder().setName("second").build())
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.parameters(), merged.parameters());
    }

    @Test
    void mergeParametersMixed() {
        final var parameters = List.of(
                SqlParameter.builder().setName("first").build(),
                SqlParameter.builder().setName("second").build());
        final var first = SqlConfiguration.builder()
                .addParameters(parameters.get(0))
                .build();
        final var second = SqlConfiguration.builder()
                .addParameters(parameters.get(1))
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(parameters, merged.parameters());
    }

    @Test
    void mergeParametersMixedDuplicatedFirst() {
        final List<SqlParameter> parameters = List.of(
                SqlParameter.builder().setName("first").build(),
                SqlParameter.builder().setName("second").build());
        final var first = SqlConfiguration.builder()
                .addAllParameters(parameters)
                .build();
        final var second = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder().setName("second").build())
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(parameters, merged.parameters());
    }

    @Test
    void mergeParametersMixedDuplicatedSecond() {
        final List<SqlParameter> parameters = List.of(
                SqlParameter.builder().setName("first").build(),
                SqlParameter.builder().setName("second").build());
        final var first = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder().setName("first").build())
                .build();
        final var second = SqlConfiguration.builder()
                .addAllParameters(parameters)
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(parameters, merged.parameters());
    }

    @Test
    void mergeParametersDuplicated() {
        final var first = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder()
                        .setName("name")
                        .setType("java.lang.String")
                        .setIndices(new int[]{1})
                        .build())
                .build();
        final var second = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder()
                        .setName("name")
                        .setType("java.lang.String")
                        .setIndices(new int[]{1})
                        .build())
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(1, merged.parameters().size());
        Assertions.assertEquals("name", merged.parameters().get(0).name().orElse(""));
        Assertions.assertEquals("java.lang.String", merged.parameters().get(0).type().orElse(""));
    }

    @Test
    void mergeParametersDuplicatedNamesMissingTypeFirst() {
        final var first = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder()
                        .setName("name")
                        .setIndices(new int[]{1})
                        .build())
                .build();
        final var second = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder()
                        .setName("name")
                        .setType("java.lang.String")
                        .setIndices(new int[]{1})
                        .build())
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(1, merged.parameters().size());
        Assertions.assertEquals("name", merged.parameters().get(0).name().orElse(""));
        Assertions.assertEquals("java.lang.String", merged.parameters().get(0).type().orElse(""));
    }

    @Test
    void mergeParametersDuplicatedNamesMissingTypeSecond() {
        final var first = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder()
                        .setName("name")
                        .setType("java.lang.String")
                        .setIndices(new int[]{1})
                        .build())
                .build();
        final var second = SqlConfiguration.builder()
                .addParameters(SqlParameter.builder()
                        .setName("name")
                        .setIndices(new int[]{1})
                        .build())
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(1, merged.parameters().size());
        Assertions.assertEquals("name", merged.parameters().get(0).name().orElse(""));
        Assertions.assertEquals("java.lang.String", merged.parameters().get(0).type().orElse(""));
    }

    @Test
    void mergeParametersMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.parameters().isEmpty());
    }

    @Test
    void mergeAnnotationsMissing() {
        final var first = SqlConfiguration.builder().build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertTrue(merged.annotations().isEmpty());
    }

    @Test
    void mergeAnnotationsFirst() {
        final var first = SqlConfiguration.builder()
                .addAnnotations(Annotation.builder().setType("com.example.MyAnnotation").build())
                .build();
        final var second = SqlConfiguration.builder().build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(first.annotations(), merged.annotations());
    }

    @Test
    void mergeAnnotationsSecond() {
        final var first = SqlConfiguration.builder()
                .build();
        final var second = SqlConfiguration.builder()
                .addAnnotations(Annotation.builder().setType("com.example.MyAnnotation").build())
                .build();

        final var merged = SqlConfiguration.merge(first, second);

        Assertions.assertEquals(second.annotations(), merged.annotations());
    }

    @Test
    void standardNameEmpty() {
        final var config = SqlConfiguration.builder()
                .build();

        Assertions.assertEquals("", config.standardName());
    }

    @Test
    void standardNameWithoutAffixes() {
        final var config = SqlConfiguration.builder()
                .setName("test")
                .build();

        Assertions.assertEquals("test", config.standardName());
    }

    @Test
    void standardNameWithPrefix() {
        final var config = SqlConfiguration.builder()
                .setName("test")
                .setStandardPrefix("prefix")
                .build();

        Assertions.assertEquals("prefixTest", config.standardName());
    }

    @Test
    void standardNameWithSuffix() {
        final var config = SqlConfiguration.builder()
                .setName("test")
                .setStandardSuffix("suffix")
                .build();

        Assertions.assertEquals("testSuffix", config.standardName());
    }

    @Test
    void standardNameWithAffixes() {
        final var config = SqlConfiguration.builder()
                .setName("test")
                .setStandardPrefix("prefix")
                .setStandardSuffix("suffix")
                .build();

        Assertions.assertEquals("prefixTestSuffix", config.standardName());
    }

    @Test
    void standardNameWithAffixesUpperCase() {
        final var config = SqlConfiguration.builder()
                .setName("test")
                .setStandardPrefix("Prefix")
                .setStandardSuffix("Suffix")
                .build();

        Assertions.assertEquals("PrefixTestSuffix", config.standardName());
    }

}
