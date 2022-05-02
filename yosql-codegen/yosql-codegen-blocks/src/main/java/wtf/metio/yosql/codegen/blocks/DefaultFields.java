/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.codegen.api.AnnotationGenerator;
import wtf.metio.yosql.codegen.api.Fields;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.immutables.JavaConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;

public final class DefaultFields implements Fields {

    private static final String NAME_REGEX = "([a-z])([A-Z])";
    private static final String NAME_REPLACEMENT = "$1_$2";

    private final AnnotationGenerator annotations;
    private final JavaConfiguration java;
    private final NamesConfiguration names;

    public DefaultFields(
            final AnnotationGenerator annotations,
            final JavaConfiguration java,
            final NamesConfiguration names) {
        this.annotations = annotations;
        this.java = java;
        this.names = names;
    }

    @Override
    public FieldSpec field(final Type type, final String name) {
        return field(TypeName.get(type), name);
    }

    @Override
    public FieldSpec field(final TypeName type, final String name) {
        final var builder = builder(type, name).addModifiers(Modifier.PRIVATE);
        if (java.useFinal()) {
            builder.addModifiers(Modifier.FINAL);
        }
        return builder.build();
    }

    @Override
    public FieldSpec.Builder prepareConstant(final Type type, final String name) {
        return prepareConstant(TypeName.get(type), name);
    }

    @Override
    public FieldSpec.Builder prepareConstant(final TypeName type, final String name) {
        return builder(type, name).addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);
    }

    @Override
    public CodeBlock initialize(final String statement) {
        if (java.useTextBlocks()) {
            return CodeBlock.builder()
                    .add("\"\"\"")
                    .add("$>$>\n$L", statement)
                    .add("\"\"\"$<$<")
                    .build();
        }
        return CodeBlock.builder()
                .add("$S", statement)
                .build();
    }

    private FieldSpec.Builder builder(final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addAnnotations(annotations.generatedField());
    }

    @Override
    public String constantSqlStatementFieldName(final SqlConfiguration configuration) {
        return configuration.name()
                .replaceAll(NAME_REGEX, NAME_REPLACEMENT)
                .toUpperCase()
                + getVendor(configuration);
    }

    @Override
    public String constantRawSqlStatementFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + names.rawSuffix();
    }

    @Override
    public String constantSqlStatementParameterIndexFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + names.indexSuffix();
    }

    private static String getVendor(final SqlConfiguration configuration) {
        return Strings.isBlank(configuration.vendor())
                ? ""
                : "_" + configuration.vendor().replace(" ", "_").toUpperCase();
    }

}
