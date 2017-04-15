package de.xn__ho_hia.yosql.benchmark.parse_file;

import de.xn__ho_hia.yosql.benchmark.DaggerYoSqlBenchmarkComponent;

/**
 * JMH based micro benchmark for the DefaultSqlFileParser running against all .sql files.
 */
public class AlternativeSqlFileParserParseAllFileBenchmark extends AbstractParseAllFileBenchmark {

    @Override
    public void setUpParser() throws Exception {
        // DaggerYoSqlWithAlternativeParserComponent
        parser = DaggerYoSqlBenchmarkComponent.builder().build().sqlFileParser();
    }

}
