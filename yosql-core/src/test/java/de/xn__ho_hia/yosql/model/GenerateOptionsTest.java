package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

final class GenerateOptionsTest implements EnumTCK<GenerateOptions> {

    @Override
    public Class<GenerateOptions> getEnumClass() {
        return GenerateOptions.class;
    }

    @Override
    @SuppressWarnings("nls")
    public Stream<String> validValues() {
        return Stream.of(
                "INPUT_BASE_DIRECTORY",
                "OUTPUT_BASE_DIRECTORY",
                "CURRENT_DIRECTORY",
                "BASE_PACKAGE_NAME",
                "BASE_PACKAGE_NAME_DEFAULT",
                "UTILITY_PACKAGE_NAME",
                "UTILITY_PACKAGE_NAME_DEFAULT",
                "CONVERTER_PACKAGE_NAME",
                "CONVERTER_PACKAGE_NAME_DEFAULT",
                "JAVA",
                "JAVA_DEFAULT",
                "REPOSITORY_NAME_SUFFIX",
                "REPOSITORY_NAME_SUFFIX_DEFAULT",
                "SQL_FILES_SUFFIX",
                "SQL_FILES_SUFFIX_DEFAULT",
                "SQL_FILES_CHARSET",
                "SQL_FILES_CHARSET_DEFAULT",
                "SQL_STATEMENT_SEPARATOR",
                "SQL_STATEMENT_SEPARATOR_DEFAULT",
                "DEFAULT_ROW_CONVERTER",
                "DEFAULT_ROW_CONVERTER_DEFAULT",
                "METHOD_BATCH_PREFIX",
                "METHOD_BATCH_PREFIX_DEFAULT",
                "METHOD_BATCH_SUFFIX",
                "METHOD_BATCH_SUFFIX_DEFAULT",
                "METHOD_STREAM_PREFIX",
                "METHOD_STREAM_PREFIX_DEFAULT",
                "METHOD_STREAM_SUFFIX",
                "METHOD_STREAM_SUFFIX_DEFAULT",
                "METHOD_RXJAVA_PREFIX",
                "METHOD_RXJAVA_PREFIX_DEFAULT",
                "METHOD_RXJAVA_SUFFIX",
                "METHOD_RXJAVA_SUFFIX_DEFAULT",
                "METHOD_EAGER_NAME",
                "METHOD_EAGER_NAME_DEFAULT",
                "METHOD_LAZY_NAME",
                "METHOD_LAZY_NAME_DEFAULT",
                "METHOD_STANDARD_API",
                "METHOD_STANDARD_API_DEFAULT",
                "METHOD_BATCH_API",
                "METHOD_BATCH_API_DEFAULT",
                "METHOD_RXJAVA_API",
                "METHOD_RXJAVA_API_DEFAULT",
                "METHOD_STREAM_EAGER_API",
                "METHOD_STREAM_EAGER_API_DEFAULT",
                "METHOD_STREAM_LAZY_API",
                "METHOD_STREAM_LAZY_API_DEFAULT",
                "METHOD_ALLOWED_CALL_PREFIXES",
                "METHOD_ALLOWED_CALL_PREFIXES_DEFAULT",
                "METHOD_ALLOWED_READ_PREFIXES",
                "METHOD_ALLOWED_READ_PREFIXES_DEFAULT",
                "METHOD_ALLOWED_WRITE_PREFIXES",
                "METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT",
                "METHOD_VALIDATE_NAME_PREFIXES",
                "METHOD_VALIDATE_NAME_PREFIXES_DEFAULT",
                "METHOD_CATCH_AND_RETHROW",
                "METHOD_CATCH_AND_RETHROW_DEFAULT",
                "GENERATED_ANNOTATION_CLASS",
                "GENERATED_ANNOTATION_CLASS_DEFAULT",
                "GENERATED_ANNOTATION_FIELD",
                "GENERATED_ANNOTATION_FIELD_DEFAULT",
                "GENERATED_ANNOTATION_METHOD",
                "GENERATED_ANNOTATION_METHOD_DEFAULT",
                "GENERATED_ANNOTATION_COMMENT",
                "GENERATED_ANNOTATION_COMMENT_DEFAULT",
                "REPOSITORY_GENERATE_INTERFACE",
                "REPOSITORY_GENERATE_INTERFACE_DEFAULT",
                "DEFAULT_FLOW_STATE_CLASS_NAME",
                "DEFAULT_FLOW_STATE_CLASS_NAME_DEFAULT",
                "DEFAULT_RESULT_STATE_CLASS_NAME",
                "DEFAULT_RESULT_STATE_CLASS_NAME_DEFAULT",
                "DEFAULT_RESULT_ROW_CLASS_NAME",
                "DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT",
                "LOGGING_API",
                "LOGGING_API_DEFAULT",
                "LOG_LEVEL",
                "LOG_LEVEL_DEFAULT",
                "RESULT_ROW_CONVERTERS",
                "RESULT_ROW_CONVERTERS_DEFAULT",
                "TO_RESULT_ROW_CONVERTER_CLASS_NAME");
    }

}
