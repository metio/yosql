package de.xn__ho_hia.yosql.generator.helpers;

import org.junit.jupiter.api.Test;

import de.xn__ho_hia.yosql.testutils.TestSqlConfigurations;
import de.xn__ho_hia.yosql.testutils.ValidationFile;
import de.xn__ho_hia.yosql.testutils.ValidationFileTest;

@SuppressWarnings({ "nls", "static-method" })
class TypicalCodeBlocksTest extends ValidationFileTest {

    @Test
    public void shouldAssignFieldToValueWithSameName(final ValidationFile validationFile) {
        validate(TypicalCodeBlocks.setFieldToSelf("test"), validationFile);
    }

    @Test
    public void shouldGetMetadataFromResultSet(final ValidationFile validationFile) {
        validate(TypicalCodeBlocks.getMetaData(), validationFile);
    }

    @Test
    public void shouldGetColumnCountFromMetadata(final ValidationFile validationFile) {
        validate(TypicalCodeBlocks.getColumnCount(), validationFile);
    }

    @Test
    public void shouldExecuteQuery(final ValidationFile validationFile) {
        validate(TypicalCodeBlocks.executeQuery(), validationFile);
    }

    @Test
    public void shouldExecuteUpdate(final ValidationFile validationFile) {
        validate(TypicalCodeBlocks.executeUpdate(), validationFile);
    }

    @Test
    public void shouldPrepareBatch(final ValidationFile validationFile) {
        validate(TypicalCodeBlocks.prepareBatch(TestSqlConfigurations.createConfiguration()), validationFile);
    }

    @Test
    public void shouldExecuteBatch(final ValidationFile validationFile) {
        validate(TypicalCodeBlocks.executeBatch(), validationFile);
    }

    @Test
    public void shouldAddBatch(final ValidationFile validationFile) {
        validate(TypicalCodeBlocks.addBatch(), validationFile);
    }

}
