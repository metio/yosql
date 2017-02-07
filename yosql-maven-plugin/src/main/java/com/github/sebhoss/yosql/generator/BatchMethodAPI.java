package com.github.sebhoss.yosql.generator;

import java.util.List;

import org.codehaus.plexus.component.annotations.Component;

import com.github.sebhoss.yosql.SqlStatement;
import com.github.sebhoss.yosql.SqlStatementConfiguration;
import com.github.sebhoss.yosql.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.helpers.TypicalModifiers;
import com.github.sebhoss.yosql.helpers.TypicalTypes;
import com.squareup.javapoet.MethodSpec;

@Component(role = MethodGenerator.class, hint = "batch")
public class BatchMethodAPI implements MethodGenerator {

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

    @Override
    public MethodSpec generateMethod(final String methodName, final List<SqlStatement> statements) {
        return batchApi(statements);
    }

    @Override
    public String getName() {
        return "batch";
    }

}
