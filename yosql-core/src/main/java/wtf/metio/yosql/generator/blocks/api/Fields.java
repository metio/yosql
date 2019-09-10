package wtf.metio.yosql.generator.blocks.api;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.Type;

public interface Fields {

    FieldSpec field(Type type, String name);
    FieldSpec field(TypeName type, String name);

    FieldSpec.Builder prepareConstant(Type type, String name);
    FieldSpec.Builder prepareConstant(TypeName type, String name);

    FieldSpec privateField(TypeName type, String name);

}
