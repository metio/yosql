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
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import java.util.List;
import java.util.StringJoiner;

public final class Repositories {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Repositories.class.getSimpleName())
                .setDescription("Configures converters.")
                .addSettings(basePackageName())
                .addSettings(repositoryNameSuffix())
                .addSettings(generateInterface())
                .addAllSettings(methods())
                .addSettings(allowedWritePrefixes())
                .addSettings(allowedReadPrefixes())
                .addSettings(allowedCallPrefixes())
                .addSettings(validateMethodNamePrefixes())
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
                .setDescription("he base package name for all repositories")
                .build();
    }

    private static ConfigurationSetting repositoryNameSuffix() {
        return ConfigurationSetting.builder()
                .setName("repositoryNameSuffix")
                .setType(TypicalTypes.STRING)
                .setValue("Repository")
                .setDescription("The repository name suffix to use.")
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
                .setValue(false)
                .setDescription("Generate batch methods")
                .build();
    }

    private static ConfigurationSetting generateStreamEagerApi() {
        return ConfigurationSetting.builder()
                .setName("generateStreamEagerApi")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .setDescription("Generate batch methods")
                .build();
    }

    private static ConfigurationSetting generateStreamLazyApi() {
        return ConfigurationSetting.builder()
                .setName("generateStreamLazyApi")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .setDescription("Generate batch methods")
                .build();
    }

    private static ConfigurationSetting generateRxJavaApi() {
        return ConfigurationSetting.builder()
                .setName("generateRxJavaApi")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
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
        final var stringJoiner = new StringJoiner(", ", "\"", "\"");
        List.of("update", "insert", "delete", "create", "write", "add", "remove", "merge", "drop")
                .forEach(stringJoiner::add);
        return ConfigurationSetting.builder()
                .setName("allowedWritePrefixes")
                .setType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setValue(CodeBlock.of("$T.of($L)", List.class, stringJoiner.toString()))
                .setDescription("")
                .setCliValue("update, insert, delete, create, write, add, remove, merge, drop")
                .setMavenValue("update, insert, delete, create, write, add, remove, merge, drop")
                .build();
    }

    private static ConfigurationSetting allowedReadPrefixes() {
        final var stringJoiner = new StringJoiner(", ", "\"", "\"");
        List.of("").forEach(stringJoiner::add);
        return ConfigurationSetting.builder()
                .setName("allowedReadPrefixes")
                .setType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setValue(CodeBlock.of("$T.of($L)", List.class, stringJoiner.toString()))
                .setDescription("")
                .setCliValue("query, read, select, find")
                .setMavenValue("query, read, select, find")
                .build();
    }

    private static ConfigurationSetting allowedCallPrefixes() {
        final var stringJoiner = new StringJoiner(", ", "\"", "\"");
        List.of("")
                .forEach(stringJoiner::add);
        return ConfigurationSetting.builder()
                .setName("allowedCallPrefixes")
                .setType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setValue(CodeBlock.of("$T.of($L)", List.class, stringJoiner.toString()))
                .setDescription("")
                .setCliValue("call")
                .setMavenValue("call")
                .build();
    }

    private static ConfigurationSetting validateMethodNamePrefixes() {
        return ConfigurationSetting.builder()
                .setName("validateMethodNamePrefixes")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .setDescription("Validate user given names against list of allowed prefixes per type")
                .build();
    }

    private Repositories() {
        // data class
    }

}
