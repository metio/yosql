package wtf.metio.yosql.model.options;

/**
 * Options that control how the type of a variable should be declared.
 */
public enum VariableTypeOptions {

    /**
     * Generate variable declarations using the type of the variable (<code>int x = 123</code>).
     */
    TYPE,

    /**
     * Generate variable declarations using the 'var' keyword introduced in Java 11 (<code>var x = 123</code>).
     */
    VAR

}
