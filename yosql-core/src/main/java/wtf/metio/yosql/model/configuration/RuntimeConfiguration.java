/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

/**
 * Configuration that holds all possible runtime configurations.
 */
@Value.Immutable
public interface RuntimeConfiguration {

    static ImmutableRuntimeConfiguration.Builder builder() {
        return ImmutableRuntimeConfiguration.builder();
    }

    /**
     * @return A runtime configuration using default values.
     */
    static ImmutableRuntimeConfiguration usingDefaults() {
        return builder().build();
    }

    @Value.Default
    default FileConfiguration files() {
        return FileConfiguration.usingDefaults();
    }

    @Value.Default
    default JavaConfiguration java() {
        return JavaConfiguration.usingDefaults();
    }

    @Value.Default
    default LoggingConfiguration logging() {
        return LoggingConfiguration.usingDefaults();
    }

    @Value.Default
    default MethodConfiguration methods() {
        return MethodConfiguration.usingDefaults();
    }

    @Value.Default
    default PackagesConfiguration names() { // TODO: rename to packages
        return PackagesConfiguration.usingDefaults();
    }

    @Value.Default
    default ResourceConfiguration resources() {
        return ResourceConfiguration.usingDefaults();
    }

    @Value.Default
    default RepositoryConfiguration repositories() {
        return RepositoryConfiguration.usingDefaults();
    }

    @Value.Default
    default StatementConfiguration statements() {
        return StatementConfiguration.usingDefaults();
    }

    @Value.Default
    default VariableConfiguration variables() {
        return VariableConfiguration.usingDefaults();
    }

    @Value.Default
    default JdbcNamesConfiguration jdbcNames() {
        return JdbcNamesConfiguration.usingDefaults();
    }

    @Value.Default
    default JdbcFieldsConfiguration jdbcFields() {
        return JdbcFieldsConfiguration.usingDefaults();
    }

    @Value.Default
    default RxJavaConfiguration rxJava() {
        return RxJavaConfiguration.usingDefaults();
    }

    @Value.Default
    default ResultConfiguration result() {
        return ResultConfiguration.usingDefaults();
    }

    @Value.Default
    default AnnotationConfiguration annotations() {
        return AnnotationConfiguration.usingDefaults();
    }

}
