package wtf.metio.yosql.orchestration;

import wtf.metio.yosql.model.sql.PackageTypeSpec;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Orchestrator {

    void execute(
            Supplier<List<SqlStatement>> parseFiles,
            Function<List<SqlStatement>, Stream<PackageTypeSpec>> generateCode);
}
