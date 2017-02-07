package com.github.sebhoss.yosql.generator;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.SqlStatement;
import com.github.sebhoss.yosql.SqlStatementConfiguration;
import com.github.sebhoss.yosql.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.helpers.TypicalModifiers;
import com.github.sebhoss.yosql.helpers.TypicalTypes;
import com.squareup.javapoet.MethodSpec;

@Named
@Singleton
public class BatchAPI {

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
