/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Parses SQL statements inside files.
 */
@FunctionalInterface
public interface SqlStatementParser {

    /**
     * The regex to extract named parameters out of SQL statements.
     *
     * <p>A named parameter is a colon and at least one word character. Requiring the name rules out
     * punctuation, as in the {@code :} of a licence header the statement carries, and the lookbehind
     * rules out the second colon of PostgreSQL's {@code ::} cast. Both used to be read as parameters,
     * and a cast took an index with it — which moved every parameter after it onto the wrong
     * placeholder.</p>
     *
     * <p>A colon inside a string literal is not ruled out here, because a regular expression cannot
     * see where a literal begins. Match it against {@link SqlText#maskLiteralsAndComments(String)}
     * instead, which is what every caller does — the offsets are the same either way.</p>
     */
    String NAMED_PARAMETER_REGEX = "(?<!:)(:\\w+)";
    String PARAMETER_REGEX = "\\?";

    /**
     * Names the parameters written as a bare {@code ?}, which have no name of their own.
     */
    String UNNAMED_PARAMETERS = "unnamedParameters";

    /**
     * The pattern to extract named parameters out of SQL statements
     */
    Pattern NAMED_PARAMETER_PATTERN = Pattern.compile(NAMED_PARAMETER_REGEX);
    Pattern PARAMETER_PATTERN = Pattern.compile(PARAMETER_REGEX);

    /**
     * Either spelling of a placeholder, so that both can be counted in the order they appear.
     */
    Pattern PLACEHOLDER_PATTERN = Pattern.compile(NAMED_PARAMETER_REGEX + "|" + PARAMETER_REGEX);

    /**
     * Parses SQL statements from a file.
     *
     * @param pathToFile The file to parse.
     * @return A stream of statements founds inside the source file.
     */
    Stream<SqlStatement> parse(Path pathToFile);

    /**
     * Extracts parameter indices from a given SQL statement.
     *
     * @param sqlStatement The raw SQL statement to parse.
     * @return Extracted parameters and their indices.
     */
    /**
     * <p>Both spellings are counted in one pass, because the number a placeholder is bound by is its
     * position among <em>all</em> of them. Counting each spelling from one separately gives a
     * statement mixing {@code ?} and {@code :name} two parameters bound to index 1 and nothing bound
     * to index 2.</p>
     */
    static Map<String, List<Integer>> extractParameterIndices(final String sqlStatement) {
        final Map<String, List<Integer>> indices = new LinkedHashMap<>();
        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(SqlText.maskLiteralsAndComments(sqlStatement));
        int counter = 1;
        while (matcher.find()) {
            final String parameterName = matcher.group().startsWith("?")
                    ? UNNAMED_PARAMETERS
                    : sqlStatement.substring(matcher.start() + 1, matcher.end());
            indices.computeIfAbsent(parameterName, string -> new ArrayList<>(3)).add(counter++);
        }
        return indices;
    }

}
