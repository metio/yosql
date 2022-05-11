/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import java.util.List;
import java.util.StringJoiner;

/**
 * Configures how repositories are generated.
 */
public final class Repositories {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Repositories.class.getSimpleName())
                .setDescription("The `repositories` configuration can be used to control how `YoSQL` outputs repositories.")
                .addSettings(allowedCallPrefixes())
                .addSettings(allowedReadPrefixes())
                .addSettings(allowedWritePrefixes())
                .addSettings(basePackageName())
                .addSettings(generateInterface())
                .addSettings(repositoryNameSuffix())
                .addSettings(validateMethodNamePrefixes())
                .addAllSettings(stringMethods())
                .addAllSettings(booleanMethods())
                .build();
    }

    /**
     * @return String configuration settings that can be set on repository or method level.
     */
    public static List<ConfigurationSetting> stringMethods() {
        return List.of(
                blockingPrefix(),
                blockingSuffix(),
                batchPrefix(),
                batchSuffix(),
                mutinyPrefix(),
                mutinySuffix(),
                reactorPrefix(),
                reactorSuffix(),
                rxJavaPrefix(),
                rxJavaSuffix(),
                streamPrefix(),
                streamSuffix());
    }

    /**
     * @return Boolean configuration settings that can be set on repository or method level.
     */
    public static List<ConfigurationSetting> booleanMethods() {
        return List.of(
                generateBlockingApi(),
                generateBatchApi(),
                generateMutinyApi(),
                generateReactorApi(),
                generateRxJavaApi(),
                generateStreamApi(),
                catchAndRethrow(),
                injectConverters());
    }

    private static ConfigurationSetting basePackageName() {
        return ConfigurationSetting.builder()
                .setName("basePackageName")
                .setDescription("The base package name for all repositories")
                .setType(TypicalTypes.STRING)
                .setValue("com.example.persistence")
                .addExamples(ConfigurationExample.builder()
                        .setValue("com.example.persistence")
                        .setDescription("The default value of the `basePackageName` configuration option is `com.example.persistence`. Setting the option to `com.example.persistence` therefore produces the same code generated as the default configuration without any configuration option set. It produces code similar to this:")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    // ... rest of generated code

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("your.own.domain")
                        .setDescription("Changing the `basePackageName` configuration option to `your.own.domain` generates the following code instead:")
                        .setResult("""
                                package your.own.domain;

                                public class SomeRepository {

                                    // ... rest of generated code (same as above)

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting repositoryNameSuffix() {
        return ConfigurationSetting.builder()
                .setName("repositoryNameSuffix")
                .setDescription("The repository name suffix to use.")
                .setType(TypicalTypes.STRING)
                .setValue("Repository")
                .setExplanation("In case the repository name already contains the configured suffix, it will not be added twice.")
                .addExamples(ConfigurationExample.builder()
                        .setValue("Repository")
                        .setDescription("The default value of the `repositoryNameSuffix` configuration option is `Repository`. Setting the option to `Repository` therefore produces the same code generated as the default configuration without any configuration option set. It produces code similar to this:")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    // ... rest of generated code

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("Repo")
                        .setDescription("Changing the `repositoryNameSuffix` configuration option to `Repo` generates the following code instead:")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepo {

                                    // ... rest of generated code (same as above)

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting generateInterface() {
        return ConfigurationSetting.builder()
                .setName("generateInterfaces")
                .setDescription("Generate interfaces for all repositories")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .build();
    }

    private static ConfigurationSetting generateBlockingApi() {
        return ConfigurationSetting.builder()
                .setName("generateBlockingApi")
                .setDescription("Generate blocking methods")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .build();
    }

    private static ConfigurationSetting generateBatchApi() {
        return ConfigurationSetting.builder()
                .setName("generateBatchApi")
                .setDescription("Generate batch methods")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .build();
    }

    private static ConfigurationSetting generateStreamApi() {
        return ConfigurationSetting.builder()
                .setName("generateStreamApi")
                .setDescription("Generate streaming methods")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .build();
    }

    private static ConfigurationSetting generateRxJavaApi() {
        return ConfigurationSetting.builder()
                .setName("generateRxJavaApi")
                .setDescription("Generate RxJava based methods")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .build();
    }

    private static ConfigurationSetting generateReactorApi() {
        return ConfigurationSetting.builder()
                .setName("generateReactorApi")
                .setDescription("Generate Reactor based methods")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .build();
    }

    private static ConfigurationSetting generateMutinyApi() {
        return ConfigurationSetting.builder()
                .setName("generateMutinyApi")
                .setDescription("Generate Mutiny based methods")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .build();
    }

    private static ConfigurationSetting catchAndRethrow() {
        return ConfigurationSetting.builder()
                .setName("catchAndRethrow")
                .setDescription("Catch exceptions during SQL execution and re-throw them as RuntimeExceptions")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .build();
    }

    private static ConfigurationSetting injectConverters() {
        return ConfigurationSetting.builder()
                .setName("injectConverters")
                .setDescription("Toggles whether converters should be injected as constructor parameters.")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("The default value of the `injectConverters` configuration option is `false`. It produces code similar to this:")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    private final DataSource dataSource;

                                    private final ToResultRowConverter resultRow;

                                    public SomeRepository(final DataSource dataSource) {
                                        this.dataSource = dataSource;
                                        this.resultRow = new ToResultRowConverter();
                                    }

                                    // ... rest of generated code

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("Changing the `injectConverters` configuration option to `true` generates the following code instead:")
                        .setResult("""
                                package your.own.domain;

                                public class SomeRepository {

                                    private final DataSource dataSource;

                                    private final ToResultRowConverter resultRow;

                                    public SomeRepository(final DataSource dataSource, final ToResultRowConverter resultRow) {
                                        this.dataSource = dataSource;
                                        this.resultRow = resultRow;
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting blockingPrefix() {
        return ConfigurationSetting.builder()
                .setName("blockingPrefix")
                .setDescription("The method prefix to use for generated blocking methods.")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .build();
    }

    private static ConfigurationSetting blockingSuffix() {
        return ConfigurationSetting.builder()
                .setName("blockingSuffix")
                .setDescription("The method suffix to use for generated blocking methods.")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .build();
    }

    private static ConfigurationSetting batchPrefix() {
        return ConfigurationSetting.builder()
                .setName("batchPrefix")
                .setDescription("The method prefix to use for generated batch methods.")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .build();
    }

    private static ConfigurationSetting batchSuffix() {
        return ConfigurationSetting.builder()
                .setName("batchSuffix")
                .setDescription("The method suffix to use for generated batch methods.")
                .setType(TypicalTypes.STRING)
                .setValue("Batch")
                .build();
    }

    private static ConfigurationSetting streamPrefix() {
        return ConfigurationSetting.builder()
                .setName("streamPrefix")
                .setDescription("The method prefix to use for generated stream methods.")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .build();
    }

    private static ConfigurationSetting streamSuffix() {
        return ConfigurationSetting.builder()
                .setName("streamSuffix")
                .setDescription("The method suffix to use for generated stream methods.")
                .setType(TypicalTypes.STRING)
                .setValue("Stream")
                .build();
    }

    private static ConfigurationSetting mutinyPrefix() {
        return ConfigurationSetting.builder()
                .setName("mutinyPrefix")
                .setDescription("The method prefix to use for generated methods that use the Mutiny API.")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .build();
    }

    private static ConfigurationSetting mutinySuffix() {
        return ConfigurationSetting.builder()
                .setName("mutinySuffix")
                .setDescription("The method suffix to use for generated methods that use the Mutiny API.")
                .setType(TypicalTypes.STRING)
                .setValue("Multi")
                .build();
    }

    private static ConfigurationSetting reactorPrefix() {
        return ConfigurationSetting.builder()
                .setName("reactorPrefix")
                .setDescription("The method prefix to use for generated methods that use the Reactor API.")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .build();
    }

    private static ConfigurationSetting reactorSuffix() {
        return ConfigurationSetting.builder()
                .setName("reactorSuffix")
                .setDescription("The method suffix to use for generated methods that use the Reactor API.")
                .setType(TypicalTypes.STRING)
                .setValue("Flux")
                .build();
    }

    private static ConfigurationSetting rxJavaPrefix() {
        return ConfigurationSetting.builder()
                .setName("rxJavaPrefix")
                .setDescription("The method prefix to use for generated methods that use the RxJava API.")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .build();
    }

    private static ConfigurationSetting rxJavaSuffix() {
        return ConfigurationSetting.builder()
                .setName("rxJavaSuffix")
                .setDescription("The method suffix to use for generated methods that use the RxJava API.")
                .setType(TypicalTypes.STRING)
                .setValue("Flow")
                .build();
    }

    private static ConfigurationSetting allowedWritePrefixes() {
        final var defaultPrefixes = List.of("update", "insert", "delete", "create", "write", "add", "remove", "merge", "drop");
        final var prefixesInCode = new StringJoiner("\", \"", "\"", "\"");
        final var prefixesInDocs = new StringJoiner(", ");
        defaultPrefixes.forEach(prefixesInCode::add);
        defaultPrefixes.forEach(prefixesInDocs::add);
        return ConfigurationSetting.builder()
                .setName("allowedWritePrefixes")
                .setDescription("Configures which name prefixes are allowed for statements that are writing data to your database.")
                .setType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setValue(CodeBlock.of("$T.of($L)", List.class, prefixesInCode.toString()))
                .setCliValue(prefixesInDocs.toString())
                .setMavenValue(prefixesInDocs.toString())
                .addExamples(ConfigurationExample.builder()
                        .setValue(prefixesInDocs.toString())
                        .setDescription(String.format("The default value of the `allowedWritePrefixes` configuration option is `%s` to allow several commonly used names for writing statements.", prefixesInDocs))
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("scribo")
                        .setDescription("Changing the `allowedWritePrefixes` configuration option to `scribo` only allows names with the prefix `scribo` to write data.")
                        .build())
                .build();
    }

    private static ConfigurationSetting allowedReadPrefixes() {
        final var defaultPrefixes = List.of("read", "select", "find", "query", "lookup", "get");
        final var prefixesInCode = new StringJoiner("\", \"", "\"", "\"");
        final var prefixesInDocs = new StringJoiner(", ");
        defaultPrefixes.forEach(prefixesInCode::add);
        defaultPrefixes.forEach(prefixesInDocs::add);
        return ConfigurationSetting.builder()
                .setName("allowedReadPrefixes")
                .setDescription("Configures which name prefixes are allowed for statements that are reading data from your database.")
                .setType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setValue(CodeBlock.of("$T.of($L)", List.class, prefixesInCode.toString()))
                .setCliValue(prefixesInDocs.toString())
                .setMavenValue(prefixesInDocs.toString())
                .addExamples(ConfigurationExample.builder()
                        .setValue(prefixesInDocs.toString())
                        .setDescription(String.format("The default value of the `allowedReadPrefixes` configuration option is `%s` to allow several commonly used names for reading statements.", prefixesInDocs))
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("lego")
                        .setDescription("Changing the `allowedReadPrefixes` configuration option to `lego` only allows names with the prefix `lego` to read data.")
                        .build())
                .build();
    }

    private static ConfigurationSetting allowedCallPrefixes() {
        final var defaultPrefixes = List.of("call", "execute", "evaluate", "eval");
        final var prefixesInCode = new StringJoiner("\", \"", "\"", "\"");
        final var prefixesInDocs = new StringJoiner(", ");
        defaultPrefixes.forEach(prefixesInCode::add);
        defaultPrefixes.forEach(prefixesInDocs::add);
        return ConfigurationSetting.builder()
                .setName("allowedCallPrefixes")
                .setDescription("Configures which name prefixes are allowed for statements that are calling stored procedures.")
                .setType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setValue(CodeBlock.of("$T.of($L)", List.class, prefixesInCode.toString()))
                .setCliValue(prefixesInDocs.toString())
                .setMavenValue(prefixesInDocs.toString())
                .addExamples(ConfigurationExample.builder()
                        .setValue(prefixesInDocs.toString())
                        .setDescription(String.format("The default value of the `allowedCallPrefixes` configuration option is `%s` to allow several commonly used names for calling procedures.", prefixesInDocs))
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("do")
                        .setDescription("Changing the `allowedCallPrefixes` configuration option to `do` only allows names with the prefix `do` to call stored procedures.")
                        .build())
                .build();
    }

    private static ConfigurationSetting validateMethodNamePrefixes() {
        return ConfigurationSetting.builder()
                .setName("validateMethodNamePrefixes")
                .setDescription("Validate user given names against list of allowed prefixes per type.")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("The default value of the `validateMethodNamePrefixes` configuration option is `false` which disables the validation of names according to your configured prefixes.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("Changing the `validateMethodNamePrefixes` configuration option to `true` enables the validation of method names.")
                        .build())
                .build();
    }

    private Repositories() {
        // data class
    }

}
