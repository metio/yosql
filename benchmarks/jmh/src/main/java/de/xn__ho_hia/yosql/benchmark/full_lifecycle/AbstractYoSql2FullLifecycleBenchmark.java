package de.xn__ho_hia.yosql.benchmark.full_lifecycle;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.generator.raw_jdbc.RawJdbcRepositoryGenerator;
import de.xn__ho_hia.yosql.generator.utils.DefaultUtilitiesGenerator;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
abstract class AbstractYoSql2FullLifecycleBenchmark extends AbstractFullLifecycleBenchmark {

    @Override
    protected YoSql createYoSql(final ExecutionErrors errors, final SqlFileParser sqlFileParser,
            final SqlFileResolver fileResolver, final RawJdbcRepositoryGenerator repositoryGenerator,
            final DefaultUtilitiesGenerator utilsGenerator, final Timer timer) {
        return new YoSqlImplementation2(fileResolver, sqlFileParser, repositoryGenerator, utilsGenerator, errors, timer);
    }

}
