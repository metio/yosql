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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.yaml.snakeyaml.Yaml;

@Named
@Singleton
public class SqlFileParser {

    private final Yaml         yamlParser = new Yaml();
    private final PluginErrors pluginErrors;

    @Inject
    public SqlFileParser(final PluginErrors pluginErrors) {
        this.pluginErrors = pluginErrors;
    }

    public SqlStatement parse(final Path path) {
        final StringBuilder yaml = new StringBuilder();
        final StringBuilder sql = new StringBuilder();

        splitUpYamlAndSql(path, yaml::append, sql::append);

        final SqlStatementConfiguration configuration = yamlParser.loadAs(yaml.toString().trim(),
                SqlStatementConfiguration.class);

        return new SqlStatement(configuration, sql.toString().trim());
    }

    private void splitUpYamlAndSql(final Path path, final Consumer<String> yaml, final Consumer<String> sql) {
        try (final BufferedReader reader = Files.newBufferedReader(path)) {
            for (String line; (line = reader.readLine()) != null;) {
                if (line.startsWith("--")) {
                    yaml.accept(line.substring(2));
                    yaml.accept("\n");
                } else {
                    sql.accept(line);
                    sql.accept("\n");
                }
            }
        } catch (final IOException exception) {
            pluginErrors.add(exception);
        }
    }

}
