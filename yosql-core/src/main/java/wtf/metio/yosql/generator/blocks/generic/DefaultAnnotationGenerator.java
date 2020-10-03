/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.configuration.AnnotationConfiguration;
import wtf.metio.yosql.model.options.AnnotationClassOptions;
import wtf.metio.yosql.model.options.AnnotationMemberOptions;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.EnumSet;

final class DefaultAnnotationGenerator implements AnnotationGenerator {

    private static final EnumSet<AnnotationMemberOptions> OPTIONS_WITH_VALUE = EnumSet.of(
            AnnotationMemberOptions.ALL,
            AnnotationMemberOptions.VALUE,
            AnnotationMemberOptions.WITHOUT_DATE);

    private static final EnumSet<AnnotationMemberOptions> OPTIONS_WITH_DATE = EnumSet.of(
            AnnotationMemberOptions.ALL,
            AnnotationMemberOptions.DATE);

    private static final EnumSet<AnnotationMemberOptions> OPTIONS_WITH_COMMENT = EnumSet.of(
            AnnotationMemberOptions.ALL,
            AnnotationMemberOptions.COMMENT,
            AnnotationMemberOptions.WITHOUT_DATE);

    private final AnnotationConfiguration configuration;
    private final Translator translator;

    DefaultAnnotationGenerator(
            final AnnotationConfiguration configuration,
            final Translator translator) {
        this.configuration = configuration;
        this.translator = translator;
    }

    @Override
    public Iterable<AnnotationSpec> generatedClass() {
        return getAnnotationSpecs(
                configuration.classAnnotation(),
                configuration.classMembers(),
                configuration.classComment());
    }

    @Override
    public Iterable<AnnotationSpec> generatedField() {
        return getAnnotationSpecs(
                configuration.fieldAnnotation(),
                configuration.fieldMembers(),
                configuration.fieldComment());
    }

    @Override
    public Iterable<AnnotationSpec> generatedMethod() {
        return getAnnotationSpecs(
                configuration.methodAnnotation(),
                configuration.methodMembers(),
                configuration.methodComment());
    }

    private Iterable<AnnotationSpec> getAnnotationSpecs(
            final AnnotationClassOptions classOption,
            final AnnotationMemberOptions memberOption,
            final String comment) {
        if (AnnotationClassOptions.NONE != classOption) {
            final var annotationClass = ClassName.bestGuess(translator.get(classOption));
            final var builder = AnnotationSpec.builder(annotationClass);
            if (OPTIONS_WITH_VALUE.contains(memberOption)) {
                builder.addMember("value", "$S", configuration.generatorName());
            }
            if (OPTIONS_WITH_DATE.contains(memberOption)) {
                builder.addMember("date", "$S", ZonedDateTime.now().toString());
            }
            if (OPTIONS_WITH_COMMENT.contains(memberOption)) {
                builder.addMember("comments", "$S", comment);
            }
            return Collections.singletonList(builder.build());
        }
        return Collections.emptyList();
    }

}
