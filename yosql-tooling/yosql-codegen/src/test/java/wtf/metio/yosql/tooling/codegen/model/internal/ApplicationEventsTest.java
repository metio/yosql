/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.model.internal;

import org.junit.jupiter.api.DisplayName;
import wtf.metio.yosql.internals.utils.test.EnumTCK;
import wtf.metio.yosql.internals.utils.test.PropertiesTCK;

import java.util.stream.Stream;

@DisplayName("ApplicationEvents")
final class ApplicationEventsTest implements EnumTCK<ApplicationEvents>, PropertiesTCK<ApplicationEvents> {

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
                "STATEMENT_PARSING_STARTING",
                "STATEMENT_PARSING_FINISHED",
                "FILE_PARSING_FAILED",
                "TYPE_GENERATED",
                "READ_FILES",
                "READ_FILES_FAILED",
                "CONSIDER_FILE",
                "ENCOUNTER_FILE",
                "PARSE_FILES",
                "PARSE_FILES_FAILED",
                "CODE_GENERATION_FAILED",
                "GENERATE_REPOSITORIES",
                "GENERATE_UTILITIES",
                "WRITE_FILES",
                "STATEMENT_PARSED",
                "STATEMENT_YAML_FRONT_MATTER_PARSED",
                "REPOSITORY_NAME_CALC_INPUT",
                "REPOSITORY_NAME_CALC_SOURCE",
                "REPOSITORY_NAME_CALC_RELATIVE",
                "REPOSITORY_NAME_CALC_RAW",
                "REPOSITORY_NAME_CALC_DOTTED",
                "REPOSITORY_NAME_CALC_UPPER",
                "REPOSITORY_NAME_CALC_ACTUAL",
                "REPOSITORY_NAME_CALC_NAME");
    }

}
