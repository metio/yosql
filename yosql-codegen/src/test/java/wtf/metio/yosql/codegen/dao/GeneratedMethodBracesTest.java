/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.MethodSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Every generated method closes what it opens.
 *
 * <p>The generators compose a method out of blocks, and a block that opens a control flow is paired
 * with a separate one that closes it. JavaPoet counts the closing blocks it is given but does not
 * complain about the ones it is not, so a missing close emits a method body one level in, with the
 * method's own brace never written. The snapshot expectations record whatever comes out, so nothing
 * else notices; only the user's compiler does.</p>
 *
 * <p>This holds every combination of the settings that decide which flows a method opens, so a new
 * {@link ReturningMode} or a new branch in the assembly is covered the day it is added.</p>
 */
@DisplayName("generated methods")
class GeneratedMethodBracesTest {

    private static final Pattern STRING_LITERAL = Pattern.compile("\"(\\\\.|[^\"\\\\])*\"");
    private static final Pattern JAVADOC = Pattern.compile("/\\*\\*.*?\\*/", Pattern.DOTALL);

    static Stream<Arguments> methods() {
        final var arguments = new ArrayList<Arguments>();
        for (final var returningMode : ReturningMode.values()) {
            for (final var createConnection : List.of(true, false)) {
                final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(returningMode)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(createConnection);
                arguments.add(Arguments.of(
                        "read " + returningMode + " createConnection=" + createConnection,
                        (Supplier) () -> DaoObjectMother.readMethodGenerator().readMethod(
                                configuration.withType(SqlStatementType.READING),
                                SqlConfigurations.sqlStatement())));
                arguments.add(Arguments.of(
                        "write " + returningMode + " createConnection=" + createConnection,
                        (Supplier) () -> DaoObjectMother.writeMethodGenerator().writeMethod(
                                configuration.withType(SqlStatementType.WRITING),
                                SqlConfigurations.sqlStatement())));
                arguments.add(Arguments.of(
                        "call " + returningMode + " createConnection=" + createConnection,
                        (Supplier) () -> DaoObjectMother.callMethodGenerator().callMethod(
                                configuration.withType(SqlStatementType.CALLING),
                                SqlConfigurations.sqlStatement())));
            }
            arguments.add(Arguments.of(
                    "batch write " + returningMode,
                    (Supplier) () -> DaoObjectMother.writeMethodGenerator().batchWriteMethod(
                            SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                                    .withType(SqlStatementType.WRITING)
                                    .withReturningMode(returningMode)
                                    .withUsePreparedStatement(true)
                                    .withCreateConnection(true),
                            SqlConfigurations.sqlStatement())));
        }
        return arguments.stream();
    }

    @FunctionalInterface
    interface Supplier {
        MethodSpec get();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("methods")
    @DisplayName("close every brace they open")
    void balancesBraces(final String description, final Supplier method) {
        final var generated = method.get().toString();
        final var code = STRING_LITERAL.matcher(JAVADOC.matcher(generated).replaceAll("")).replaceAll("");
        var depth = 0;
        for (final var character : code.toCharArray()) {
            if (character == '{') {
                depth++;
            } else if (character == '}') {
                depth--;
            }
            Assertions.assertFalse(depth < 0,
                    () -> "closes more braces than it opens: " + description + "\n" + generated);
        }
        final var unclosed = depth;
        Assertions.assertEquals(0, unclosed,
                () -> "leaves " + unclosed + " brace(s) open: " + description + "\n" + generated);
    }

}
