/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.model.sql;

import org.immutables.value.Value;

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
        return Collectors.groupingBy(statement -> statement.getConfiguration().getName());
    }

    Path getSourcePath();

    SqlConfiguration getConfiguration();

    String getRawStatement();

    @Value.Derived
    default String getName() {
        return getConfiguration().getName();
    }

    @Value.Derived
    default String getRepository() {
        return getConfiguration().getRepository();
    }

    @Value.Derived
    default boolean isReading() {
        return SqlType.READING == getConfiguration().getType();
    }

    @Value.Derived
    default boolean isWriting() {
        return SqlType.WRITING == getConfiguration().getType();
    }

    @Value.Derived
    default boolean isCalling() {
        return SqlType.CALLING == getConfiguration().getType();
    }

    @Value.Derived
    default boolean shouldGenerateRxJavaAPI() {
        return getConfiguration().isMethodRxJavaApi() && isReading();
    }

    @Value.Derived
    default boolean shouldGenerateStandardReadAPI() {
        return getConfiguration().isMethodStandardApi() && isReading();
    }

    @Value.Derived
    default boolean shouldGenerateStandardCallAPI() {
        return getConfiguration().isMethodStandardApi() && isCalling();
    }

    @Value.Derived
    default boolean shouldGenerateStandardWriteAPI() {
        return getConfiguration().isMethodStandardApi() && isWriting();
    }

    @Value.Derived
    default boolean shouldGenerateStreamEagerAPI() {
        return getConfiguration().isMethodStreamEagerApi() && isReading();
    }

    @Value.Derived
    default boolean shouldGenerateStreamLazyAPI() {
        return getConfiguration().isMethodStreamLazyApi() && isReading();
    }

    @Value.Derived
    default boolean shouldGenerateBatchAPI() {
        return getConfiguration().isMethodBatchApi() && getConfiguration().hasParameters() && isWriting();
    }

}
