package de.xn__ho_hia.yosql.benchmark.parse_file;

import java.io.PrintStream;

import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.parser.SqlConfigurationFactory;

/**
 * JMH based micro benchmark for the {@link Java8SqlFileParser2} running against each .sql file individually.
 */
public class Java8SqlFileParser2ParseEachFileBenchmark extends AbstractParseEachFileBenchmark {

    @Override
    public void setUpParser() throws Exception {
        final ExecutionErrors errors = new ExecutionErrors();
        final ExecutionConfiguration configuration = YoSql.defaultConfiguration().build();
        final SqlConfigurationFactory configurationFactory = new SqlConfigurationFactory(errors, configuration);
        final PrintStream output = null;
        parser = new Java8SqlFileParser2(errors, configuration, configurationFactory, output);
    }

}
