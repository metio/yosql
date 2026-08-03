/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen;

/**
 * The release that generated a file, filled in at build time.
 *
 * <p>It is the one fact about generated code that is nowhere else in it: which file a class came
 * from is already in its javadoc, and what it does is the code itself. Which release wrote it is
 * only knowable from the build that ran, and that is exactly what somebody reading a stack trace
 * through generated code needs.</p>
 */
public final class YoSqlVersion {

    public static final String VERSION = "${project.version}";

    private YoSqlVersion() {
        // constants class
    }

}
