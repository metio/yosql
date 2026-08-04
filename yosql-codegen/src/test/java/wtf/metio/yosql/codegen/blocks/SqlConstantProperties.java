/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeSpec;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.Assertions;
import wtf.metio.yosql.codegen.dao.JavaCompilation;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.List;

/**
 * A statement survives the trip into a generated constant and back.
 *
 * <p>The SQL goes into a Java text block, and a text block is source: a backslash starts an escape
 * sequence there, three quotes end the block, and trailing whitespace is stripped. Getting that
 * wrong stops the user's compile at best, and at worst compiles into a different query than the one
 * that was written — {@code '\s+'} is a legal escape meaning a single space.</p>
 *
 * <p>So the check is a round trip rather than a comparison against expected text: the constant is
 * compiled by javac, loaded, and read back. Comparing text would mean writing the escaping rules a
 * second time, and a second implementation is a second thing to get wrong — which is how the schema
 * validator came to look up columns that do not exist.</p>
 */
class SqlConstantProperties {

    private static final String CLASS = "com.example.persistence.Constants";

    /**
     * The characters a text block reads as something other than themselves, mixed with ordinary SQL.
     */
    @Provide
    Arbitrary<String> statements() {
        return Arbitraries.of(
                        "select", " ", "\n", "\t", "id", "from", "t", "where", "a", "=", "'", "\"",
                        "\\", "\\n", "\\s", "\\d+", "\"\"\"", "\"\"", "$", "%s", "{", "}", "\r",
                        "  ", "'\\d+'", "/*", "*/", "--", ";", "€", "ß", "\\\\")
                .list().ofMinSize(1).ofMaxSize(25)
                .map(parts -> String.join("", parts));
    }

    @Property(tries = 300)
    void aStatementReadsBackAsItself(@ForAll("statements") final String statement) {
        Assertions.assertEquals(escape(statement), escape(readBack(statement)),
                () -> "the constant does not hold the statement it was given: " + escape(statement));
    }

    /**
     * Text nobody thought about, rather than only the characters this test knows to be awkward.
     *
     * <p>Bounded to what a `.sql` file plausibly holds: printable characters, tabs, newlines and
     * carriage returns. Not every code point — U+2028 breaks the plain string literal JavaPoet
     * writes, and what JavaPoet can hold is not something YoSQL is in a position to promise.</p>
     */
    @Property(tries = 200)
    void arbitraryTextReadsBackAsItself(@ForAll("fileText") final String text) {
        Assertions.assertEquals(escape(text), escape(readBack(text)),
                () -> "the constant does not hold the text it was given: " + escape(text));
    }

    @Provide
    Arbitrary<String> fileText() {
        return Arbitraries.strings()
                .withChars('\t', '\n', '\r')
                .withCharRange('\u0020', '\u007e')
                .withCharRange('\u00a0', '\u04ff')
                .withCharRange('\u4e00', '\u4eff')
                .ofMaxLength(60);
    }

    /**
     * Compiles a class holding the statement as the generator writes it, then asks the JVM what the
     * constant actually contains.
     */
    private static String readBack(final String statement) {
        final var fields = new DefaultFields(BlocksObjectMother.annotationGenerator());
        final var constant = FieldSpec.builder(String.class, "QUERY",
                        Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(fields.initialize(statement))
                .build();
        final var type = TypeSpec.classBuilder("Constants")
                .addModifiers(Modifier.PUBLIC)
                .addField(constant)
                .build();
        final var source = JavaFile.builder("com.example.persistence", type).build().toString();
        try {
            final var loaded = JavaCompilation.compileAndLoad(
                    List.of(new JavaCompilation.Source(CLASS, source)));
            final Field field = loaded.loadClass(CLASS).getDeclaredField("QUERY");
            return (String) field.get(null);
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        } catch (final IllegalStateException exception) {
            throw new IllegalStateException("javac rejected the constant generated for ["
                    + escape(statement) + "]:\n" + exception.getMessage() + "\n\n" + source, exception);
        }
    }

    /**
     * Every character written so it can be read, because a counterexample full of control characters
     * applies itself to the terminal instead of showing what it was.
     */
    private static String escape(final String text) {
        final var readable = new StringBuilder(text.length() * 2);
        text.codePoints().forEach(point -> {
            if (point == '\\') {
                readable.append("\\\\");
            } else if (point >= 0x20 && point < 0x7F) {
                readable.appendCodePoint(point);
            } else {
                readable.append("\\u%04x".formatted(point));
            }
        });
        return readable.toString();
    }

}
