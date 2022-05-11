/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.constants.sql.SqlType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.testing.logging.LoggingObjectMother;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DefaultMethodNameConfigurer")
class DefaultMethodNameConfigurerTest {

    private DefaultMethodNameConfigurer configurer;
    private RepositoriesConfiguration repositories;

    @BeforeEach
    void setUp() {
        repositories = RepositoriesConfigurations.defaults();
        configurer = new DefaultMethodNameConfigurer(LoggingObjectMother.logger(), repositories);
    }

    @Test
    void batchNamePrefixKeep() {
        final var original = SqlConfiguration.usingDefaults().setBatchPrefix("prefix").build();
        final var adapted = configurer.batchNamePrefix(original);
        assertEquals(original.batchPrefix(), adapted.batchPrefix());
    }

    @Test
    void batchNamePrefixChange() {
        final var original = SqlConfiguration.usingDefaults().setBatchPrefix(" ").build();
        final var adapted = configurer.batchNamePrefix(original);
        assertEquals(repositories.batchPrefix(), adapted.batchPrefix());
    }

    @Test
    void batchNameSuffixKeep() {
        final var original = SqlConfiguration.usingDefaults().setBatchSuffix("suffix").build();
        final var adapted = configurer.batchNameSuffix(original);
        assertEquals(original.batchSuffix(), adapted.batchSuffix());
    }

    @Test
    void batchNameSuffixChange() {
        final var original = SqlConfiguration.usingDefaults().setBatchSuffix(" ").build();
        final var adapted = configurer.batchNameSuffix(original);
        assertEquals(repositories.batchSuffix(), adapted.batchSuffix());
    }

    @Test
    void blockingNamePrefixKeep() {
        final var original = SqlConfiguration.usingDefaults().setBlockingPrefix("prefix").build();
        final var adapted = configurer.blockingNamePrefix(original);
        assertEquals(original.blockingPrefix(), adapted.blockingPrefix());
    }

    @Test
    void blockingNamePrefixChange() {
        final var original = SqlConfiguration.usingDefaults().setBlockingPrefix(" ").build();
        final var adapted = configurer.blockingNamePrefix(original);
        assertEquals(repositories.blockingPrefix(), adapted.blockingPrefix());
    }

    @Test
    void blockingNameSuffixKeep() {
        final var original = SqlConfiguration.usingDefaults().setBlockingSuffix("suffix").build();
        final var adapted = configurer.blockingNameSuffix(original);
        assertEquals(original.blockingSuffix(), adapted.blockingSuffix());
    }

    @Test
    void blockingNameSuffixChange() {
        final var original = SqlConfiguration.usingDefaults().setBlockingSuffix(" ").build();
        final var adapted = configurer.blockingNameSuffix(original);
        assertEquals(repositories.blockingSuffix(), adapted.blockingSuffix());
    }

    @Test
    void mutinyNamePrefixKeep() {
        final var original = SqlConfiguration.usingDefaults().setMutinyPrefix("prefix").build();
        final var adapted = configurer.mutinyNamePrefix(original);
        assertEquals(original.mutinyPrefix(), adapted.mutinyPrefix());
    }

    @Test
    void mutinyNamePrefixChange() {
        final var original = SqlConfiguration.usingDefaults().setMutinyPrefix(" ").build();
        final var adapted = configurer.mutinyNamePrefix(original);
        assertEquals(repositories.mutinyPrefix(), adapted.mutinyPrefix());
    }

    @Test
    void mutinyNameSuffixKeep() {
        final var original = SqlConfiguration.usingDefaults().setMutinySuffix("suffix").build();
        final var adapted = configurer.mutinyNameSuffix(original);
        assertEquals(original.mutinySuffix(), adapted.mutinySuffix());
    }

    @Test
    void mutinyNameSuffixChange() {
        final var original = SqlConfiguration.usingDefaults().setMutinySuffix(" ").build();
        final var adapted = configurer.mutinyNameSuffix(original);
        assertEquals(repositories.mutinySuffix(), adapted.mutinySuffix());
    }

