/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.tooling.dagger.logging;

import dagger.MapKey;
import wtf.metio.yosql.models.configuration.LoggingApis;

/**
 * Binds a {@link wtf.metio.yosql.codegen.logging.LoggingGenerator} to the one API it implements.
 *
 * <p>A map rather than a set, because a set answers "which of these claims to support this API" with
 * whichever one iteration reached first. Two generators claiming the same API is a mistake, and with
 * a map it is one Dagger refuses to compile instead of one that silently picks a winner.</p>
 */
@MapKey
public @interface LoggingApiKey {

    LoggingApis value();

}
