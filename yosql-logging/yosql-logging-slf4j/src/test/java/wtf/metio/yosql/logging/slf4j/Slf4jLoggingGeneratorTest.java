/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.logging.slf4j;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testing.codegen.Blocks;

@DisplayName("Slf4jLoggingGenerator")
final class Slf4jLoggingGeneratorTest {

    private Slf4jLoggingGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new Slf4jLoggingGenerator(Blocks.names(), Blocks.fields());
    }

    @Test
    void logger() {
        final var logger = generator.logger(TypeName.BOOLEAN);
        Assertions.assertTrue(logger.isPresent());
        Assertions.assertEquals("""
                private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(boolean.class);
                """, logger.get().toString());
    }

    @Test
    void queryPicked() {
        Assertions.assertEquals("""
                LOG.debug(String.format("Picked query [%s]", "test"));
                """, generator.queryPicked("test").toString());
    }

    @Test
    void indexPicked() {
        Assertions.assertEquals("""
                LOG.debug(String.format("Picked index [%s]", "test"));
                """, generator.indexPicked("test").toString());
    }

    @Test
    void vendorQueryPicked() {
        Assertions.assertEquals("""
                LOG.debug(String.format("Picked query [%s]", "test"));
                """, generator.vendorQueryPicked("test").toString());
    }

    @Test
    void vendorIndexPicked() {
        Assertions.assertEquals("""
                LOG.debug(String.format("Picked index [%s]", "test"));
                """, generator.vendorIndexPicked("test").toString());
    }

    @Test
    void vendorDetected() {
        Assertions.assertEquals("""
                LOG.info(java.lang.String.format("Detected database vendor [%s]", databaseProductName));
                """, generator.vendorDetected().toString());
    }

    @Test
    void executingQuery() {
        Assertions.assertEquals("""
                LOG.info(java.lang.String.format("Executing query [%s]", executedQuery));
                """, generator.executingQuery().toString());
    }

    @Test
    void shouldLog() {
        Assertions.assertEquals("""
                LOG.isInfoEnabled()""", generator.shouldLog().toString());
    }

    @Test
    void isEnabled() {
        Assertions.assertTrue(generator.isEnabled());
    }

    @Test
    void entering() {
        Assertions.assertEquals("""
                LOG.debug(java.lang.String.format("Entering [%s#%s]", "repo", "method"));
                """, generator.entering("repo", "method").toString());
    }

}