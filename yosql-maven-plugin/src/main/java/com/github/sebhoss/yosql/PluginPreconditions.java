package com.github.sebhoss.yosql;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Preconditions for plugin execution.
 */
@Named
@Singleton
public class PluginPreconditions {

    private final PluginErrors pluginErrors;

    @Inject
    public PluginPreconditions(final PluginErrors pluginErrors) {
        this.pluginErrors = pluginErrors;
    }

    /**
     * Asserts that a single directory is writable. In order to be writable, the
     * directory has to:
     * <ul>
     * <li>exist</li>
     * <li>be a directory (not a file)</li>
     * <li>be writable by the current process</li>
     * </ul>
     *
     * @param directory
     *            The directory to check
     * @throws MojoExecutionException
     *             In case the directory is somehow not writeable.
     */
    public void assertDirectoryIsWriteable(final File directory) throws MojoExecutionException {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                pluginErrors.buildError("Could not create [%s]. Check the permissions.", directory);
            }
        }
        if (!directory.isDirectory()) {
            pluginErrors.buildError("[%s] is not a directory.", directory);
        }
        if (!directory.canWrite()) {
            pluginErrors.buildError("Don't have permission to write to [%s].", directory);
        }
    }

}
