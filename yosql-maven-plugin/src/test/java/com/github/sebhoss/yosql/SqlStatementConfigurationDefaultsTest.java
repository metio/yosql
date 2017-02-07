package com.github.sebhoss.yosql;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.sebhoss.yosql.model.SqlStatementConfiguration;

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
        Assert.assertTrue(configuration.isMethodBatchApi());
    }

    @Test
    public void shouldUseBatchAsDefaultBatchPrefix() {
        Assert.assertEquals("batch", configuration.getMethodBatchPrefix());
    }

    @Test
    public void shouldEnableStreamModeByDefault() {
        Assert.assertTrue(configuration.isMethodStreamEagerApi());
        Assert.assertTrue(configuration.isMethodStreamLazyApi());
    }

    @Test
    public void shouldUseStreamAsDefaultStreamPrefix() {
        Assert.assertEquals("stream", configuration.getMethodStreamPrefix());
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
