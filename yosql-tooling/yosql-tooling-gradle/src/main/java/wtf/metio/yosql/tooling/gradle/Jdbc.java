/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Configures JDBC related options.
 */
public abstract class Jdbc {

    /**
     * @return The name suffix to add for raw SQL statements.
     */
    @Input
    public abstract Property<String> getRawSuffix();

    /**
     * @return The name suffix to add for index lookup tables.
     */
    @Input
    public abstract Property<String> getIndexSuffix();

    /**
     * @return The name for a DataSource variable.
     */
    @Input
    public abstract Property<String> getDataSource();

    /**
     * @return The name for a Connection variable.
     */
    @Input
    public abstract Property<String> getConnection();

    /**
     * @return The name for a Statement variable.
     */
    @Input
    public abstract Property<String> getStatement();

    /**
     * @return The name for a MetaData variable.
     */
    @Input
    public abstract Property<String> getMetaData();

    /**
     * @return The name for a ResultSet variable.
     */
    @Input
    public abstract Property<String> getResultSet();

    /**
     * @return The name for a ColumnCount variable.
     */
    @Input
    public abstract Property<String> getColumnCount();

    /**
     * @return The name for a ColumnLabel variable.
     */
    @Input
    public abstract Property<String> getColumnLabel();

    /**
     * @return The name for a Batch variable.
     */
    @Input
    public abstract Property<String> getBatch();

    /**
     * @return The name for a List variable.
     */
    @Input
    public abstract Property<String> getList();

    /**
     * @return The name for a JDBC index variable.
     */
    @Input
    public abstract Property<String> getJdbcIndex();

    /**
     * @return The name for a index variable.
     */
    @Input
    public abstract Property<String> getIndex();

    /**
     * @return The name for a row variable.
     */
    @Input
    public abstract Property<String> getRow();

    /**
     * @return The class name of the result-state class.
     */
    @Input
    public abstract Property<String> getResultStateClassName();

    /**
     * @return The class name of the result-row class.
     */
    @Input
    public abstract Property<String> getResultRowClassName();

    /**
     * @return The class name of the flow-state class.
     */
    @Input
    public abstract Property<String> getFlowStateClassName();

    /**
     * @return The default result row converter in case no other is specified.
     */
    @Input
    public abstract Property<DefaultResultRowConverter> getDefaultConverter();

    /**
     * @return The list of custom converters provided by the user.
     */
    @Input
    public abstract NamedDomainObjectContainer<UserResultRowConverter> getUserTypes();

    /**
     * @return The package name for JDBC utility classes.
     */
    @Input
    public abstract Property<String> getUtilityPackageName();

    /**
     * @param action The files config to apply.
     */
    public void defaultConverter(Action<? super DefaultResultRowConverter> action) {
        action.execute(getDefaultConverter().get());
    }

    JdbcConfiguration asConfiguration() {
        return JdbcConfiguration.usingDefaults()
                .setDefaultConverter(getDefaultConverter().get().asResultRowConverter(getUtilityPackageName().get()))
                .setUserTypes(createRowConverters())
                .setUtilityPackageName(getUtilityPackageName().get())
                .setRawSuffix(getRawSuffix().get())
                .setIndexSuffix(getIndexSuffix().get())
                .setDataSource(getDataSource().get())
                .setConnection(getConnection().get())
                .setStatement(getStatement().get())
                .setMetaData(getMetaData().get())
                .setResultSet(getResultSet().get())
                .setColumnCount(getColumnCount().get())
                .setColumnLabel(getColumnLabel().get())
                .setBatch(getBatch().get())
                .setList(getList().get())
                .setJdbcIndex(getJdbcIndex().get())
                .setIndex(getIndex().get())
                .setRow(getRow().get())
                .build();
    }

    void configureConventions(final ObjectFactory objects) {
        final var defaultConverter = objects.newInstance(DefaultResultRowConverter.class);
        defaultConverter.getAlias().set("resultRow");
        defaultConverter.getConverterType().set("ToResultRowConverter");
        defaultConverter.getMethodName().set("asUserType");
        defaultConverter.getResultType().set("ResultRow");
        getDefaultConverter().convention(defaultConverter);
        getUtilityPackageName().convention("com.example.persistence.util");
        getRawSuffix().convention("_RAW");
        getIndexSuffix().convention("_INDEX");
        getDataSource().convention("dataSource");
        getConnection().convention("connection");
        getStatement().convention("statement");
        getMetaData().convention("metaData");
        getResultSet().convention("resultSet");
        getColumnCount().convention("columnCount");
        getColumnLabel().convention("columnLabel");
        getBatch().convention("batch");
        getList().convention("list");
        getJdbcIndex().convention("jdbcIndex");
        getIndex().convention("index");
        getRow().convention("row");
        getResultStateClassName().convention("ResultState");
        getResultRowClassName().convention("ResultRow");
        getFlowStateClassName().convention("FlowState");
    }

    private List<ResultRowConverter> createRowConverters() {
        return getUserTypes().stream()
                .map(UserResultRowConverter::asResultRowConverter)
                .collect(Collectors.toList());
    }

    private ResultRowConverter createRowConverter(final String converterDefinition) {
        return Optional.ofNullable(converterDefinition)
                .map(String::strip)
                .filter(Predicate.not(Strings::isBlank))
                .map(value -> value.split(":"))
                .map(values -> ResultRowConverter.builder()
                        .setAlias(values[0])
                        .setConverterType(values[1])
                        .setMethodName(values[2])
                        .setResultType(values[3])
                        .build())
                .orElse(ResultRowConverter.builder()
                        .setAlias("resultRow")
                        .setConverterType("com.example.persistence.util.ToResultRowConverter")
                        .setMethodName("asUserType")
                        .setResultType("com.example.persistence.util.ResultRow")
                        .build());
    }
}
