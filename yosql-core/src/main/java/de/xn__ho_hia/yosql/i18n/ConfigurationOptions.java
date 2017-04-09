package de.xn__ho_hia.yosql.i18n;

import ch.qos.cal10n.BaseName;

/**
 * Enumeration of all known configuration options.
 */
@BaseName("configuration-options")
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

    /**
     * The base package name.
     */
    BASE_PACKAGE_NAME,

    /**
     * The description for {@link #BASE_PACKAGE_NAME}.
     */
    BASE_PACKAGE_NAME_DESCRIPTION,

    /**
     * The default value for {@link #BASE_PACKAGE_NAME}.
     */
    BASE_PACKAGE_NAME_DEFAULT,

    /**
     * The utility package name.
     */
    UTILITY_PACKAGE_NAME,

    /**
     * The description for {@link #UTILITY_PACKAGE_NAME}.
     */
    UTILITY_PACKAGE_NAME_DESCRIPTION,

    /**
     * The default value for {@link #UTILITY_PACKAGE_NAME}.
     */
    UTILITY_PACKAGE_NAME_DEFAULT,

    /**
     * The converter package name.
     */
    CONVERTER_PACKAGE_NAME,

    /**
     * The description for {@link #CONVERTER_PACKAGE_NAME}.
     */
    CONVERTER_PACKAGE_NAME_DESCRIPTION,

    /**
     * The default value for {@link #CONVERTER_PACKAGE_NAME}.
     */
    CONVERTER_PACKAGE_NAME_DEFAULT,

    /**
     * The java version number.
     */
    JAVA,

    /**
     * The description for {@link #JAVA}.
     */
    JAVA_DESCRIPTION,

    /**
     * The default value for {@link #JAVA}.
     */
    JAVA_DEFAULT,

    /**
     * The repository name suffix.
     */
    REPOSITORY_NAME_SUFFIX,

    /**
     * The description for {@link #REPOSITORY_NAME_SUFFIX}.
     */
    REPOSITORY_NAME_SUFFIX_DESCRIPTION,

    /**
     * The default value for {@link #REPOSITORY_NAME_SUFFIX}.
     */
    REPOSITORY_NAME_SUFFIX_DEFAULT,

    /**
     * The charset for .sql files.
     */
    SQL_FILES_CHARSET,

    /**
     * The description for {@link #SQL_FILES_CHARSET}.
     */
    SQL_FILES_CHARSET_DESCRIPTION,

    /**
     * The default value for {@link #SQL_FILES_CHARSET}.
     */
    SQL_FILES_CHARSET_DEFAULT,

    /**
     * The SQL statement separator.
     */
    SQL_STATEMENT_SEPARATOR,

    /**
     * The description for {@link #SQL_STATEMENT_SEPARATOR}.
     */
    SQL_STATEMENT_SEPARATOR_DESCRIPTION,

    /**
     * The default value for {@link #SQL_STATEMENT_SEPARATOR}.
     */
    SQL_STATEMENT_SEPARATOR_DEFAULT,

    /**
     * The default row converter.
     */
    DEFAULT_ROW_CONVERTER,

    /**
     * The description for {@link #DEFAULT_ROW_CONVERTER}.
     */
    DEFAULT_ROW_CONVERTER_DESCRIPTION,

    /**
     * The default value for {@link #DEFAULT_ROW_CONVERTER}.
     */
    DEFAULT_ROW_CONVERTER_DEFAULT,

    /**
     * The batch method name prefix.
     */
    METHOD_BATCH_PREFIX,

    /**
     * The description for {@link #METHOD_BATCH_PREFIX}.
     */
    METHOD_BATCH_PREFIX_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_BATCH_PREFIX}.
     */
    METHOD_BATCH_PREFIX_DEFAULT,

    /**
     * The batch method name suffix.
     */
    METHOD_BATCH_SUFFIX,

    /**
     * The description for {@link #METHOD_BATCH_SUFFIX}.
     */
    METHOD_BATCH_SUFFIX_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_BATCH_SUFFIX}.
     */
    METHOD_BATCH_SUFFIX_DEFAULT,

    /**
     * The stream method name prefix.
     */
    METHOD_STREAM_PREFIX,

    /**
     * The description for {@link #METHOD_STREAM_PREFIX}.
     */
    METHOD_STREAM_PREFIX_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_STREAM_PREFIX}.
     */
    METHOD_STREAM_PREFIX_DEFAULT,

    /**
     * The stream method name suffix.
     */
    METHOD_STREAM_SUFFIX,

    /**
     * The description for {@link #METHOD_STREAM_SUFFIX}.
     */
    METHOD_STREAM_SUFFIX_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_STREAM_SUFFIX}.
     */
    METHOD_STREAM_SUFFIX_DEFAULT,

    /**
     * The RxJava method name prefix.
     */
    METHOD_RXJAVA_PREFIX,

    /**
     * The description for {@link #METHOD_RXJAVA_PREFIX}.
     */
    METHOD_RXJAVA_PREFIX_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_RXJAVA_PREFIX}.
     */
    METHOD_RXJAVA_PREFIX_DEFAULT,

    /**
     * The RxJava method name suffix.
     */
    METHOD_RXJAVA_SUFFIX,

    /**
     * The description for {@link #METHOD_RXJAVA_SUFFIX}.
     */
    METHOD_RXJAVA_SUFFIX_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_RXJAVA_SUFFIX}.
     */
    METHOD_RXJAVA_SUFFIX_DEFAULT,

    /**
     * The method eager name.
     */
    METHOD_EAGER_NAME,

    /**
     * The description for {@link #METHOD_EAGER_NAME}.
     */
    METHOD_EAGER_NAME_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_EAGER_NAME}.
     */
    METHOD_EAGER_NAME_DEFAULT,

    /**
     * The method lazy name.
     */
    METHOD_LAZY_NAME,

    /**
     * The description for {@link #METHOD_LAZY_NAME}.
     */
    METHOD_LAZY_NAME_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_LAZY_NAME}.
     */
    METHOD_LAZY_NAME_DEFAULT,

    /**
     * Generate standard API.
     */
    METHOD_STANDARD_API,

    /**
     * The description for {@link #METHOD_STANDARD_API}.
     */
    METHOD_STANDARD_API_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_STANDARD_API}.
     */
    METHOD_STANDARD_API_DEFAULT,

    /**
     * Generate batch API.
     */
    METHOD_BATCH_API,

    /**
     * The description for {@link #METHOD_BATCH_API}.
     */
    METHOD_BATCH_API_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_BATCH_API}.
     */
    METHOD_BATCH_API_DEFAULT,

    /**
     * Generate RxJava API.
     */
    METHOD_RXJAVA_API,

    /**
     * The description for {@link #METHOD_RXJAVA_API}.
     */
    METHOD_RXJAVA_API_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_RXJAVA_API}.
     */
    METHOD_RXJAVA_API_DEFAULT,

    /**
     * Generate eager stream API.
     */
    METHOD_STREAM_EAGER_API,

    /**
     * The description for {@link #METHOD_STREAM_EAGER_API}.
     */
    METHOD_STREAM_EAGER_API_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_STREAM_EAGER_API}.
     */
    METHOD_STREAM_EAGER_API_DEFAULT,

    /**
     * Generate lazy stream API.
     */
    METHOD_STREAM_LAZY_API,

    /**
     * The description for {@link #METHOD_STREAM_LAZY_API}.
     */
    METHOD_STREAM_LAZY_API_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_STREAM_LAZY_API}.
     */
    METHOD_STREAM_LAZY_API_DEFAULT,

    /**
     * Allowed call prefixes.
     */
    METHOD_ALLOWED_CALL_PREFIXES,

    /**
     * The description for {@link #METHOD_ALLOWED_CALL_PREFIXES}.
     */
    METHOD_ALLOWED_CALL_PREFIXES_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_ALLOWED_CALL_PREFIXES}.
     */
    METHOD_ALLOWED_CALL_PREFIXES_DEFAULT,

    /**
     * Allowed read prefixes.
     */
    METHOD_ALLOWED_READ_PREFIXES,

    /**
     * The description for {@link #METHOD_ALLOWED_READ_PREFIXES}.
     */
    METHOD_ALLOWED_READ_PREFIXES_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_ALLOWED_READ_PREFIXES}.
     */
    METHOD_ALLOWED_READ_PREFIXES_DEFAULT,

    /**
     * Allowed write prefixes.
     */
    METHOD_ALLOWED_WRITE_PREFIXES,

    /**
     * The description for {@link #METHOD_ALLOWED_WRITE_PREFIXES}.
     */
    METHOD_ALLOWED_WRITE_PREFIXES_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_ALLOWED_WRITE_PREFIXES}.
     */
    METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT,

    /**
     * Validate method name prefix usage.
     */
    METHOD_VALIDATE_NAME_PREFIXES,

    /**
     * The description for {@link #METHOD_VALIDATE_NAME_PREFIXES}.
     */
    METHOD_VALIDATE_NAME_PREFIXES_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_VALIDATE_NAME_PREFIXES}.
     */
    METHOD_VALIDATE_NAME_PREFIXES_DEFAULT,

    /**
     * Catch and rethrow exceptions.
     */
    METHOD_CATCH_AND_RETHROW,

    /**
     * The description for {@link #METHOD_CATCH_AND_RETHROW}.
     */
    METHOD_CATCH_AND_RETHROW_DESCRIPTION,

    /**
     * The default value for {@link #METHOD_CATCH_AND_RETHROW}.
     */
    METHOD_CATCH_AND_RETHROW_DEFAULT,

}
