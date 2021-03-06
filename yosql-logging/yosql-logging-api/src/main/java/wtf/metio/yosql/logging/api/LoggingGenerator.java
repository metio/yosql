/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.logging.api;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.constants.api.LoggingApis;

import java.util.Optional;

public interface LoggingGenerator {

    boolean supports(LoggingApis loggingApi);

    CodeBlock queryPicked(String fieldName);

    CodeBlock indexPicked(String fieldName);

    CodeBlock vendorQueryPicked(String fieldName);

    CodeBlock vendorIndexPicked(String fieldName);

    CodeBlock vendorDetected();

    CodeBlock executingQuery();

    CodeBlock shouldLog();

    boolean isEnabled();

    Optional<FieldSpec> logger(TypeName repoClass);

    CodeBlock entering(String repository, String method);

}
