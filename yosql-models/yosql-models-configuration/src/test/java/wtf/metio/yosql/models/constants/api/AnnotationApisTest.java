/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.constants.api;

import wtf.metio.yosql.internals.junit5.EnumTCK;
import wtf.metio.yosql.models.configuration.AnnotationApis;

import java.util.stream.Stream;

class AnnotationApisTest implements EnumTCK<AnnotationApis> {

    @Override
    public Class<AnnotationApis> getEnumClass() {
        return AnnotationApis.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("AUTO", "ANNOTATION_API", "PROCESSING_API");
    }

}
