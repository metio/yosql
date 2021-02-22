/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.immutables;

import org.immutables.value.Value;
import wtf.metio.yosql.internals.jdk.Buckets;
import wtf.metio.yosql.models.constants.sql.SqlType;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Encapsulates everything we know about a single SQL statement.
 */
@Value.Immutable
public interface SqlStatement {

    //region builders

    static ImmutableSqlStatement.Builder builder() {
        return ImmutableSqlStatement.builder();
    }

    //endregion

    static Collector<SqlStatement, ?, Map<String, List<SqlStatement>>> groupByName() {
        return Collectors.groupingBy(statement -> statement.getConfiguration().name());
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
    default boolean shouldGenerateRxJavaAPI() {
        return getConfiguration().generateRxJavaApi() && isReading();
    }

    @Value.Lazy
    default boolean shouldGenerateStandardReadAPI() {
        return getConfiguration().generateStandardApi() && isReading();
    }

    @Value.Lazy
    default boolean shouldGenerateStandardCallAPI() {
        return getConfiguration().generateStandardApi() && isCalling();
    }

    @Value.Lazy
    default boolean shouldGenerateStandardWriteAPI() {
        return getConfiguration().generateStandardApi() && isWriting();
    }

    @Value.Lazy
    default boolean shouldGenerateStreamEagerAPI() {
        return getConfiguration().generateStreamEagerApi() && isReading();
    }

    @Value.Lazy
    default boolean shouldGenerateStreamLazyAPI() {
        return getConfiguration().generateStreamLazyApi() && isReading();
    }

    @Value.Lazy
    default boolean shouldGenerateBatchAPI() {
        return getConfiguration().generateBatchApi() && Buckets.hasEntries(getConfiguration().parameters()) && isWriting();
    }

}
