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
