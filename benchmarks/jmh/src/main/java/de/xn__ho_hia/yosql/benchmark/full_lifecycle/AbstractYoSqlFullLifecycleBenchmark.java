package de.xn__ho_hia.yosql.benchmark.full_lifecycle;

import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.YoSqlImplementation;
import de.xn__ho_hia.yosql.generator.raw_jdbc.RawJdbcRepositoryGenerator;
import de.xn__ho_hia.yosql.generator.utils.DefaultUtilitiesGenerator;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

abstract class AbstractYoSqlFullLifecycleBenchmark extends AbstractFullLifecycleBenchmark {

    @Override
    protected YoSql createYoSql(final ExecutionErrors errors, final SqlFileParser sqlFileParser,
            final SqlFileResolver fileResolver, final RawJdbcRepositoryGenerator repositoryGenerator,
            final DefaultUtilitiesGenerator utilsGenerator, final Timer timer) {
        return new YoSqlImplementation(fileResolver, sqlFileParser, repositoryGenerator, utilsGenerator, errors, timer);
    }

}
