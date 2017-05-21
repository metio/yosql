/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings({ "javadoc" })
public class SqlStatement {

    public static final Collector<SqlStatement, ?, Map<String, List<SqlStatement>>> groupByName() {
        return Collectors.groupingBy(statement -> statement.getConfiguration().getName());
    }

    private final SqlConfiguration configuration;
    private final String           rawStatement;
    private final Path             source;

    public SqlStatement(final Path source, final SqlConfiguration configuration, final String rawStatement) {
        this.source = source;
        this.configuration = configuration;
        this.rawStatement = rawStatement;
    }

    public SqlConfiguration getConfiguration() {
        return configuration;
    }

    public String getName() {
        return configuration.getName();
    }

    public String getRawStatement() {
        return rawStatement;
    }

    public String getRepository() {
        return configuration.getRepository();
    }

    public boolean isReading() {
        return SqlType.READING == configuration.getType();
    }

    public boolean isWriting() {
        return SqlType.WRITING == configuration.getType();
    }

    public boolean isCalling() {
        return SqlType.CALLING == configuration.getType();
    }

    public boolean shouldGenerateRxJavaAPI() {
        return configuration.isMethodRxJavaApi()
                && isReading();
    }

    public boolean shouldGenerateStandardReadAPI() {
        return configuration.isMethodStandardApi()
                && isReading();
    }

    public boolean shouldGenerateStandardCallAPI() {
        return configuration.isMethodStandardApi()
                && isCalling();
    }

    public boolean shouldGenerateStandardWriteAPI() {
        return configuration.isMethodStandardApi()
                && isWriting();
    }

    public boolean shouldGenerateStreamEagerAPI() {
        return configuration.isMethodStreamEagerApi()
                && isReading();
    }

    public boolean shouldGenerateStreamLazyAPI() {
        return configuration.isMethodStreamLazyApi()
                && isReading();
    }

    public boolean shouldGenerateBatchAPI() {
        return configuration.isMethodBatchApi()
                && configuration.hasParameters()
                && isWriting();
    }

    public Path getSourcePath() {
        return source;
    }

}
