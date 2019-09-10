package wtf.metio.yosql.generator.blocks.api;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.sql.ResultRowConverter;

public interface GenericBlocks {

    CodeBlock returnTrue();

    CodeBlock returnFalse();

    CodeBlock close(String resource);

    CodeBlock setFieldToSelf(String fieldName);

    CodeBlock returnValue(CodeBlock value);

    CodeBlock initializeConverter(ResultRowConverter converter);

}
