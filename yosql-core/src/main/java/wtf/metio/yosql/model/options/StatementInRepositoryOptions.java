package wtf.metio.yosql.model.options;

/**
 * Enumeration of all the possible ways a SQL statement can be written into a repository.
 */
public enum StatementInRepositoryOptions {

    /**
     * SQL statements are copied into repositories using string concatenation.
     */
    INLINE_CONCAT,

    /**
     * SQL statements are copied into repositories using text blocks.
     *
     * @since Java 15
     */
    INLINE_TEXT_BLOCK,

}
