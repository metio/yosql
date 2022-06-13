/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
     */
    String NAMED_PARAMETER_REGEX = "(?<!')(:[\\w]*)(?!')";
    String PARAMETER_REGEX = "\\?";

    /**
     * The pattern to extract named parameters out of SQL statements
     */
    Pattern NAMED_PARAMETER_PATTERN = Pattern.compile(NAMED_PARAMETER_REGEX);
    Pattern PARAMETER_PATTERN = Pattern.compile(PARAMETER_REGEX);

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
    default Map<String, List<Integer>> extractParameterIndices(final String sqlStatement) {
        final var indices = parseNamedParameters(sqlStatement);
        indices.putAll(parseUnnamedParameters(sqlStatement));
        return indices;
    }

    private Map<String, List<Integer>> parseNamedParameters(final String sqlStatement) {
        final Map<String, List<Integer>> indices = new LinkedHashMap<>();
        final Matcher matcher = NAMED_PARAMETER_PATTERN.matcher(sqlStatement);
        int counter = 1;
        while (matcher.find()) {
            final String parameterName = matcher.group().substring(1);
            indices.computeIfAbsent(parameterName, string -> new ArrayList<>(3)).add(counter++);
        }
        return indices;
    }

    private Map<String, List<Integer>> parseUnnamedParameters(final String sqlStatement) {
        final Map<String, List<Integer>> indices = new LinkedHashMap<>();
        final Matcher matcher = PARAMETER_PATTERN.matcher(sqlStatement);
        int counter = 1;
        while (matcher.find()) {
            indices.computeIfAbsent("unnamedParameters", string -> new ArrayList<>(3)).add(counter++);
        }
        return indices;
    }

}
