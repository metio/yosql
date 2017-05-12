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
    public void shouldAssignFieldToValueWithSameName(final ValidationFile validationFile) {
        validateWithValidationFile(TypicalCodeBlocks.setFieldToSelf("test"), validationFile);
    }

    @Test
    public void shouldGetMetadataFromResultSet(final ValidationFile validationFile) {
        validateWithValidationFile(TypicalCodeBlocks.getMetaData(), validationFile);
    }

    @Test
    public void shouldGetColumnCountFromMetadata(final ValidationFile validationFile) {
        validateWithValidationFile(TypicalCodeBlocks.getColumnCount(), validationFile);
    }

    @Test
    public void shouldExecuteQuery(final ValidationFile validationFile) {
        validateWithValidationFile(TypicalCodeBlocks.executeQuery(), validationFile);
    }

    @Test
    public void shouldExecuteUpdate(final ValidationFile validationFile) {
        validateWithValidationFile(TypicalCodeBlocks.executeUpdate(), validationFile);
    }

    @Test
    public void shouldPrepareBatch(final ValidationFile validationFile) {
        validateWithValidationFile(TypicalCodeBlocks.prepareBatch(createConfiguration()), validationFile);
    }

    @Test
    public void shouldExecuteBatch(final ValidationFile validationFile) {
        validateWithValidationFile(TypicalCodeBlocks.executeBatch(), validationFile);
    }

    @Test
    public void shouldAddBatch(final ValidationFile validationFile) {
        validateWithValidationFile(TypicalCodeBlocks.addBatch(), validationFile);
    }

    private static void validateWithValidationFile(final CodeBlock codeBlock, final ValidationFile validationFile) {
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());

    }

    private static SqlConfiguration createConfiguration() {
        final SqlConfiguration config = new SqlConfiguration();
        final SqlParameter parameter = new SqlParameter();
        parameter.setName("test");
        config.setParameters(Arrays.asList(parameter));
        return config;
    }

}
