package de.xn__ho_hia.yosql.benchmark.parse_file;

import de.xn__ho_hia.yosql.benchmark.DaggerYoSqlBenchmarkComponent;

/**
 * JMH based micro benchmark for the DefaultSqlFileParser running against each .sql file individually.
 */
public class DefaultSqlFileParserParseEachFileBenchmark extends AbstractParseEachFileBenchmark {

    @Override
    public void setUpParser() throws Exception {
        parser = DaggerYoSqlBenchmarkComponent.builder().build().sqlFileParser();
    }

}
