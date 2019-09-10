/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.model.options;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of all known 'generate' configuration options.
 */
@LocaleData(@Locale("en"))
@BaseName("generate-options")
public enum GenerateOptions {

    /**
     * The maximum number of threads to use.
     */
    MAX_THREADS,

    /**
     * Default value for {@link #MAX_THREADS}.
     */
    MAX_THREADS_DEFAULT,

    /**
     * The base input directory.
     */
    INPUT_BASE_DIRECTORY,

    /**
     * The base output directory.
     */
    OUTPUT_BASE_DIRECTORY,

    /**
     * The current directory.
     */
    CURRENT_DIRECTORY,

    /**
     * The base package name.
     */
    BASE_PACKAGE_NAME,

    /**
     * The default value for {@link #BASE_PACKAGE_NAME}.
     */
    BASE_PACKAGE_NAME_DEFAULT,

    /**
     * The utility package name.
     */
    UTILITY_PACKAGE_NAME,

    /**
     * The default value for {@link #UTILITY_PACKAGE_NAME}.
     */
    UTILITY_PACKAGE_NAME_DEFAULT,

    /**
     * The converter package name.
     */
    CONVERTER_PACKAGE_NAME,

    /**
     * The default value for {@link #CONVERTER_PACKAGE_NAME}.
     */
    CONVERTER_PACKAGE_NAME_DEFAULT,

    /**
     * The java version number.
     */
    JAVA,

    /**
     * The default value for {@link #JAVA}.
     */
    JAVA_DEFAULT,

    /**
     * The repository name suffix.
     */
    REPOSITORY_NAME_SUFFIX,

    /**
     * The default value for {@link #REPOSITORY_NAME_SUFFIX}.
     */
    REPOSITORY_NAME_SUFFIX_DEFAULT,

    /**
     * The file name suffix for SQL files.
     */
    SQL_FILES_SUFFIX,

    /**
     * The default value for {@link #SQL_FILES_SUFFIX}.
     */
    SQL_FILES_SUFFIX_DEFAULT,

    /**
     * The charset for .sql files.
     */
    SQL_FILES_CHARSET,

    /**
     * The default value for {@link #SQL_FILES_CHARSET}.
     */
    SQL_FILES_CHARSET_DEFAULT,

    /**
     * The SQL statement separator.
     */
    SQL_STATEMENT_SEPARATOR,

    /**
     * The default value for {@link #SQL_STATEMENT_SEPARATOR}.
     */
    SQL_STATEMENT_SEPARATOR_DEFAULT,

    /**
     * The default row converter.
     */
    DEFAULT_ROW_CONVERTER,

    /**
     * The default value for {@link #DEFAULT_ROW_CONVERTER}.
     */
    DEFAULT_ROW_CONVERTER_DEFAULT,

    /**
     * The batch method name prefix.
     */
    METHOD_BATCH_PREFIX,

    /**
     * The default value for {@link #METHOD_BATCH_PREFIX}.
     */
    METHOD_BATCH_PREFIX_DEFAULT,

    /**
     * The batch method name suffix.
     */
    METHOD_BATCH_SUFFIX,

    /**
     * The default value for {@link #METHOD_BATCH_SUFFIX}.
     */
    METHOD_BATCH_SUFFIX_DEFAULT,

    /**
     * The stream method name prefix.
     */
    METHOD_STREAM_PREFIX,

    /**
     * The default value for {@link #METHOD_STREAM_PREFIX}.
     */
    METHOD_STREAM_PREFIX_DEFAULT,

    /**
     * The stream method name suffix.
     */
    METHOD_STREAM_SUFFIX,

    /**
     * The default value for {@link #METHOD_STREAM_SUFFIX}.
     */
    METHOD_STREAM_SUFFIX_DEFAULT,

    /**
     * The RxJava method name prefix.
     */
    METHOD_RXJAVA_PREFIX,

    /**
     * The default value for {@link #METHOD_RXJAVA_PREFIX}.
     */
    METHOD_RXJAVA_PREFIX_DEFAULT,

    /**
     * The RxJava method name suffix.
     */
    METHOD_RXJAVA_SUFFIX,

    /**
     * The default value for {@link #METHOD_RXJAVA_SUFFIX}.
     */
    METHOD_RXJAVA_SUFFIX_DEFAULT,

    /**
     * The method eager name.
     */
    METHOD_EAGER_NAME,

    /**
     * The default value for {@link #METHOD_EAGER_NAME}.
     */
    METHOD_EAGER_NAME_DEFAULT,

    /**
     * The method lazy name.
     */
    METHOD_LAZY_NAME,

    /**
     * The default value for {@link #METHOD_LAZY_NAME}.
     */
    METHOD_LAZY_NAME_DEFAULT,

    /**
     * Generate generic API.
     */
    METHOD_STANDARD_API,

    /**
     * The default value for {@link #METHOD_STANDARD_API}.
     */
    METHOD_STANDARD_API_DEFAULT,

    /**
     * Generate batch API.
     */
    METHOD_BATCH_API,

    /**
     * The default value for {@link #METHOD_BATCH_API}.
     */
    METHOD_BATCH_API_DEFAULT,

