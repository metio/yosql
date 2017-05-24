package de.xn__ho_hia.yosql.generator.logging.shared;

import com.squareup.javapoet.CodeBlock;

import de.xn__ho_hia.yosql.generator.api.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;

/**
 * Abstract implementation for {@link LoggingGenerator}s.
 */
@SuppressWarnings("nls")
public abstract class AbstractLoggingGenerator implements LoggingGenerator {

    @Override
    public final CodeBlock queryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.$L(() -> String.format($S, $S))", TypicalNames.LOGGER, debug(), "Picked query [%s]",
                        fieldName)
                .build();
    }

    @Override
    public final CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.$L(() -> String.format($S, $S))", TypicalNames.LOGGER, debug(),
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public final CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.$L(() -> String.format($S, $S))", TypicalNames.LOGGER, debug(),
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public final CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.$L(() -> String.format($S, $S))", TypicalNames.LOGGER, debug(),
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public final CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$N.$L(() -> $T.format($S, $N))", TypicalNames.LOGGER, info(), String.class,
                        "Detected database vendor [%s]", TypicalNames.DATABASE_PRODUCT_NAME)
                .build();
    }

    @Override
    public final CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$N.$L(() -> $T.format($S, $N))", TypicalNames.LOGGER, info(), String.class,
                        "Executing query [%s]", TypicalNames.EXECUTED_QUERY)
                .build();
    }

    @Override
    public final boolean isEnabled() {
        return true;
    }

    @SuppressWarnings("static-method")
    protected String info() {
        return "info";
    }

    @SuppressWarnings("static-method")
    protected String debug() {
        return "debug";
    }

    @SuppressWarnings("static-method")
    protected String canLog() {
        return "isEnabled";
    }

}
