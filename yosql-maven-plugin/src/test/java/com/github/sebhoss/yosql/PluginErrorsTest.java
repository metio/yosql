package com.github.sebhoss.yosql;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link PluginErrors}.
 */
public class PluginErrorsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private PluginErrors pluginErrors;

    @Before
    public void setUp() {
        pluginErrors = new PluginErrors();
    }

    /**
     * Ensures that a fresh instance has no errors attached yet.
     */
    @Test
    public void shouldNotHaveErrorsAfterCreation() {
        // given
        // nothing

        // when
        final boolean hasErrors = pluginErrors.hasErrors();

        // then
        Assert.assertFalse(hasErrors);
    }

    /**
     * Ensures that errors will be reported once they are added.
     */
    @Test
    public void shouldHaveErrorsAfterAddingOne() {
        // given
        final Throwable throwable = new Throwable();

        // when
        pluginErrors.add(throwable);

        // then
        Assert.assertTrue(pluginErrors.hasErrors());
    }

    /**
     * Ensures that no <code>NULL</code> {@link Throwable}s can be added. Those would otherwise cause another NullPointerException later down the line.
     */
    @Test
    public void shouldNotAcceptNullThrowables() {
        // given
        final Throwable throwable = null;

        // when
        thrown.expect(NullPointerException.class);

        // then
        pluginErrors.add(throwable);
    }

    /**
     * Ensures that an actual exception is thrown in case a build-error is encountered.
     */
    @Test
    public void shouldThrowMojoException() throws MojoExecutionException {
        // given
        final String message = "kaboom";

        // when
        thrown.expect(MojoExecutionException.class);
        thrown.expectMessage(message);

        // then
        pluginErrors.buildError(message);
    }

}
