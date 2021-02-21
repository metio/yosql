package wtf.metio.yosql.internals.model.generator.api;

import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;

import java.util.function.Function;

public interface Generator extends Function<ConfigurationGroup, TypeSpec> {

}
