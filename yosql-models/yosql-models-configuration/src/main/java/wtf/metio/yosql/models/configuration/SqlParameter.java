/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.javapoet.TypeName;
import org.immutables.value.Value;
import wtf.metio.javapoet.TypeGuesser;
import wtf.metio.yosql.internals.jdk.Buckets;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a single input parameter of a SQL statement.
 */
@Value.Immutable
@JsonSerialize(
        as = ImmutableSqlParameter.class
)
@JsonDeserialize(
        as = ImmutableSqlParameter.class
)
public interface SqlParameter {

    //region builders

    static ImmutableSqlParameter.Builder builder() {
        return ImmutableSqlParameter.builder();
    }

    static ImmutableSqlParameter copyOf(final SqlParameter parameter) {
        return ImmutableSqlParameter.copyOf(parameter);
    }

    //endregion

    //region utils

    /**
     * Deeply merges two parameter lists using the name of the parameter as identifying key.
     *
     * @param first  The first list of parameters.
     * @param second The second list of parameters.
     * @return The merged list of parameters.
     */
    static List<SqlParameter> mergeParameters(
            final List<SqlParameter> first,
            final List<SqlParameter> second) {
        if (first == null || first.isEmpty()) {
            return second;
        }
        return Stream.concat(copyAttributes(first, second), copyAttributes(second, first))
                .filter(Buckets.distinctByKey(SqlParameter::name))
                .collect(Collectors.toList());
    }

    private static Stream<ImmutableSqlParameter> copyAttributes(final List<SqlParameter> first, final List<SqlParameter> second) {
        return first.stream()
                .map(param -> second.stream()
                        .filter(other -> param.name().equals(other.name()))
                        .findFirst()
                        .map(other -> SqlParameter.copyOf(param)
                                .withType(param.type().or(other::type))
                                .withIndices(param.indices().or(other::indices))
                                .withVariant(param.variant().or(other::variant))
                                .withSqlType(param.sqlType().or(other::sqlType))
                                .withScale(param.scale().or(other::scale)))
                        .orElseGet(() -> SqlParameter.copyOf(param)));
    }

    //endregion

    /**
     * @return The name of the parameter.
     */
    Optional<String> name();

    /**
     * @return The fully-qualified type name.
     */
    Optional<String> type();

    /**
     * @return The indices in the SQL statement that match this parameter.
     */
    @JsonIgnore
    Optional<int[]> indices();

    /**
     * @return The variant of this parameter.
     */
    Optional<SqlParameterVariant> variant();

    /**
     * @return The JDBC type of this parameter.
     */
    Optional<Integer> sqlType();

    /**
     * @return The scale of this parameter in case its sqlType is numeric/decimal.
     */
    Optional<Integer> scale();

    //region lazy

    /**
     * @return The parsed type name of this parameter.
     */
    @Value.Lazy
    default Optional<TypeName> typeName() {
        return type().map(TypeGuesser::guessTypeName);
    }

    /**
     * @return <code>true</code> in case this statement has indices, <code>false</code> otherwise.
     */
    @Value.Lazy
    default boolean hasIndices() {
        return indices().map(values -> values.length > 0).orElse(Boolean.FALSE);
    }

    //endregion

}
