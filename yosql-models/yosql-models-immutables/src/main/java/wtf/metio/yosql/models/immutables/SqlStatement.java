/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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

    static ImmutableSqlStatement.SourcePathBuildStage builder() {
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
        return  isWriting() && getConfiguration().generateBlockingApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateMutinyCallAPI() {
        return isCalling() && getConfiguration().generateMutinyApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateMutinyReadAPI() {
        return isReading() && getConfiguration().generateMutinyApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateMutinyWriteAPI() {
        return isWriting() && getConfiguration().generateMutinyApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateReactorCallAPI() {
        return isCalling() && getConfiguration().generateReactorApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateReactorReadAPI() {
        return isReading() && getConfiguration().generateReactorApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateReactorWriteAPI() {
        return isWriting() && getConfiguration().generateReactorApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateRxJavaCallAPI() {
        return isCalling() && getConfiguration().generateRxJavaApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateRxJavaReadAPI() {
        return isReading() && getConfiguration().generateRxJavaApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateRxJavaWriteAPI() {
        return isWriting() && getConfiguration().generateRxJavaApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateStreamEagerReadAPI() {
        return isReading() && getConfiguration().generateStreamEagerApi().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean shouldGenerateStreamLazyReadAPI() {
        return isReading() && getConfiguration().generateStreamLazyApi().orElse(Boolean.FALSE);
    }

}
