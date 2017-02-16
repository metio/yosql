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
    public MethodSpec batchMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return TypicalMethods.publicMethod(mergedConfiguration.getBatchName())
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(mergedConfiguration.getBatchParameterSpecs())
                .addExceptions(TypicalCodeBlocks.sqlException(mergedConfiguration))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(codeBlocks.pickVendorQuery(vendorStatements))
                .addCode(TypicalCodeBlocks.tryPrepareStatement())
                .addCode(TypicalCodeBlocks.prepareBatch(mergedConfiguration))
                .addCode(codeBlocks.logExecutedQuery(mergedConfiguration))
                .addCode(TypicalCodeBlocks.executeBatch())
                .addCode(TypicalCodeBlocks.endTryBlock(2))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(mergedConfiguration))
                .build();
    }

}
