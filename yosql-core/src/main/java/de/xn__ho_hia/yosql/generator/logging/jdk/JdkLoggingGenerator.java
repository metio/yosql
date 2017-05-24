/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.logging.jdk;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.logging.shared.AbstractLoggingGenerator;

@JDK
@SuppressWarnings({ "nls", "javadoc" })
public final class JdkLoggingGenerator extends AbstractLoggingGenerator {

    private final TypicalFields fields;

    @Inject
    public JdkLoggingGenerator(final TypicalFields fields) {
        this.fields = fields;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.of(fields.prepareConstant(getClass(), Logger.class, TypicalNames.LOGGER)
                .initializer("$T.getLogger($T.class.getName())", Logger.class, repoClass)
                .build());
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$N.entering($S, $S)", TypicalNames.LOGGER, repository, method)
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$N.isLoggable($T.FINE)", TypicalNames.LOGGER, Level.class).build();
    }

    @Override
    protected String info() {
        return "fine";
    }

    @Override
    protected String debug() {
        return "finer";
    }

    @Override
    protected String canLog() {
        return "isLoggable";
    }

}
