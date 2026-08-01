/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.models.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.javapoet.TypeName;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.javapoet.TypeGuesser;
import wtf.metio.yosql.internals.jdk.Strings;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * A converter turning one result row into one value.
 *
 * <p>Users name a converter by its class alone. The method to call, the type it returns and the
 * field name the repository gives it are read back out of that class, so the four parts are always
 * consistent with the code that actually exists — see {@code MethodResultRowConverterConfigurer},
 * which is what fills the other three in.</p>
 */
@Value.Immutable
@JsonSerialize(
        as = ImmutableResultRowConverter.class
)
public interface ResultRowConverter {

    //region builders

    static ImmutableResultRowConverter.Builder builder() {
        return ImmutableResultRowConverter.builder();
    }

    /**
     * Names a converter by its class, leaving everything else to be resolved from that class.
     *
     * <p>This is the only shape a user writes, both in a statement's front matter and in the
     * build tool's {@code defaultConverter}, which is why it is also the JSON/YAML form.</p>
     *
     * @param converterClass The fully-qualified name of the converter class.
     * @return A converter naming that class, or {@code null} if no class was named.
     */
    @JsonCreator
    static ResultRowConverter fromClassName(final String converterClass) {
        return Optional.ofNullable(converterClass)
                .map(String::strip)
                .filter(Predicate.not(Strings::isBlank))
                .map(value -> ResultRowConverter.builder()
                        .setConverterType(value)
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
