package wtf.metio.yosql.generator.blocks.api;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.model.sql.SqlParameter;

import java.util.List;

public interface Parameters {

    ParameterSpec parameter(Class<?> type, String name);
    ParameterSpec parameter(TypeName type, String name);

    Iterable<ParameterSpec> asParameterSpecs(List<SqlParameter> parameters);
    Iterable<ParameterSpec> asBatchParameterSpecs(List<SqlParameter> parameters);

    Iterable<ParameterSpec> resultState(TypeName type);

}
