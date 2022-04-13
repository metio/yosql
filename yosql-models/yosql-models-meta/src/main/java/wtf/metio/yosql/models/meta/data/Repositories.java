/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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
                .addAllSettings(methods())
                .build();
    }
    
    public static List<ConfigurationSetting> methods() {
        return List.of(
                generateStandardApi(),
                generateBatchApi(),
                generateStreamEagerApi(),
                generateStreamLazyApi(),
                generateRxJavaApi(),
                catchAndRethrow(),
                injectConverters(),
                batchPrefix(),
                batchSuffix(),
                streamPrefix(),
                streamSuffix(),
                rxjava2Prefix(),
                rxjava2Suffix(),
                lazyName(),
                eagerName());
    }

    private static ConfigurationSetting basePackageName() {
        return ConfigurationSetting.builder()
                .setName("basePackageName")
                .setType(TypicalTypes.STRING)
                .setValue("com.example.persistence")
                .setDescription("The base package name for all repositories")
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
                .setType(TypicalTypes.STRING)
                .setValue("Repository")
                .setDescription("The repository name suffix to use.")
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
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .setDescription("Generate interfaces for all repositories")
                .build();
    }

    private static ConfigurationSetting generateStandardApi() {
        return ConfigurationSetting.builder()
                .setName("generateStandardApi")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Generate standard methods")
                .build();
    }

    private static ConfigurationSetting generateBatchApi() {
        return ConfigurationSetting.builder()
                .setName("generateBatchApi")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Generate batch methods")
                .build();
    }

    private static ConfigurationSetting generateStreamEagerApi() {
        return ConfigurationSetting.builder()
                .setName("generateStreamEagerApi")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Generate batch methods")
                .build();
    }

    private static ConfigurationSetting generateStreamLazyApi() {
        return ConfigurationSetting.builder()
                .setName("generateStreamLazyApi")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Generate batch methods")
                .build();
    }

    private static ConfigurationSetting generateRxJavaApi() {
        return ConfigurationSetting.builder()
                .setName("generateRxJavaApi")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Generate batch methods")
                .build();
    }

    private static ConfigurationSetting catchAndRethrow() {
        return ConfigurationSetting.builder()
                .setName("catchAndRethrow")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Catch exceptions during SQL execution and re-throw them as RuntimeExceptions")
                .build();
    }

    private static ConfigurationSetting injectConverters() {
        return ConfigurationSetting.builder()
                .setName("injectConverters")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .setDescription("Toggles whether converters should be injected as constructor parameters.")
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

    private static ConfigurationSetting batchPrefix() {
        return ConfigurationSetting.builder()
                .setName("batchPrefix")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .setDescription("")
                .build();
    }

    private static ConfigurationSetting batchSuffix() {
        return ConfigurationSetting.builder()
                .setName("batchSuffix")
                .setType(TypicalTypes.STRING)
                .setValue("Batch")
                .setDescription("")
                .build();
    }

    private static ConfigurationSetting streamPrefix() {
        return ConfigurationSetting.builder()
                .setName("streamPrefix")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .setDescription("")
                .build();
    }

    private static ConfigurationSetting streamSuffix() {
        return ConfigurationSetting.builder()
                .setName("streamSuffix")
                .setType(TypicalTypes.STRING)
                .setValue("Stream")
                .setDescription("")
                .build();
    }

    private static ConfigurationSetting rxjava2Prefix() {
        return ConfigurationSetting.builder()
                .setName("rxjava2Prefix")
                .setType(TypicalTypes.STRING)
                .setValue("")
                .setDescription("")
                .build();
    }

    private static ConfigurationSetting rxjava2Suffix() {
        return ConfigurationSetting.builder()
                .setName("rxjava2Suffix")
                .setType(TypicalTypes.STRING)
                .setValue("Flow")
                .setDescription("")
                .build();
    }

    private static ConfigurationSetting lazyName() {
        return ConfigurationSetting.builder()
                .setName("lazyName")
                .setType(TypicalTypes.STRING)
                .setValue("Lazy")
                .setDescription("")
                .build();
    }

    private static ConfigurationSetting eagerName() {
        return ConfigurationSetting.builder()
                .setName("eagerName")
                .setType(TypicalTypes.STRING)
                .setValue("Eager")
                .setDescription("")
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
                .setType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setValue(CodeBlock.of("$T.of($L)", List.class, prefixesInCode.toString()))
                .setDescription("Configures which name prefixes are allowed for statements that are writing data to your database.")
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
                .setType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setValue(CodeBlock.of("$T.of($L)", List.class, prefixesInCode.toString()))
                .setDescription("Configures which name prefixes are allowed for statements that are reading data from your database.")
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
        final var defaultPrefixes = List.of("call, execute, evaluate, eval");
        final var prefixesInCode = new StringJoiner("\", \"", "\"", "\"");
        final var prefixesInDocs = new StringJoiner(", ");
        defaultPrefixes.forEach(prefixesInCode::add);
        defaultPrefixes.forEach(prefixesInDocs::add);
        return ConfigurationSetting.builder()
                .setName("allowedCallPrefixes")
                .setType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setValue(CodeBlock.of("$T.of($L)", List.class, prefixesInCode.toString()))
                .setDescription("Configures which name prefixes are allowed for statements that are calling stored procedures.")
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
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .setDescription("Validate user given names against list of allowed prefixes per type.")
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
