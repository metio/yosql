package com.github.sebhoss.yosql.generator.logging;

import com.github.sebhoss.yosql.generator.LoggingGenerator;
import com.squareup.javapoet.CodeBlock;

public class Slf4jLoggingGenerator implements LoggingGenerator {

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CodeBlock vendorDetected() {
        // TODO Auto-generated method stub
        return null;
    }

}
