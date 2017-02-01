package com.github.sebhoss.yosql.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.stream.StreamSupport;

import com.github.sebhoss.yosql.SqlStatementConfiguration;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeSpec;

import io.reactivex.Flowable;

public class TypicalCodeBlocks {

    public static CodeBlock setFieldToSelf(final String name) {
        return CodeBlock.builder()
                .addStatement("this.$N = $N", name, name)
                .build();
    }

    public static CodeBlock getMetaData() {
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.getMetaData()", ResultSetMetaData.class, TypicalNames.META_DATA,
                        TypicalNames.RESULT_SET)
                .build();
    }

    public static CodeBlock getColumnCount() {
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.getColumnCount()", int.class, TypicalNames.COLUMN_COUNT,
                        TypicalNames.META_DATA)
                .build();
    }

    public static CodeBlock executeQuery() {
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.executeQuery()", ResultSet.class, TypicalNames.RESULT_SET,
                        TypicalNames.PREPARED_STATEMENT)
                .build();
    }

    public static CodeBlock getConnection() {
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.getConnection()", Connection.class, TypicalNames.CONNECTION,
                        TypicalNames.DATA_SOURCE)
                .build();
    }

    public static CodeBlock prepareStatement(final SqlStatementConfiguration configuration) {
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.prepareStatement($N)", PreparedStatement.class,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                        TypicalFields.constantSqlStatementFieldName(configuration))
                .build();
    }

    public static CodeBlock newResultState(final ClassName resultState) {
        return CodeBlock.builder()
                .addStatement("final $T $N = new $T($N, $N, $N)", resultState, TypicalNames.RESULT, resultState,
                        TypicalNames.RESULT_SET, TypicalNames.META_DATA, TypicalNames.COLUMN_COUNT)
                .build();
    }

    public static CodeBlock newFlowState(final ClassName flowState) {
        return CodeBlock.builder()
                .addStatement("return new $T($N, $N, $N, $N, $N)", flowState, TypicalNames.CONNECTION,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.RESULT_SET, TypicalNames.META_DATA,
                        TypicalNames.COLUMN_COUNT)
                .build();
    }

    public static CodeBlock newFlowable(final TypeSpec initialState, final TypeSpec generator,
            final TypeSpec disposer) {
        return CodeBlock.builder()
                .addStatement("return $T.generate($L, $L, $L)", Flowable.class, initialState, generator, disposer)
                .build();
    }

    public static CodeBlock streamStatefull(final TypeSpec spliterator, final TypeSpec closer) {
        return CodeBlock.builder()
                .addStatement("return $T.stream($L, false).onClose($L)", StreamSupport.class, spliterator, closer)
                .build();
    }

}
