package de.xn__ho_hia.yosql.generator.helpers;

import java.util.Arrays;

import com.squareup.javapoet.CodeBlock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlParameter;
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

    @Test
    public void shouldGetColumnCountFromMetadata(final ValidationFile validationFile)
            throws Exception {
        // given
        // when
        final CodeBlock codeBlock = TypicalCodeBlocks.getColumnCount();

        // then
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

    @Test
    public void shouldExecuteQuery(final ValidationFile validationFile)
            throws Exception {
        // given
        // when
        final CodeBlock codeBlock = TypicalCodeBlocks.executeQuery();

        // then
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

    @Test
    public void shouldExecuteUpdate(final ValidationFile validationFile)
            throws Exception {
        // given
        // when
        final CodeBlock codeBlock = TypicalCodeBlocks.executeUpdate();

        // then
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

    @Test
    public void shouldPrepareBatch(final ValidationFile validationFile)
            throws Exception {
        // given
        final SqlConfiguration config = new SqlConfiguration();
        final SqlParameter parameter = new SqlParameter();
        parameter.setName("test");
        config.setParameters(Arrays.asList(parameter));

        // when
        final CodeBlock codeBlock = TypicalCodeBlocks.prepareBatch(config);

        // then
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

    @Test
    public void shouldExecuteBatch(final ValidationFile validationFile)
            throws Exception {
        // given
        // when
        final CodeBlock codeBlock = TypicalCodeBlocks.executeBatch();

        // then
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

    @Test
    public void shouldAddBatch(final ValidationFile validationFile)
            throws Exception {
        // given
        // when
        final CodeBlock codeBlock = TypicalCodeBlocks.addBatch();

        // then
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

}
