/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.immutables;

import org.immutables.value.Value;
import wtf.metio.yosql.internals.jdk.Buckets;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlType;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Encapsulates everything we know about a single SQL statement.
 */
@Value.Immutable
public interface SqlStatement {

    //region builders

    static ImmutableSqlStatement.SourcePathBuildStage builder() {
        return ImmutableSqlStatement.builder();
    }

    //endregion

    static Collector<SqlStatement, ?, Map<String, List<SqlStatement>>> groupByName() {
        return Collectors.groupingBy(statement -> statement.getConfiguration().name());
    }

    static Stream<ResultRowConverter> resultConverters(
            final List<SqlStatement> statements,
            final ResultRowConverter defaultConverter) {
        return statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(SqlStatement::requiresConverter)
                .map(config -> config.resultRowConverter().orElse(defaultConverter))
                .distinct();
    }

    private static boolean requiresConverter(final SqlConfiguration configuration) {
        return ReturningMode.NONE != configuration.returningMode();
    }


    Path getSourcePath();

    SqlConfiguration getConfiguration();

    String getRawStatement();

    @Value.Lazy
    default String getName() {
        return getConfiguration().name();
    }

    @Value.Lazy
    default String getRepository() {
        return getConfiguration().repository();
    }

    @Value.Lazy
    default boolean isReading() {
        return SqlType.READING == getConfiguration().type();
    }

    @Value.Lazy
    default boolean isWriting() {
        return SqlType.WRITING == getConfiguration().type();
    }

    @Value.Lazy
    default boolean isCalling() {
        return SqlType.CALLING == getConfiguration().type();
    }

    @Value.Lazy
    default boolean shouldGenerateBatchWriteAPI() {
        return isWriting() &&
                getConfiguration().generateBatchApi().orElse(Boolean.FALSE) &&
                Buckets.hasEntries(getConfiguration().parameters());
    }

    @Value.Lazy
    default boolean shouldGenerateBlockingReadAPI() {
        return isReading() && getConfiguration().generateBlockingApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateBlockingCallAPI() {
        return isCalling() && getConfiguration().generateBlockingApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateBlockingWriteAPI() {
        return isWriting() && getConfiguration().generateBlockingApi().orElse(Boolean.FALSE);
    }

}