    @Test
    void reactorNamePrefixKeep() {
        final var original = SqlConfiguration.usingDefaults().setReactorPrefix("prefix").build();
        final var adapted = configurer.reactorNamePrefix(original);
        assertEquals(original.reactorPrefix(), adapted.reactorPrefix());
    }

    @Test
    void reactorNamePrefixChange() {
        final var original = SqlConfiguration.usingDefaults().setReactorPrefix(" ").build();
        final var adapted = configurer.reactorNamePrefix(original);
        assertEquals(repositories.reactorPrefix(), adapted.reactorPrefix());
    }

    @Test
    void reactorNameSuffixKeep() {
        final var original = SqlConfiguration.usingDefaults().setReactorSuffix("suffix").build();
        final var adapted = configurer.reactorNameSuffix(original);
        assertEquals(original.reactorSuffix(), adapted.reactorSuffix());
    }

    @Test
    void reactorNameSuffixChange() {
        final var original = SqlConfiguration.usingDefaults().setReactorSuffix(" ").build();
        final var adapted = configurer.reactorNameSuffix(original);
        assertEquals(repositories.reactorSuffix(), adapted.reactorSuffix());
    }

    @Test
    void rxJavaNamePrefixKeep() {
        final var original = SqlConfiguration.usingDefaults().setRxJavaPrefix("prefix").build();
        final var adapted = configurer.rxJavaNamePrefix(original);
        assertEquals(original.rxJavaPrefix(), adapted.rxJavaPrefix());
    }

    @Test
    void rxJavaNamePrefixChange() {
        final var original = SqlConfiguration.usingDefaults().setRxJavaPrefix(" ").build();
        final var adapted = configurer.rxJavaNamePrefix(original);
        assertEquals(repositories.rxJavaPrefix(), adapted.rxJavaPrefix());
    }

    @Test
    void rxJavaNameSuffixKeep() {
        final var original = SqlConfiguration.usingDefaults().setRxJavaSuffix("suffix").build();
        final var adapted = configurer.rxJavaNameSuffix(original);
        assertEquals(original.rxJavaSuffix(), adapted.rxJavaSuffix());
    }

    @Test
    void rxJavaNameSuffixChange() {
        final var original = SqlConfiguration.usingDefaults().setRxJavaSuffix(" ").build();
        final var adapted = configurer.rxJavaNameSuffix(original);
        assertEquals(repositories.rxJavaSuffix(), adapted.rxJavaSuffix());
    }

    @Test
    void streamEagerNamePrefixKeep() {
        final var original = SqlConfiguration.usingDefaults().setStreamPrefix("prefix").build();
        final var adapted = configurer.streamEagerNamePrefix(original);
        assertEquals(original.streamPrefix(), adapted.streamPrefix());
    }

    @Test
    void streamEagerNamePrefixChange() {
        final var original = SqlConfiguration.usingDefaults().setStreamPrefix(" ").build();
        final var adapted = configurer.streamEagerNamePrefix(original);
        assertEquals(repositories.streamPrefix(), adapted.streamPrefix());
    }

    @Test
    void streamEagerNameSuffixKeep() {
        final var original = SqlConfiguration.usingDefaults().setStreamSuffix("suffix").build();
        final var adapted = configurer.streamEagerNameSuffix(original);
        assertEquals(original.streamSuffix(), adapted.streamSuffix());
    }

    @Test
    void streamEagerNameSuffixChange() {
        final var original = SqlConfiguration.usingDefaults().setStreamSuffix(" ").build();
        final var adapted = configurer.streamEagerNameSuffix(original);
        assertEquals(repositories.streamSuffix(), adapted.streamSuffix());
    }

