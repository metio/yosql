package de.xn__ho_hia.yosql.model;

/**
 * Signals that something went wrong during code generation.
 */
public class CodeGenerationException extends RuntimeException {

    private static final long serialVersionUID = 7360689175873441476L;

    /**
     * @param message
     *            The message to send.
     */
    public CodeGenerationException(final String message) {
        super(message);
    }

}
