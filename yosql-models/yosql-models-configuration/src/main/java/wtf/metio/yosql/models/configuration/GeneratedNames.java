/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.configuration;

import java.util.Set;

/**
 * What the generator calls the things it declares.
 *
 * <p>Every one of these is a local, a private field or a constant inside a generated class. None of
 * them is part of a repository's API, so none of them is a decision a project has to make: a name
 * nobody outside the method can see cannot fit a codebase better or worse.</p>
 *
 * <p>What they can do is collide with a statement's own parameter, since both become identifiers in
 * the same method. {@link #TAKEN} is the set that would, and a statement naming a parameter after
 * one of them is refused while the SQL is still in front of us — rather than compiling into a Java
 * error about a variable already defined, in a file the author did not write.</p>
 */
public final class GeneratedNames {

    public static final String LOGGER = "LOG";
    public static final String QUERY = "query";
    public static final String RAW_QUERY = "rawQuery";
    public static final String EXECUTED_QUERY = "executedQuery";
    public static final String DATABASE_PRODUCT_NAME = "databaseProductName";
    public static final String ACTION = "action";
    public static final String EXCEPTION = "exception";
    public static final String RAW_SUFFIX = "_RAW";
    public static final String INDEX_SUFFIX = "_INDEX";
    public static final String DATA_SOURCE = "dataSource";
    public static final String CONNECTION = "connection";
    public static final String STATEMENT = "statement";
    public static final String RESULT_SET_META_DATA = "resultSetMetaData";
    public static final String DATABASE_META_DATA = "databaseMetaData";
    public static final String RESULT_SET = "resultSet";
    public static final String COLUMN_COUNT = "columnCount";
    public static final String COLUMN_LABEL = "columnLabel";
    public static final String BATCH = "batch";
    public static final String LIST = "list";
    public static final String JDBC_INDEX = "jdbcIndex";
    public static final String INDEX = "index";
    public static final String ROW = "row";

    /**
     * The identifiers a generated method already uses. The two suffixes are not among them: they name
     * constants, which a method-level parameter cannot clash with.
     *
     * <p>All of them, rather than only the ones a particular statement would declare. Which
     * variables a method ends up with depends on what it returns, whether it batches and whether
     * logging is on — so a reserved set that followed the shape would make adding
     * {@code returning: multiple} break a parameter that had been fine until then. One list, checked
     * the same way for every statement, is the version an author can predict.</p>
     */
    public static final Set<String> TAKEN = Set.of(
            LOGGER, QUERY, RAW_QUERY, EXECUTED_QUERY, DATABASE_PRODUCT_NAME, ACTION, EXCEPTION,
            DATA_SOURCE, CONNECTION, STATEMENT, RESULT_SET_META_DATA, DATABASE_META_DATA, RESULT_SET,
            COLUMN_COUNT, COLUMN_LABEL, BATCH, LIST, JDBC_INDEX, INDEX, ROW);

    /**
     * What {@code @Generated} names as the tool, and therefore what marks a file as ours.
     *
     * <p>Read as well as written: it is how a run tells a file it wrote last time from a file
     * somebody put in the output directory by hand.</p>
     */
    public static final String GENERATOR = "wtf.metio.yosql";

    /**
     * Names the local a collection's values are bound through, one at a time.
     */
    public static final String ELEMENT_SUFFIX = "Element";

    /**
     * Names the local a parameter is converted into before it is bound.
     */
    public static final String PARAMETER_SUFFIX = "Parameter";

    /**
     * Whether one parameter's name is a local the generated method derives from another's.
     *
     * <p>{@link #TAKEN} cannot hold these: they are not fixed names but names built from whatever the
     * author called the parameter beside them, so a statement binding both {@code ids} and
     * {@code idsElement} declares the same identifier twice.</p>
     *
     * <p>Checked whether or not the statement in question would actually derive the name, for the
     * same reason {@link #TAKEN} lists every variable rather than the ones a particular shape
     * declares: a rule that depends on the shape moves under the author's feet.</p>
     */
    public static boolean derivesFrom(final String candidate, final String parameter) {
        if (candidate.length() <= parameter.length() || !candidate.startsWith(parameter)) {
            return false;
        }
        final var suffix = candidate.substring(parameter.length());
        return ELEMENT_SUFFIX.equals(suffix) || suffix.startsWith(PARAMETER_SUFFIX);
    }

    private GeneratedNames() {
        // constants class
    }

}
