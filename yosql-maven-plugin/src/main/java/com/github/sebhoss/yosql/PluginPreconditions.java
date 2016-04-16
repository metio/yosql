/*
 * ysura GmbH ("COMPANY") CONFIDENTIAL
 * Unpublished Copyright (c) 2012-2015 ysura GmbH, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have executed
 * Confidentiality and Non-disclosure agreements explicitly covering such access.
 *
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes
 * information that is confidential and/or proprietary, and is a trade secret, of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
 * LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.github.sebhoss.yosql;

import org.apache.maven.plugin.MojoExecutionException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;

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
     * Asserts that a single directory is writeable. In order to be writeable, the directory has to:
     * <ul>
     *     <li>exist</li>
     *     <li>be a directory (not a file)</li>
     *     <li>be writeable by the current process</li>
     * </ul>
     *
     * @param directory The directory to check
     * @throws MojoExecutionException In case the directory is somehow not writeable.
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
