package com.github.sebhoss.yosql.generator;

import com.squareup.javapoet.CodeBlock;

public interface LoggingGenerator {

    CodeBlock queryPicked(String fieldName);

    CodeBlock indexPicked(String fieldName);

    CodeBlock vendorQueryPicked(String fieldName);

    CodeBlock vendorIndexPicked(String fieldName);

    CodeBlock vendorDetected();

}
