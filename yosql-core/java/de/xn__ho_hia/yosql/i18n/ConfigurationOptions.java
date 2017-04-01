package de.xn__ho_hia.yosql.i18n;

import ch.qos.cal10n.BaseName;

/**
 * Enumeration of all known configuration options.
 */
@BaseName("yosql-core/resources/configuration-options")
public enum ConfigurationOptions {

    /**
     * The base input directory.
     */
    INPUT_BASE_DIRECTORY,

    /**
     * The description for {@link #INPUT_BASE_DIRECTORY}.
     */
    INPUT_BASE_DIRECTORY_DESCRIPTION,

    /**
     * The base output directory.
     */
    OUTPUT_BASE_DIRECTORY,

    /**
     * The description for {@link #OUTPUT_BASE_DIRECTORY}.
     */
    OUTPUT_BASE_DIRECTORY_DESCRIPTION,

    /**
     * The current directory.
     */
    CURRENT_DIRECTORY,

}
