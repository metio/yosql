package com.github.sebhoss.yosql.generator.utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.github.sebhoss.yosql.generator.UtilitiesGenerator;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlType;

public class DefaultUtilitiesGenerator implements UtilitiesGenerator {

    private final FlowStateGenerator            flowStateGenerator;
    private final ResultStateGenerator          resultStateGenerator;
    private final ToResultRowConverterGenerator toResultRowConverterGenerator;
    private final ResultRowGenerator            resultRowGenerator;

    @Inject
    public DefaultUtilitiesGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator) {
        this.flowStateGenerator = flowStateGenerator;
        this.resultStateGenerator = resultStateGenerator;
        this.toResultRowConverterGenerator = toResultRowConverterGenerator;
        this.resultRowGenerator = resultRowGenerator;
    }

    @Override
    public void generateUtilities(final List<SqlStatement> allStatements) {
        if (allStatements.stream()
                .anyMatch(SqlStatement::isReading)) {
            resultStateGenerator.generateResultStateClass();
        }
        if (allStatements.stream()
                .anyMatch(SqlStatement::shouldGenerateRxJavaAPI)) {
            flowStateGenerator.generateFlowStateClass();
        }
        if (resultConverters(allStatements)
                .anyMatch(converter -> converter.getConverterType().endsWith(
                        ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME))) {
            toResultRowConverterGenerator.generateToResultRowConverterClass();
            resultRowGenerator.generateResultRowClass();
        }
    }

    private Stream<ResultRowConverter> resultConverters(final List<SqlStatement> sqlStatements) {
        return sqlStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.getType())
                .map(SqlConfiguration::getResultConverter)
                .filter(Objects::nonNull)
                .distinct();
    }

}
