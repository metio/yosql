package wtf.metio.yosql.generator.blocks.api;

import com.squareup.javapoet.MethodSpec;

public interface Methods {

    MethodSpec.Builder publicMethod(String name);
    MethodSpec.Builder implementation(String name);
    MethodSpec.Builder constructor();

}
