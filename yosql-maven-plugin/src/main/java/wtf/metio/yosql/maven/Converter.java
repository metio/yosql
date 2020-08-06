package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.generator.utilities.ToResultRowConverterGenerator;
import wtf.metio.yosql.model.sql.ParameterConverter;
import wtf.metio.yosql.model.sql.ResultRowConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Configures converters.
 */
public class Converter {

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter
    private final List<ParameterConverter> parameterConverters = new ArrayList<>();

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter
    private final List<ResultRowConverter> resultRowConverters = new ArrayList<>();

    /**
     * The default row converter which is being used if no custom converter is specified for a statement. Can be either
     * the alias or fully-qualified name of a converter. Default 'resultRow'.
     */
    @Parameter(required = true, defaultValue = ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME)
    private String defaultRowConverter;

    //        final RuntimeConfiguration.Builder builder = RuntimeConfiguration.builder()
//                .setDefaultRowConverter(defaultRowConverter);
//
//        final ResultRowConverter toResultRow = new ResultRowConverter();
//        toResultRow.setAlias("resultRow");
//        toResultRow.setResultType(utilPackage + "resultRow");
//        toResultRow.setConverterType(
//                basePackageName
//                        + ""
//                        + converterPackageName
//                        + "ToResultRowConverter");
//        resultRowConverters.add(toResultRow);
//        builder.setResultRowConverters(resultRowConverters);

}
