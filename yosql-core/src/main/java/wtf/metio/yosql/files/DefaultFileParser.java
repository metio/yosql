/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.files;

import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

import static java.util.stream.Collectors.toList;

final class DefaultFileParser implements FileParser {

    private final SqlFileResolver fileResolver;
    private final SqlFileParser fileParser;

    DefaultFileParser(final SqlFileResolver fileResolver, final SqlFileParser fileParser) {
        this.fileResolver = fileResolver;
        this.fileParser = fileParser;
    }

    @Override
    public List<SqlStatement> parseFiles() {
        return fileResolver.resolveFiles()
                .flatMap(fileParser::parse)
                .collect(toList());
    }

}
