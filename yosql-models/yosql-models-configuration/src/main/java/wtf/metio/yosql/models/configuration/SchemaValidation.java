/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.models.configuration;

/**
 * What to do about a statement that disagrees with the schema.
 *
 * <p>Three settings rather than a switch, because turning this on in an existing project is a
 * migration: the first run finds everything at once, and a build that fails on all of it at the same
 * time is a build nobody can bisect. {@code WARN} makes the same list without stopping anyone.</p>
 */
public enum SchemaValidation {

    /**
     * Say nothing. Statements are generated exactly as they were before any of this existed.
     */
    OFF,

    /**
     * Report a disagreement and carry on generating.
     */
    WARN,

    /**
     * Fail the build, naming the file and the statement.
     */
    ERROR,

}
