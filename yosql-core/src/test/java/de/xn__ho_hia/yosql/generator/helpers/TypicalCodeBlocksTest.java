package de.xn__ho_hia.yosql.generator.helpers;

import com.squareup.javapoet.CodeBlock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.xn__ho_hia.yosql.testutils.ValidationFile;
import de.xn__ho_hia.yosql.testutils.ValidationFileParameterResolver;

@SuppressWarnings({ "nls", "static-method" })
@ExtendWith(ValidationFileParameterResolver.class)
class TypicalCodeBlocksTest {

    @Test
    public void shouldAssignFieldToValueWithSameName(final ValidationFile validationFile)
            throws Exception {
        // given
        final String name = "test";

        // when
        final CodeBlock codeBlock = TypicalCodeBlocks.setFieldToSelf(name);

        // then
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

    @Test
    public void shouldGetMetadataFromResultSet(final ValidationFile validationFile)
            throws Exception {
        // given
        // when
        final CodeBlock codeBlock = TypicalCodeBlocks.getMetaData();

        // then
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

}
