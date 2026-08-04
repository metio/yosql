/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.internals.javapoet;

import com.palantir.javapoet.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * An extension for JavaPoet's {@link ClassName#bestGuess(String)} that adds support for primitive and generic types as
 * well as wildcard using sub- and supertypes.
 */
public final class TypeGuesser {

    private static final String OPEN_ANGLE_BRACKET = "<";
    private static final String CLOSING_ANGLE_BRACKET = ">";
    private static final String CLOSING_SQUARE_BRACKET = "]";
    private static final String COMMA = ",";
    private static final String QUESTION_MARK = "?";
    private static final String EXTENDS = "extends";
    private static final String SUPER = "super";

    /**
     * The types a statement's parameters and results are overwhelmingly written in, under the name
     * they are known by rather than the package they live in.
     *
     * <p>Without them a bare {@code UUID} is a class name with no package, which compiles to an
     * import that is not there. Matching is case-insensitive, so {@code uuid} and {@code UUID} name
     * the same type and neither can be mistaken for a class of the project: a name that resolves
     * here has no package, and a type of the project always does.</p>
     */
    private static final Map<String, ClassName> ALIASES = Map.ofEntries(
            Map.entry("string", ClassName.get(String.class)),
            Map.entry("object", ClassName.get(Object.class)),
            Map.entry("uuid", ClassName.get(java.util.UUID.class)),
            Map.entry("bigdecimal", ClassName.get(java.math.BigDecimal.class)),
            Map.entry("biginteger", ClassName.get(java.math.BigInteger.class)),
            Map.entry("instant", ClassName.get(java.time.Instant.class)),
            Map.entry("localdate", ClassName.get(java.time.LocalDate.class)),
            Map.entry("localdatetime", ClassName.get(java.time.LocalDateTime.class)),
            Map.entry("localtime", ClassName.get(java.time.LocalTime.class)),
            Map.entry("offsetdatetime", ClassName.get(java.time.OffsetDateTime.class)),
            Map.entry("offsettime", ClassName.get(java.time.OffsetTime.class)),
            Map.entry("zoneddatetime", ClassName.get(java.time.ZonedDateTime.class)),
            Map.entry("duration", ClassName.get(java.time.Duration.class)),
            Map.entry("period", ClassName.get(java.time.Period.class)),
            Map.entry("date", ClassName.get(java.sql.Date.class)),
            Map.entry("time", ClassName.get(java.sql.Time.class)),
            Map.entry("timestamp", ClassName.get(java.sql.Timestamp.class)),
            Map.entry("array", ClassName.get(java.sql.Array.class)),
            Map.entry("blob", ClassName.get(java.sql.Blob.class)),
            Map.entry("clob", ClassName.get(java.sql.Clob.class)),
            Map.entry("ref", ClassName.get(java.sql.Ref.class)),
            Map.entry("rowid", ClassName.get(java.sql.RowId.class)),
            Map.entry("sqlxml", ClassName.get(java.sql.SQLXML.class)),
            Map.entry("url", ClassName.get(java.net.URL.class)),
            Map.entry("uri", ClassName.get(java.net.URI.class)),
            Map.entry("currency", ClassName.get(java.util.Currency.class)),
            Map.entry("locale", ClassName.get(java.util.Locale.class)),
            Map.entry("zoneid", ClassName.get(java.time.ZoneId.class)),
            Map.entry("inputstream", ClassName.get(java.io.InputStream.class)),
            Map.entry("reader", ClassName.get(java.io.Reader.class)));

    private TypeGuesser() {
        // utility class, call #guessTypeName() directly
    }

    /**
     * A replacement for {@link ClassName#bestGuess(String)} that adds support for various new types.
     *
     * @param type The fully qualified type to guess
     * @return The guessed {@link TypeName}.
     */
    public static TypeName guessTypeName(final String type) {
        try {
            final var trimmedInput = type.trim();
            if (trimmedInput.endsWith(CLOSING_SQUARE_BRACKET)) {
                final var typeNameWithoutArraySuffix = trimmedInput.substring(0, trimmedInput.length() - 2);
                return ArrayTypeName.of(guessTypeName(typeNameWithoutArraySuffix));
            }
            return guessType(trimmedInput);
        } catch (final StringIndexOutOfBoundsException exception) {
            throw new IllegalArgumentException(type, exception);
        }
    }

    // visible for testing
    static TypeName guessType(final String type) {
        return switch (type) {
            case "boolean" -> TypeName.BOOLEAN;
            case "byte" -> TypeName.BYTE;
            case "short" -> TypeName.SHORT;
            case "long" -> TypeName.LONG;
            case "char" -> TypeName.CHAR;
            case "float" -> TypeName.FLOAT;
            case "double" -> TypeName.DOUBLE;
            case "int" -> TypeName.INT;
            default -> guessObjectType(type);
        };
    }

