/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.spring.jdbc;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public interface SpringJdbcBlocks {

    CodeBlock returnAsList(ParameterizedTypeName listOfResults, String converterAlias);

    CodeBlock returnAsFirst(TypeName resultType, String converterAlias);

    CodeBlock returnAsOne(TypeName resultType, String converterAlias);

}
