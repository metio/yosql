package wtf.metio.yosql.generator.blocks.api;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

public interface Classes {

    TypeSpec.Builder publicClass(ClassName name);
    TypeSpec.Builder openClass(ClassName name);

}
