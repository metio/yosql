package wtf.metio.yosql.generator.api;

import wtf.metio.yosql.model.sql.PackageTypeSpec;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

public interface CodeGenerator {

    Stream<PackageTypeSpec> generateCode(List<SqlStatement> statements);

}
