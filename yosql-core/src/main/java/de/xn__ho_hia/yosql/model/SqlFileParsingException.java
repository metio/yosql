package de.xn__ho_hia.yosql.model;

/**
 * Signals that something went wrong during SQL file parsing.
 */
public class SqlFileParsingException extends RuntimeException {

    private static final long serialVersionUID = 4967281564674584404L;

    /**
     * @param message
     *            The message to send.
     */
    public SqlFileParsingException(final String message) {
        super(message);
    }

}
