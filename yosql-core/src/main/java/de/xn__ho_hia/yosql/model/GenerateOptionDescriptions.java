package de.xn__ho_hia.yosql.model;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of all known generate command configuration option descriptions.
 */
@LocaleData(@Locale("en"))
@BaseName("generate-option-descriptions")
public enum GenerateOptionDescriptions {

    /**
     * The description for {@link GenerateOptions#INPUT_BASE_DIRECTORY}.
     */
    INPUT_BASE_DIRECTORY_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#OUTPUT_BASE_DIRECTORY}.
     */
    OUTPUT_BASE_DIRECTORY_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#BASE_PACKAGE_NAME}.
     */
    BASE_PACKAGE_NAME_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#UTILITY_PACKAGE_NAME}.
     */
    UTILITY_PACKAGE_NAME_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#CONVERTER_PACKAGE_NAME}.
     */
    CONVERTER_PACKAGE_NAME_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#JAVA}.
     */
    JAVA_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#REPOSITORY_NAME_SUFFIX}.
     */
    REPOSITORY_NAME_SUFFIX_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#SQL_FILES_SUFFIX}.
     */
    SQL_FILES_SUFFIX_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#SQL_FILES_CHARSET}.
     */
    SQL_FILES_CHARSET_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#SQL_STATEMENT_SEPARATOR}.
     */
    SQL_STATEMENT_SEPARATOR_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#DEFAULT_ROW_CONVERTER}.
     */
    DEFAULT_ROW_CONVERTER_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_BATCH_PREFIX}.
     */
    METHOD_BATCH_PREFIX_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_BATCH_SUFFIX}.
     */
    METHOD_BATCH_SUFFIX_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_STREAM_PREFIX}.
     */
    METHOD_STREAM_PREFIX_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_STREAM_SUFFIX}.
     */
    METHOD_STREAM_SUFFIX_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_RXJAVA_PREFIX}.
     */
    METHOD_RXJAVA_PREFIX_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_RXJAVA_SUFFIX}.
     */
    METHOD_RXJAVA_SUFFIX_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_EAGER_NAME}.
     */
    METHOD_EAGER_NAME_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_LAZY_NAME}.
     */
    METHOD_LAZY_NAME_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_STANDARD_API}.
     */
    METHOD_STANDARD_API_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_BATCH_API}.
     */
    METHOD_BATCH_API_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_RXJAVA_API}.
     */
    METHOD_RXJAVA_API_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_STREAM_EAGER_API}.
     */
    METHOD_STREAM_EAGER_API_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_STREAM_LAZY_API}.
     */
    METHOD_STREAM_LAZY_API_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_ALLOWED_CALL_PREFIXES}.
     */
    METHOD_ALLOWED_CALL_PREFIXES_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_ALLOWED_READ_PREFIXES}.
     */
    METHOD_ALLOWED_READ_PREFIXES_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_ALLOWED_WRITE_PREFIXES}.
     */
    METHOD_ALLOWED_WRITE_PREFIXES_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_VALIDATE_NAME_PREFIXES}.
     */
    METHOD_VALIDATE_NAME_PREFIXES_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#METHOD_CATCH_AND_RETHROW}.
     */
    METHOD_CATCH_AND_RETHROW_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#GENERATED_ANNOTATION_CLASS}.
     */
    GENERATED_ANNOTATION_CLASS_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#GENERATED_ANNOTATION_FIELD}.
     */
    GENERATED_ANNOTATION_FIELD_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#GENERATED_ANNOTATION_METHOD}.
     */
    GENERATED_ANNOTATION_METHOD_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#GENERATED_ANNOTATION_COMMENT}.
     */
    GENERATED_ANNOTATION_COMMENT_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#REPOSITORY_GENERATE_INTERFACE}.
     */
    REPOSITORY_GENERATE_INTERFACE_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#DEFAULT_FLOW_STATE_CLASS_NAME}.
     */
    DEFAULT_FLOW_STATE_CLASS_NAME_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#DEFAULT_RESULT_STATE_CLASS_NAME}.
     */
    DEFAULT_RESULT_STATE_CLASS_NAME_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#DEFAULT_RESULT_ROW_CLASS_NAME}.
     */
    DEFAULT_RESULT_ROW_CLASS_NAME_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#LOGGING_API}.
     */
    LOGGING_API_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#LOG_LEVEL}.
     */
    LOG_LEVEL_DESCRIPTION,

    /**
     * The description for {@link GenerateOptions#RESULT_ROW_CONVERTERS}.
     */
    RESULT_ROW_CONVERTERS_DESCRIPTION,

}
