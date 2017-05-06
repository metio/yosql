package de.xn__ho_hia.yosql.cli;

import joptsimple.OptionException;
import joptsimple.OptionParser;

class OptionParsingException extends RuntimeException {

    private static final long     serialVersionUID = 1L;

    private final OptionParser    parser;

    private final OptionException exception;

    public OptionParsingException(final OptionParser parser, final OptionException exception) {
        super(exception);
        this.parser = parser;
        this.exception = exception;
    }

    public OptionParsingException(final OptionParser parser) {
        this(parser, null);
    }

    public OptionParser getParser() {
        return parser;
    }

    public boolean couldNotParseOptions() {
        return exception != null;
    }

    @Override
    public synchronized OptionException getCause() {
        return exception;
    }

}
