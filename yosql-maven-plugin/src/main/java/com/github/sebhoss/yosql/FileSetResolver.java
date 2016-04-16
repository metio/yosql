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

import org.apache.maven.model.FileSet;
import org.codehaus.plexus.util.FileUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@Named
@Singleton
public class FileSetResolver {

    private final PluginErrors pluginErrors;

    @Inject
    public FileSetResolver(final PluginErrors pluginErrors) {
        this.pluginErrors = pluginErrors;
    }

    public Stream<Path> resolveFiles(final FileSet fileSet) {
        final File directory = new File(fileSet.getDirectory());
        final String includes = commaSeparated(fileSet.getIncludes());
        final String excludes = commaSeparated(fileSet.getExcludes());
        try {
            return FileUtils.getFiles(directory, includes, excludes)
                    .stream()
                    .map(File::toPath);
        } catch (final IOException exception) {
            pluginErrors.add(exception);
            return Stream.of();
        }
    }

    private static String commaSeparated(final List<String> patterns) {
        return patterns.stream().collect(joining(", "));
    }

}
