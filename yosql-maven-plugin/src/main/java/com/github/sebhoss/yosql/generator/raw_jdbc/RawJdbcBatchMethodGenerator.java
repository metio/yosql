package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.List;

import org.codehaus.plexus.component.annotations.Component;

import com.github.sebhoss.yosql.generator.BatchMethodGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.generator.helpers.TypicalModifiers;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.model.SqlStatementConfiguration;
import com.squareup.javapoet.MethodSpec;

@Component(role = BatchMethodGenerator.class, hint = "raw-jdbc")
public class RawJdbcBatchMethodGenerator implements BatchMethodGenerator {

    @Override
    public MethodSpec batchApi(final List<SqlStatement> sqlStatements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(sqlStatements);
        return MethodSpec.methodBuilder(configuration.getBatchName())
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(configuration.getBatchParameterSpecs())
                .addExceptions(TypicalCodeBlocks.sqlException(configuration))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(TypicalCodeBlocks.pickVendorQuery(sqlStatements))
                .addCode(TypicalCodeBlocks.tryPrepare())
                .addCode(TypicalCodeBlocks.prepareBatch(configuration))
                .addCode(TypicalCodeBlocks.executeBatch())
                .addCode(TypicalCodeBlocks.endTryBlock(2))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(configuration))
                .build();
    }

}