    private static TypeName guessObjectType(final String type) {
        if (type.startsWith(QUESTION_MARK)) {
            return guessWildcardType(type);
        }
        if (type.contains(OPEN_ANGLE_BRACKET)) {
            return guessGenericType(type);
        }
        final var alias = ALIASES.get(type.toLowerCase(Locale.ROOT));
        return alias == null ? ClassName.bestGuess(type) : alias;
    }

    private static TypeName guessWildcardType(final String type) {
        final var indexOfExtends = type.indexOf(EXTENDS);
        final var indexOfSuper = type.indexOf(SUPER);
        if (firstBeforeSecond(indexOfExtends, indexOfSuper)) {
            return guessExtendingType(type);
        } else if (firstBeforeSecond(indexOfSuper, indexOfExtends)) {
            return guessSuperType(type);
        }
        return WildcardTypeName.subtypeOf(TypeName.get(Object.class));
    }

    private static TypeName guessExtendingType(final String type) {
        return WildcardTypeName.subtypeOf(
                guessTypeName(type.substring(type.indexOf(EXTENDS) + EXTENDS.length())));
    }

    private static TypeName guessSuperType(final String type) {
        return WildcardTypeName.supertypeOf(
                guessTypeName(type.substring(type.indexOf(SUPER) + SUPER.length())));
    }

    private static TypeName guessGenericType(final String type) {
        final var rawPart = type.substring(0, type.indexOf(OPEN_ANGLE_BRACKET)).trim();
        final var genericPart = type.substring(type.indexOf(OPEN_ANGLE_BRACKET) + 1,
                type.lastIndexOf(CLOSING_ANGLE_BRACKET));
        final var rawType = ClassName.bestGuess(rawPart);
        final var typeArguments = guessGenericTypeArguments(genericPart);
        return ParameterizedTypeName.get(rawType, typeArguments);
    }

    private static TypeName[] guessGenericTypeArguments(final String genericPart) {
        final var types = new ArrayList<TypeName>();
        var inputToParse = genericPart;
        while (!inputToParse.isEmpty()) {
            final var indexOfComma = inputToParse.indexOf(COMMA);
            final var indexOfAngel = inputToParse.indexOf(OPEN_ANGLE_BRACKET);

            if (firstBeforeSecond(indexOfComma, indexOfAngel)) {
                types.add(guessTypeName(inputToParse.substring(0, indexOfComma)));
                inputToParse = inputToParse.substring(indexOfComma + 1);
            } else if (firstBeforeSecond(indexOfAngel, indexOfComma)) {
                final var endIndex = calculateEndIndexOfGenericType(inputToParse);
                types.add(guessTypeName(inputToParse.substring(0, endIndex)));
                inputToParse = afterSeparator(inputToParse, endIndex);
            } else {
                types.add(guessTypeName(inputToParse));
                inputToParse = "";
            }
        }

        return types.toArray(TypeName[]::new);
    }

    /**
     * What is left after a nested type argument and the comma that ends it.
     *
     * <p>The comma does not have to sit immediately after the closing angle bracket —
     * {@code Map<List<String> , Integer>} is as legal as it is unusual. Assuming it does consumed
     * the space instead, left the comma at the front of the remainder, and the next round then read
     * the empty string as a type name.</p>
     */
    private static String afterSeparator(final String input, final int endIndex) {
        var index = endIndex;
        while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
            index++;
        }
        if (index < input.length() && input.charAt(index) == ',') {
            index++;
        }
        return input.substring(index);
    }

    private static boolean firstBeforeSecond(final int first, final int second) {
        return isFirst(first, second) || isOnly(first, second);
    }

    private static boolean isFirst(final int first, final int second) {
        return exists(first, second) && first < second;
    }

    private static boolean isOnly(final int first, final int second) {
        return exists(first) && !firstBeforeSecond(second, first);
    }

    private static boolean exists(final int first, final int second) {
        return exists(first) && exists(second);
    }

    private static boolean exists(final int index) {
        return index >= 0;
    }

    private static int calculateEndIndexOfGenericType(final String inputToParse) {
        var countOfOpenAngels = 0;
        var countOfClosingAngels = 0;
        var endIndex = inputToParse.length();
        for (var index = 0; index < inputToParse.length(); index++) {
            if (inputToParse.codePointAt(index) == OPEN_ANGLE_BRACKET.charAt(0)) {
                countOfOpenAngels++;
            }
            if (inputToParse.codePointAt(index) == CLOSING_ANGLE_BRACKET.charAt(0)) {
                countOfClosingAngels++;
                if (countOfOpenAngels == countOfClosingAngels) {
                    endIndex = index + 1;
                    break;
                }
            }
        }
        return endIndex;
    }

}
