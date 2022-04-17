/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
/**
 * The model used during code generation.
 */
@Value.Style(
        get = {"is*", "get*"},
        init = "set*",
        strictBuilder = true,
        jdkOnly = true,
        stagedBuilder = true
)
package wtf.metio.yosql.models.meta;

import org.immutables.value.Value;