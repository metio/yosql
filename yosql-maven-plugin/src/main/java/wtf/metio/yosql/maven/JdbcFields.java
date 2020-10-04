/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.JdbcFieldsConfiguration;

public class JdbcFields {

    private final String indexSuffix = "_INDEX";
    private final String rawSuffix = "_RAW";

    JdbcFieldsConfiguration asConfiguration() {
        return JdbcFieldsConfiguration.builder()
                .setIndexSuffix(indexSuffix)
                .setRawSuffix(rawSuffix)
                .build();
    }


}
