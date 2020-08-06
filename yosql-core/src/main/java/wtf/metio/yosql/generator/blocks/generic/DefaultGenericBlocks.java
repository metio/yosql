package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.generator.blocks.api.GenericBlocks;
import wtf.metio.yosql.model.sql.ResultRowConverter;

final class DefaultGenericBlocks implements GenericBlocks {

    @Override
    public CodeBlock returnTrue() {
        return CodeBlock.builder().addStatement("return $L", true).build();
    }

    @Override
    public CodeBlock returnFalse() {
        return CodeBlock.builder().addStatement("return $L", false).build();
    }

    @Override
    public CodeBlock returnValue(final CodeBlock value) {
        return CodeBlock.builder()
                .addStatement("return $L", value)
                .build();
    }

    @Override
    public CodeBlock close(final String resource) {
        return CodeBlock.builder().addStatement("$N.close()", resource).build();
    }

    @Override
    public CodeBlock initializeFieldToSelf(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("this.$N = $N", fieldName, fieldName)
                .build();
    }

    @Override
    public CodeBlock initializeConverter(final ResultRowConverter converter) {
        final ClassName converterClass = ClassName.bestGuess(converter.getConverterType());
        return CodeBlock.builder()
                .addStatement("this.$N = new $T()", converter.getAlias(), converterClass)
                .build();
    }

}