    /**
     * Generate RxJava API.
     */
    METHOD_RXJAVA_API,

    /**
     * The default value for {@link #METHOD_RXJAVA_API}.
     */
    METHOD_RXJAVA_API_DEFAULT,

    /**
     * Generate eager stream API.
     */
    METHOD_STREAM_EAGER_API,

    /**
     * The default value for {@link #METHOD_STREAM_EAGER_API}.
     */
    METHOD_STREAM_EAGER_API_DEFAULT,

    /**
     * Generate lazy stream API.
     */
    METHOD_STREAM_LAZY_API,

    /**
     * The default value for {@link #METHOD_STREAM_LAZY_API}.
     */
    METHOD_STREAM_LAZY_API_DEFAULT,

    /**
     * Allowed call prefixes.
     */
    METHOD_ALLOWED_CALL_PREFIXES,

    /**
     * The default value for {@link #METHOD_ALLOWED_CALL_PREFIXES}.
     */
    METHOD_ALLOWED_CALL_PREFIXES_DEFAULT,

    /**
     * Allowed read prefixes.
     */
    METHOD_ALLOWED_READ_PREFIXES,

    /**
     * The default value for {@link #METHOD_ALLOWED_READ_PREFIXES}.
     */
    METHOD_ALLOWED_READ_PREFIXES_DEFAULT,

    /**
     * Allowed write prefixes.
     */
    METHOD_ALLOWED_WRITE_PREFIXES,

    /**
     * The default value for {@link #METHOD_ALLOWED_WRITE_PREFIXES}.
     */
    METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT,

    /**
     * Validate method name prefix usage.
     */
    METHOD_VALIDATE_NAME_PREFIXES,

    /**
     * The default value for {@link #METHOD_VALIDATE_NAME_PREFIXES}.
     */
    METHOD_VALIDATE_NAME_PREFIXES_DEFAULT,

    /**
     * Catch and rethrow exceptions.
     */
    METHOD_CATCH_AND_RETHROW,

    /**
     * The default value for {@link #METHOD_CATCH_AND_RETHROW}.
     */
    METHOD_CATCH_AND_RETHROW_DEFAULT,

    /**
     * The @Generated annotation for classes.
     */
    GENERATED_ANNOTATION_CLASS,

    /**
     * The default value for {@link #GENERATED_ANNOTATION_CLASS}.
     */
    GENERATED_ANNOTATION_CLASS_DEFAULT,

    /**
     * The @Generated annotation for fields.
     */
    GENERATED_ANNOTATION_FIELD,

    /**
     * The default value for {@link #GENERATED_ANNOTATION_FIELD}.
     */
    GENERATED_ANNOTATION_FIELD_DEFAULT,

    /**
     * The @Generated annotation for methods.
     */
    GENERATED_ANNOTATION_METHOD,

    /**
     * The default value for {@link #GENERATED_ANNOTATION_METHOD}.
     */
    GENERATED_ANNOTATION_METHOD_DEFAULT,

    /**
     * The @Generated annotation comment.
     */
    GENERATED_ANNOTATION_COMMENT,

    /**
     * The default value for {@link #GENERATED_ANNOTATION_COMMENT}.
     */
    GENERATED_ANNOTATION_COMMENT_DEFAULT,

    /**
     * Generate interfaces for repositories.
     */
    REPOSITORY_GENERATE_INTERFACE,

    /**
     * The default value for {@link #REPOSITORY_GENERATE_INTERFACE}.
     */
    REPOSITORY_GENERATE_INTERFACE_DEFAULT,

    /**
     * Default name of flow state class.
     */
    DEFAULT_FLOW_STATE_CLASS_NAME,

    /**
     * The default value for {@link #DEFAULT_FLOW_STATE_CLASS_NAME}.
     */
    DEFAULT_FLOW_STATE_CLASS_NAME_DEFAULT,

    /**
     * Default name of result state class.
     */
    DEFAULT_RESULT_STATE_CLASS_NAME,

    /**
     * The default value for {@link #DEFAULT_RESULT_STATE_CLASS_NAME}.
     */
    DEFAULT_RESULT_STATE_CLASS_NAME_DEFAULT,

    /**
     * Default name of result row class.
     */
    DEFAULT_RESULT_ROW_CLASS_NAME,

    /**
     * The default value for {@link #DEFAULT_RESULT_ROW_CLASS_NAME}.
     */
    DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT,

    /**
     * The logging API to use.
     */
    LOGGING_API,

    /**
     * The default value for {@link #LOGGING_API}.
     */
    LOGGING_API_DEFAULT,

    /**
     * The logging level for the YoSql CLI.
     */
    LOG_LEVEL,

    /**
     * The default value for {@link #LOG_LEVEL}.
     */
    LOG_LEVEL_DEFAULT,

    /**
     * The custom result row converters.
     */
    RESULT_ROW_CONVERTERS,

    /**
     * The default value for {@link #RESULT_ROW_CONVERTERS}.
     */
    RESULT_ROW_CONVERTERS_DEFAULT,

    /**
     * The class name of the result row converter.
     */
    TO_RESULT_ROW_CONVERTER_CLASS_NAME,

}
