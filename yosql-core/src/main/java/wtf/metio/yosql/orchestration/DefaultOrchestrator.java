package wtf.metio.yosql.orchestration;

import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.errors.ExecutionErrors;
import wtf.metio.yosql.model.sql.PackageTypeSpec;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static wtf.metio.yosql.model.internal.ApplicationEvents.*;

final class DefaultOrchestrator implements Orchestrator {

    private final Executor pool;
    private final Timer timer;
    private final Translator translator;
    private final TypeWriter typeWriter;
    private final ExecutionErrors errors;

    DefaultOrchestrator(
            final Executor pool,
            final Timer timer,
            final Translator translator,
            final TypeWriter typeWriter,
            final ExecutionErrors errors) {
        this.pool = pool;
        this.timer = timer;
        this.errors = errors;
        this.translator = translator;
        this.typeWriter = typeWriter;
    }

    @Override
    public void execute(
            final Supplier<List<SqlStatement>> parser,
            final Function<List<SqlStatement>, Stream<PackageTypeSpec>> generateCode) {
        supplyAsync(() -> parseFiles(parser), pool)
                .thenApplyAsync((statements -> timeCodeGeneration(generateCode, statements)), pool)
                .thenAcceptAsync(this::writeIntoFiles, pool)
                .thenRunAsync(timer::printTimings, pool)
                .exceptionally(this::handleExceptions)
                .join();
        if (errors.hasErrors()) {
            errors.codeGenerationException(translator.nonLocalized(CODE_GENERATION_FAILED));
        }
    }

    private List<SqlStatement> parseFiles(final Supplier<List<SqlStatement>> parser) {
        final var statements = timer.timed(translator.nonLocalized(PARSE_FILES), parser);
        if (errors.hasErrors()) {
            errors.sqlFileParsingException(translator.nonLocalized(PARSE_FILES_FAILED));
        }
        return statements;
    }

    private Stream<PackageTypeSpec> timeCodeGeneration(
            final Function<List<SqlStatement>, Stream<PackageTypeSpec>> generateCode,
            final List<SqlStatement> statements) {
        return timer.timed(translator.nonLocalized(GENERATE_REPOSITORIES), () -> generateCode.apply(statements));
    }

    private void writeIntoFiles(final Stream<PackageTypeSpec> typeSpecs) {
        timer.timed(translator.nonLocalized(WRITE_FILES), writeTypeSpecs(typeSpecs));
    }

    private Runnable writeTypeSpecs(final Stream<PackageTypeSpec> typeSpecs) {
        return () -> typeSpecs.parallel().forEach(typeWriter::writeType);
    }

    private Void handleExceptions(final Throwable throwable) {
        errors.add(throwable.getCause());
        return null;
    }

}