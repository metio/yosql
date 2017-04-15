/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli;

/**
 * Command line interface for YoSQL.
 */
public class YoSqlCLI {

    /**
     * @param args
     *            The CLI arguments.
     * @throws Exception
     *             In case anything goes wrong
     */
    public static void main(final String... args) throws Exception {
        DaggerYoSqlCLIComponent.builder()
                .jOptConfigurationModule(new JOptConfigurationModule(args))
                .build()
                .yosql()
                .generateFiles();
    }

}
