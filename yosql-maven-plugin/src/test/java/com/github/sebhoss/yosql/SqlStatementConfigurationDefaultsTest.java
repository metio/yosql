package com.github.sebhoss.yosql;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test that verifies the default values for
 * {@link SqlStatementConfiguration}.
 */
public class SqlStatementConfigurationDefaultsTest {

    private SqlStatementConfiguration configuration;

    @Before
    public void setUp() {
        configuration = new SqlStatementConfiguration();
    }

    @Test
    public void shouldEnableBatchModeByDefault() {
        Assert.assertTrue(configuration.isBatch());
    }

    @Test
    public void shouldUseBatchAsDefaultBatchPrefix() {
        Assert.assertEquals("batch", configuration.getBatchPrefix());
    }

    @Test
    public void shouldEnableStreamModeByDefault() {
        Assert.assertTrue(configuration.isStreamEager());
        Assert.assertTrue(configuration.isStreamLazy());
    }

    @Test
    public void shouldUseStreamAsDefaultStreamPrefix() {
        Assert.assertEquals("stream", configuration.getStreamPrefix());
    }

    @Test
    public void shouldReturnNullForName() {
        Assert.assertNull(configuration.getName());
    }

    @Test
    public void shouldReturnNullForParameters() {
        Assert.assertNull(configuration.getParameters());
    }

    @Test
    public void shouldReturnNullForResultConverter() {
        Assert.assertNull(configuration.getResultConverter());
    }

    @Test
    public void shouldReturnNullForRepository() {
        Assert.assertNull(configuration.getRepository());
    }

}
