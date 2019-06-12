/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.helpers;

import static wtf.metio.yosql.generator.helpers.TypicalCodeBlocks.*;

import org.junit.jupiter.api.Test;

import wtf.metio.yosql.model.ResultRowConverter;
import wtf.metio.yosql.testutils.TestSqlConfigurations;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

class TypicalCodeBlocksTest extends ValidationFileTest {

    @Test
    public void shouldAssignFieldToValueWithSameName(final ValidationFile validationFile) {
        validate(setFieldToSelf("test"), validationFile);
    }

    @Test
    public void shouldInitializeConverter(final ValidationFile validationFile) {
        final ResultRowConverter converter = new ResultRowConverter();
        converter.setAlias("converter");
        converter.setConverterType("com.example.MyTestConverter");
        validate(initializeConverter(converter), validationFile);
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
