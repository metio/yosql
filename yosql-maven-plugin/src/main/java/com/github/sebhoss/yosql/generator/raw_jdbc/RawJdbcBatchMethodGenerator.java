package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.List;

import javax.inject.Inject;

import org.codehaus.plexus.component.annotations.Component;

import com.github.sebhoss.yosql.generator.AnnotationGenerator;
import com.github.sebhoss.yosql.generator.BatchMethodGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.generator.helpers.TypicalMethods;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

@Component(role = BatchMethodGenerator.class, hint = "raw-jdbc")
public class RawJdbcBatchMethodGenerator implements BatchMethodGenerator {

    private final AnnotationGenerator annotations;
    private final TypicalCodeBlocks   codeBlocks;

    @Inject
    public RawJdbcBatchMethodGenerator(
            final AnnotationGenerator annotations,
            final TypicalCodeBlocks codeBlocks) {
        this.annotations = annotations;
        this.codeBlocks = codeBlocks;
    }

    @Override
    public MethodSpec batchApi(final List<SqlStatement> sqlStatements) {
        final SqlConfiguration configuration = SqlConfiguration.merge(sqlStatements);
        return TypicalMethods.publicMethod(configuration.getBatchName())
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(configuration.getBatchParameterSpecs())
                .addExceptions(TypicalCodeBlocks.sqlException(configuration))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(codeBlocks.pickVendorQuery(sqlStatements))
                .addCode(TypicalCodeBlocks.tryPrepareStatement())
                .addCode(TypicalCodeBlocks.prepareBatch(configuration))
                .addCode(TypicalCodeBlocks.executeBatch())
                .addCode(TypicalCodeBlocks.endTryBlock(2))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(configuration))
                .build();
    }

}