    @Test
    void affixesKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setBatchPrefix("prefix")
                .setBatchSuffix("suffix")
                .setBlockingPrefix("prefix")
                .setBlockingSuffix("suffix")
                .setMutinyPrefix("prefix")
                .setMutinySuffix("suffix")
                .setReactorPrefix("prefix")
                .setReactorSuffix("suffix")
                .setRxJavaPrefix("prefix")
                .setRxJavaSuffix("suffix")
                .setStreamPrefix("prefix")
                .setStreamSuffix("suffix")
                .build();
        final var adapted = configurer.affixes(original);
        assertAll(
                () -> assertEquals(original.batchPrefix(), adapted.batchPrefix()),
                () -> assertEquals(original.batchSuffix(), adapted.batchSuffix()),
                () -> assertEquals(original.blockingPrefix(), adapted.blockingPrefix()),
                () -> assertEquals(original.blockingSuffix(), adapted.blockingSuffix()),
                () -> assertEquals(original.mutinyPrefix(), adapted.mutinyPrefix()),
                () -> assertEquals(original.mutinySuffix(), adapted.mutinySuffix()),
                () -> assertEquals(original.reactorPrefix(), adapted.reactorPrefix()),
                () -> assertEquals(original.reactorSuffix(), adapted.reactorSuffix()),
                () -> assertEquals(original.rxJavaPrefix(), adapted.rxJavaPrefix()),
                () -> assertEquals(original.rxJavaSuffix(), adapted.rxJavaSuffix()),
                () -> assertEquals(original.streamPrefix(), adapted.streamPrefix()),
                () -> assertEquals(original.streamSuffix(), adapted.streamSuffix()));
    }

    @Test
    void affixesChange() {
        final var original = SqlConfiguration.usingDefaults()
                .setBatchPrefix(" ")
                .setBatchSuffix(" ")
                .setBlockingPrefix(" ")
                .setBlockingSuffix(" ")
                .setMutinyPrefix(" ")
                .setMutinySuffix(" ")
                .setReactorPrefix(" ")
                .setReactorSuffix(" ")
                .setRxJavaPrefix(" ")
                .setRxJavaSuffix(" ")
                .setStreamPrefix(" ")
                .setStreamSuffix(" ")
                .build();
        final var adapted = configurer.affixes(original);
        assertAll(
                () -> assertEquals(repositories.batchPrefix(), adapted.batchPrefix()),
                () -> assertEquals(repositories.batchSuffix(), adapted.batchSuffix()),
                () -> assertEquals(repositories.blockingPrefix(), adapted.blockingPrefix()),
                () -> assertEquals(repositories.blockingSuffix(), adapted.blockingSuffix()),
                () -> assertEquals(repositories.mutinyPrefix(), adapted.mutinyPrefix()),
                () -> assertEquals(repositories.mutinySuffix(), adapted.mutinySuffix()),
                () -> assertEquals(repositories.reactorPrefix(), adapted.reactorPrefix()),
                () -> assertEquals(repositories.reactorSuffix(), adapted.reactorSuffix()),
                () -> assertEquals(repositories.rxJavaPrefix(), adapted.rxJavaPrefix()),
                () -> assertEquals(repositories.rxJavaSuffix(), adapted.rxJavaSuffix()),
                () -> assertEquals(repositories.streamPrefix(), adapted.streamPrefix()),
                () -> assertEquals(repositories.streamSuffix(), adapted.streamSuffix()));
    }

    @Test
    void baseNameKeep() {
        final var original = SqlConfiguration.usingDefaults().setName("name").build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals(original.name(), adapted.name());
    }

    @Test
    void baseNameInvalidNameUnknownType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("statementNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 1);
        assertEquals("statementNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameReadType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("readNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameReadTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "some", 2);
        assertEquals("readNameWasChanged2", adapted.name());
    }

    @Test
    void baseNameInvalidNameWriteType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("updateNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameWriteTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "some", 3);
        assertEquals("updateNameWasChanged3", adapted.name());
    }

    @Test
    void baseNameInvalidNameCallType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("callNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameCallTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "some", 4);
        assertEquals("callNameWasChanged4", adapted.name());
    }

    @Test
    void baseNameBlankName() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("some", adapted.name());
    }

    @Test
    void baseNameBlankNameNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 1);
        assertEquals("some", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameUnknownType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertEquals("statementNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 1);
        assertEquals("statementNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameReadType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertEquals("readNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameReadTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 2);
        assertEquals("readNameWasChanged2", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameWriteType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertEquals("updateNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameWriteTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertEquals("updateNameWasChanged3", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameCallType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertEquals("callNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameCallTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertEquals("callNameWasChanged3", adapted.name());
    }

}