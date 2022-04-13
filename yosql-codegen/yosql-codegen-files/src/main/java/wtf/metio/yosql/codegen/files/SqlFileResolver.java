/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.files;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Resolves SQL files possibly from an external source, like the file system.
 */
@FunctionalInterface
public interface SqlFileResolver {

    /**
     * @return A stream of SQL files found in the source of this resolver.
     */
    Stream<Path> resolveFiles();

}
