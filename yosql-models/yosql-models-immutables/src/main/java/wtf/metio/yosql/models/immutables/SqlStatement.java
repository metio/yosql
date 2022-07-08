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
import wtf.metio.yosql.models.configuration.SqlStatementType;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
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

    //region utils

    static Stream<ResultRowConverter> resultConverters(
            final List<SqlStatement> statements,
            final ResultRowConverter defaultConverter) {
        return statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(SqlStatement::requiresConverter)
                .map(config -> config.resultRowConverter().orElse(defaultConverter))
                .sorted(Comparator.comparing((ResultRowConverter converter) -> converter.alias().orElse(""))
                        .thenComparing(converter -> converter.converterType().orElse(""))
                        .thenComparing(converter -> converter.resultType().orElse(""))
                        .thenComparing(converter -> converter.methodName().orElse("")))
                .distinct();
    }

    private static boolean requiresConverter(final SqlConfiguration configuration) {
        return configuration.returningMode()
                .map(mode -> ReturningMode.NONE != mode)
                .orElse(Boolean.FALSE);
    }

    //endregion

    /**
     * @return The file system path to the source file of this SQL statement.
     */
    Path getSourcePath();

    /**
     * @return The parsed configuration of this SQL statement.
     */
    SqlConfiguration getConfiguration();

    /**
     * @return The raw SQL statement.
     */
    String getRawStatement();

    //region derived

    @Value.Lazy
    default String getName() {
        return getConfiguration().name().orElseThrow();
    }

    @Value.Lazy
    default String getRepositoryClass() {
        return getConfiguration().repository().orElseThrow();
    }

    @Value.Lazy
    default String getRepositoryInterface() {
        return getConfiguration().repositoryInterface().orElse("");
    }

    @Value.Lazy
    default boolean isReading() {
        return getConfiguration().type()
                .map(type -> SqlStatementType.READING == type)
                .orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean isWriting() {
        return getConfiguration().type()
                .map(type -> SqlStatementType.WRITING == type)
                .orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean isCalling() {
        return getConfiguration().type()
                .map(type -> SqlStatementType.CALLING == type)
                .orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean executeWriteBatch() {
        return isWriting() &&
                getConfiguration().executeBatch().orElse(Boolean.FALSE) &&
                Buckets.hasEntries(getConfiguration().parameters());
    }

    @Value.Lazy
    default boolean executeReadOnce() {
        return isReading() && getConfiguration().executeOnce().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean executeCallOnce() {
        return isCalling() && getConfiguration().executeOnce().orElse(Boolean.FALSE);
    }

    @Value.Lazy
    default boolean executeWriteOnce() {
        return isWriting() && getConfiguration().executeOnce().orElse(Boolean.FALSE);
    }

    //endregion

}
