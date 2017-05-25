package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

@SuppressWarnings("nls")
final class ApplicationEventsTest implements EnumTCK<ApplicationEvents> {

    @Override
    public Class<ApplicationEvents> getEnumClass() {
        return ApplicationEvents.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of(
                "FILE_WRITE_FINISHED",
                "FILE_WRITE_FAILED",
                "TASK_RUNTIME",
                "APPLICATION_RUNTIME",
                "FILE_PARSING_STARTING",
                "FILE_PARSING_FINISHED",
                "FILE_PARSING_FAILED",
                "TYPE_GENERATED",
                "READ_FILES",
                "CONSIDER_FILE",
                "ENCOUNTER_FILE",
                "PARSE_FILES",
                "PARSE_FILES_FAILED",
                "CODE_GENERATION_FAILED",
                "GENERATE_REPOSITORIES",
                "GENERATE_UTILITIES",
                "WRITE_FILES",
                "WORKER_POOL_NAME",
                "FILE_SQL_STATEMENT_PARSED",
                "FILE_YAML_FRONTMATTER_PARSED");
    }

}
