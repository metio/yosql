/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.orchestration;

/**
 * High-level interface of YoSQL. All configuration options must be passed into the actual implementation
 * or otherwise obtained before generating code.
 */
public interface YoSQL {

    /**
     * Generates .java files based on the configured .sql files and generator options.
     */
    void generateCode();

}
