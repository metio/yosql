package com.github.sebhoss.yosql.generator;

import java.util.Optional;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

public interface LoggingGenerator {

    CodeBlock queryPicked(String fieldName);

    CodeBlock indexPicked(String fieldName);

    CodeBlock vendorQueryPicked(String fieldName);

    CodeBlock vendorIndexPicked(String fieldName);

    CodeBlock vendorDetected();

    CodeBlock executingQuery();

    CodeBlock shouldLog();

    boolean isEnabled();

    Optional<FieldSpec> logger(TypeName repoClass);

}
