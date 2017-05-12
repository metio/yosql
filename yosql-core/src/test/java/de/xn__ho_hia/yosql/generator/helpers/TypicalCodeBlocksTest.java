package de.xn__ho_hia.yosql.generator.helpers;

import static de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks.*;

import org.junit.jupiter.api.Test;

import de.xn__ho_hia.yosql.testutils.TestSqlConfigurations;
import de.xn__ho_hia.yosql.testutils.ValidationFile;
import de.xn__ho_hia.yosql.testutils.ValidationFileTest;

@SuppressWarnings({ "nls", "static-method" })
class TypicalCodeBlocksTest extends ValidationFileTest {

    @Test
    public void shouldAssignFieldToValueWithSameName(final ValidationFile validationFile) {
        validate(setFieldToSelf("test"), validationFile);
    }

    @Test
    public void shouldGetMetadataFromResultSet(final ValidationFile validationFile) {
        validate(getMetaData(), validationFile);
    }

    @Test
    public void shouldGetColumnCountFromMetadata(final ValidationFile validationFile) {
        validate(getColumnCount(), validationFile);
    }

    @Test
    public void shouldExecuteQuery(final ValidationFile validationFile) {
        validate(executeQuery(), validationFile);
    }

    @Test
    public void shouldExecuteUpdate(final ValidationFile validationFile) {
        validate(executeUpdate(), validationFile);
    }

    @Test
    public void shouldPrepareBatch(final ValidationFile validationFile) {
        validate(prepareBatch(TestSqlConfigurations.createConfiguration()), validationFile);
    }

    @Test
    public void shouldExecuteBatch(final ValidationFile validationFile) {
        validate(executeBatch(), validationFile);
    }

    @Test
    public void shouldAddBatch(final ValidationFile validationFile) {
        validate(addBatch(), validationFile);
    }

}
