/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
/**
 * The model used during code generation.
 */
@org.immutables.value.Value.Style(
        get = {"is*", "get*"},
        init = "set*",
        strictBuilder = true,
        jdkOnly = true,
        stagedBuilder = true
)
@org.jspecify.annotations.NullMarked
package wtf.metio.yosql.models.meta;
