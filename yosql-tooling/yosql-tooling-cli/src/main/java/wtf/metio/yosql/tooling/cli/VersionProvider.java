/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.cli;

import picocli.CommandLine;
import wtf.metio.yosql.cli.Version;

public class VersionProvider implements CommandLine.IVersionProvider {

    @Override
    public String[] getVersion() {
        return new String[]{
                "YoSQL: " + Version.VERSION,
                "JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
                "OS:  ${os.name} ${os.version} ${os.arch}"
        };
    }

}
