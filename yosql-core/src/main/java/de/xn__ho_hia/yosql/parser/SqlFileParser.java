package de.xn__ho_hia.yosql.parser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import de.xn__ho_hia.yosql.model.SqlStatement;

/**
 * Parses SQL statements inside files.
 */
public interface SqlFileParser {

    /** The regex to extract parameters out of SQL statements. */
    String  PARAMETER_REGEX   = "(?<!')(:[\\w]*)(?!')";          //$NON-NLS-1$

    /** The pattern to extract parameters out of SQL statements */
    Pattern PARAMETER_PATTERN = Pattern.compile(PARAMETER_REGEX);

    /**
     * Parses SQL statements from a file.
     *
     * @param source
     *            The source file to read.
     * @return A stream of statements founds inside the source file.
     */
    Stream<SqlStatement> parse(Path source);

    /**
     * @param sqlStatement
     *            The SQL statement to use.
     * @return Extracted parameters and their indices.
     */
    default Map<String, List<Integer>> extractParameterIndices(final String sqlStatement) {
        final Map<String, List<Integer>> indices = new LinkedHashMap<>();
        final Matcher matcher = PARAMETER_PATTERN.matcher(sqlStatement);
        int counter = 1;
        while (matcher.find()) {
            final String parameterName = matcher.group().substring(1);
            indices.computeIfAbsent(parameterName, string -> new ArrayList<>(3))
                    .add(Integer.valueOf(counter++));
        }
        return indices;
    }

}
