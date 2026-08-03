/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that a list of allowed name prefixes was configured empty.
 *
 * <p>The prefixes decide what kind a statement is and are used to build a name when one has to be
 * invented. With none configured there is nothing to build from — which surfaced as an
 * {@code IndexOutOfBoundsException} from inside the generator, naming no setting and suggesting a
 * bug rather than a configuration to fix.</p>
 */
public final class MissingPrefixConfigurationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MissingPrefixConfigurationException(final String setting) {
        super("The '%s' configuration is empty. At least one prefix is needed to name a statement of that kind."
                .formatted(setting));
    }

}
