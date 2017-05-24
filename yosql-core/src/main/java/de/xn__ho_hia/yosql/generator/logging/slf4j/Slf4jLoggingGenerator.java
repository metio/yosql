/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.logging.slf4j;

import java.util.Optional;

import javax.inject.Inject;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.logging.shared.AbstractLoggingGenerator;

@Slf4j
@SuppressWarnings({ "nls", "javadoc" })
public final class Slf4jLoggingGenerator extends AbstractLoggingGenerator {

    private final TypicalFields fields;

    @Inject
    public Slf4jLoggingGenerator(final TypicalFields fields) {
        this.fields = fields;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.of(fields.prepareConstant(getClass(), Logger.class, TypicalNames.LOGGER)
                .initializer("$T.getLogger($T.class)", LoggerFactory.class, repoClass)
                .build());
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$N.isInfoEnabled()", TypicalNames.LOGGER).build();
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$N.debug($T.format($S, $S, $S))", TypicalNames.LOGGER, String.class,
                        "Entering [%s#%s]", repository, method)
                .build();
    }

}
