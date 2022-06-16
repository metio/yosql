/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.javapoet.TypeName;
import de.xn__ho_hia.javapoet.TypeGuesser;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.jdk.Strings;

import java.util.Optional;
import java.util.function.Predicate;

@Value.Immutable
@JsonSerialize(
        as = ImmutableResultRowConverter.class
)
@JsonDeserialize(
        as = ImmutableResultRowConverter.class
)
public interface ResultRowConverter {

    //region builders

    static ImmutableResultRowConverter.Builder builder() {
        return ImmutableResultRowConverter.builder();
    }

    static ResultRowConverter fromString(final String input) {
        return Optional.ofNullable(input)
                .map(String::strip)
                .filter(Predicate.not(Strings::isBlank))
                .map(value -> value.split(":"))
                .map(values -> ResultRowConverter.builder()
                        .setAlias(values[0])
                        .setConverterType(values[1])
                        .setMethodName(values[2])
                        .setResultType(values[3])
                        .build())
                .orElse(null);
    }

    //endregion

    /**
     * @return The (short) alias of this converter.
     */
    Optional<String> alias();

    /**
     * @return The fully-qualified type name of this converter.
     */
    Optional<String> converterType();

    /**
     * @return The name of the method to use while converting values.
     */
    Optional<String> methodName();

    /**
     * @return The fully-qualified result type of this converter.
     */
    Optional<String> resultType();

    //region derived

    @Value.Lazy
    default Optional<TypeName> resultTypeName() {
        return resultType().map(TypeGuesser::guessTypeName);
    }

    @Value.Lazy
    default Optional<TypeName> converterTypeName() {
        return converterType().map(TypeGuesser::guessTypeName);
    }

    //endregion

}
