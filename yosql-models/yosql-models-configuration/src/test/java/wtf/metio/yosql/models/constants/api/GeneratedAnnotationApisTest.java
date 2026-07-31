/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.models.constants.api;

import wtf.metio.yosql.internals.junit5.EnumTCK;
import wtf.metio.yosql.models.configuration.GeneratedAnnotationApis;

import java.util.stream.Stream;

class GeneratedAnnotationApisTest implements EnumTCK<GeneratedAnnotationApis> {

    @Override
    public Class<GeneratedAnnotationApis> getEnumClass() {
        return GeneratedAnnotationApis.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("ANNOTATION_API", "PROCESSING_API");
    }

}
