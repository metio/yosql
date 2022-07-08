/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.configuration.Constants;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.meta.ImmutableConfigurationSetting;

import java.util.List;
import java.util.StringJoiner;

import static wtf.metio.yosql.internals.javapoet.TypicalTypes.gradleListPropertyOf;
import static wtf.metio.yosql.internals.jdk.Strings.upperCase;

/**
 * Configures how repositories are generated.
 */
public final class Repositories extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Repositories.class.getSimpleName();
    static final String INJECT_CONVERTERS = "injectConverters";

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("The `repositories` configuration can be used to control how `YoSQL` outputs repositories.")
                .addSettings(allowedCallPrefixes())
                .addSettings(allowedReadPrefixes())
                .addSettings(allowedWritePrefixes())
                .addSettings(basePackageName())
                .addSettings(generateInterfaces())
                .addSettings(repositoryInterfacePrefix())
                .addSettings(repositoryInterfaceSuffix())
                .addSettings(repositoryNamePrefix())
                .addSettings(repositoryNameSuffix())
                .addSettings(validateMethodNamePrefixes())
                .addSettings(injectConverters())
                .addAllSettings(stringMethods())
                .addAllSettings(booleanMethods())
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .build();
    }

    /**
     * @return String configuration settings that can be set on repository or method level.
     */
    public static List<ConfigurationSetting> stringMethods() {
        return List.of(
                standardPrefix(),
                standardSuffix(),
                batchPrefix(),
                batchSuffix());
    }

    /**
     * @return Boolean configuration settings that can be set on repository or method level.
     */
    public static List<ConfigurationSetting> booleanMethods() {
        return List.of(
                executeOnce(),
                executeBatch(),
                executeMany(),
                usePreparedStatement(),
                catchAndRethrow(),
                writesReturnUpdateCount(),
                throwOnMultipleResultsForSingle());
    }

    private static ConfigurationSetting basePackageName() {
        final var name = "basePackageName";
        final var description = "The base package name for all repositories";
        final var value = "com.example.persistence";
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
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

    private static ConfigurationSetting repositoryInterfacePrefix() {
        final var name = "repositoryInterfacePrefix";
        final var description = "The repository interface name prefix to use.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .build();
    }

    private static ConfigurationSetting repositoryInterfaceSuffix() {
        final var name = "repositoryInterfaceSuffix";
        final var description = "The repository interface name suffix to use.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .build();
    }

    private static ConfigurationSetting repositoryNamePrefix() {
        final var name = "repositoryNamePrefix";
        final var description = "The repository name prefix to use.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("In case the repository name already contains the configured prefix, it will not be added twice.")
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value of the `repositoryNamePrefix` configuration option is the empty string. Setting the option to `` therefore produces the same code generated as the default configuration without any configuration option set. It produces code similar to this:")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    // ... rest of generated code

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("Database")
                        .setDescription("Changing the `repositoryNamePrefix` configuration option to `Database` generates the following code instead:")
                        .setResult("""
                                package com.example.persistence;

                                public class DatabaseSomeRepo {

                                    // ... rest of generated code (same as above)

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting repositoryNameSuffix() {
        final var name = "repositoryNameSuffix";
        final var description = "The repository name suffix to use.";
        final var value = "Repository";
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("In case the repository name already contains the configured suffix, it will not be added twice.")
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
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

    private static ConfigurationSetting generateInterfaces() {
        final var name = "generateInterfaces";
        final var description = "Generate interfaces for all repositories";
        final var value = false;
        return setting(GROUP_NAME, name, description, value)
                .build();
    }

    private static ConfigurationSetting executeOnce() {
        final var name = Constants.EXECUTE_ONCE;
        final var description = "Generate methods that are executed once with the given parameters";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting executeBatch() {
        final var name = Constants.EXECUTE_BATCH;
        final var description = "Generate methods that are executed as batch";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting executeMany() {
        final var name = Constants.EXECUTE_MANY;
        final var description = "Generate methods that can be combined and executed with many other methods";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting usePreparedStatement() {
        final var name = "usePreparedStatement";
        final var description = "Should `PreparedStatement`s be used instead of `Statement`s";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting catchAndRethrow() {
        final var name = "catchAndRethrow";
        final var description = "Catch exceptions during SQL execution and re-throw them as RuntimeExceptions";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value for `catchAndRethrow` is `true`. This will catch any `SQLException` that happen during SQL execution and re-throw them as `RuntimeExceptions`.")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void writeSome() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("In case you want to handle `SQLException`s yourself, set `catchAndRethrow` to `false`.")
                        .setResult("""
                                package com.example.persistence;
                                                                
                                import java.sql.SQLException;

                                public class SomeRepository {

                                    public void writeSome() throws SQLException {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting writesReturnUpdateCount() {
        final var name = "writesReturnUpdateCount";
        final var description = "Writing method which are using `ReturningMode.NONE` return the number of affected rows instead.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting throwOnMultipleResultsForSingle() {
        final var name = "throwOnMultipleResultsForSingle";
        final var description = "Throw an exception in case a statement using `ReturningMode.SINGLE` produces more than 1 result.";
        final var value = false;
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting injectConverters() {
        final var description = "Toggles whether converters should be injected as constructor parameters.";
        final var value = false;
        return setting(GROUP_NAME, INJECT_CONVERTERS, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `injectConverters` configuration option is `false`. It produces code similar to this:")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    private final DataSource dataSource;
                                    private final ToMapConverter toMap;

                                    public SomeRepository(final DataSource dataSource) {
                                        this.dataSource = dataSource;
                                        this.toMap = new ToMapConverter();
                                    }

                                    // ... rest of generated code

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("Changing the `injectConverters` configuration option to `true` generates the following code instead:")
                        .setResult("""
                                package your.own.domain;

                                public class SomeRepository {

                                    private final DataSource dataSource;
                                    private final ToMapConverter toMap;

                                    public SomeRepository(final DataSource dataSource, final ToMapConverter toMap) {
                                        this.dataSource = dataSource;
                                        this.toMap = toMap;
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting standardPrefix() {
        final var name = "standardPrefix";
        final var description = "The method prefix to use for generated standard methods.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value for `standardPrefix` is the empty string. It does not add any prefix in front of standard methods.")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void writeSome() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("myPrefix")
                        .setDescription("In case you want to prefix standard methods with something, set the `standardPrefix` option.")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void myPrefixWriteSome() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting standardSuffix() {
        final var name = "standardSuffix";
        final var description = "The method suffix to use for generated standard methods.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value for `standardSuffix` is the empty string. It does not add any suffix after of standard methods.")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void writeSome() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("mySuffix")
                        .setDescription("In case you want to suffix standard methods with something, set the `standardSuffix` option.")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void writeSomeMySuffix() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting batchPrefix() {
        final var name = "batchPrefix";
        final var description = "The method prefix to use for generated batch methods.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value for `batchPrefix` is the empty string. It does not add any prefix in front of batch methods.")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void writeSomeBatch() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("myPrefix")
                        .setDescription("In case you want to prefix batch methods with something, set the `batchPrefix` option.")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void myPrefixWriteSomeBatch() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting batchSuffix() {
        final var name = "batchSuffix";
        final var description = "The method suffix to use for generated batch methods.";
        final var value = "Batch";
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value for `batchSuffix` is 'Batch'. It adds the word 'Batch' after each batch method.")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void writeSomeBatch() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("Other")
                        .setDescription("In case you want to suffix batch methods with something else, set the `batchSuffix` option.")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void writeSomeOther() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting allowedWritePrefixes() {
        final var defaultPrefixes = List.of("update", "insert", "delete", "create", "write", "add", "remove", "merge", "drop");
        final var prefixesInDocs = stringsInDocs(defaultPrefixes);
        final var name = "allowedWritePrefixes";
        final var description = "Configures which name prefixes are allowed for statements that are writing data to your database.";
        final var type = TypicalTypes.listOf(String.class);
        return stringListSetting(defaultPrefixes, name, description, type)
                .addExamples(ConfigurationExample.builder()
                        .setValue(prefixesInDocs)
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
        final var prefixesInDocs = stringsInDocs(defaultPrefixes);
        final var name = "allowedReadPrefixes";
        final var description = "Configures which name prefixes are allowed for statements that are reading data from your database.";
        final var type = TypicalTypes.listOf(String.class);
        return stringListSetting(defaultPrefixes, name, description, type)
                .addExamples(ConfigurationExample.builder()
                        .setValue(prefixesInDocs)
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
        final var prefixesInDocs = stringsInDocs(defaultPrefixes);
        final var name = "allowedCallPrefixes";
        final var description = "Configures which name prefixes are allowed for statements that are calling stored procedures.";
        final var type = TypicalTypes.listOf(String.class);
        return stringListSetting(defaultPrefixes, name, description, type)
                .addExamples(ConfigurationExample.builder()
                        .setValue(prefixesInDocs)
                        .setDescription(String.format("The default value of the `allowedCallPrefixes` configuration option is `%s` to allow several commonly used names for calling procedures.", prefixesInDocs))
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("do")
                        .setDescription("Changing the `allowedCallPrefixes` configuration option to `do` only allows names with the prefix `do` to call stored procedures.")
                        .build())
                .build();
    }

    private static ImmutableConfigurationSetting.BuildFinal stringListSetting(
            final List<String> values,
            final String name,
            final String description,
            final ParameterizedTypeName type) {
        final var prefixesInCode = stringsInCode(values);
        final var prefixesInDocs = stringsInDocs(values);
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setAntInitializer(CodeBlock.of(".set$L($L)\n", upperCase(name), name))
                .setCliInitializer(CodeBlock.of(".set$L($L)\n", upperCase(name), name))
                .setGradleInitializer(CodeBlock.of(".set$L($L().get())\n", upperCase(name), gradlePropertyName(name)))
                .setMavenInitializer(CodeBlock.of(".set$L($L)\n", upperCase(name), name))
                .setGradleConvention(CodeBlock.of("$L().convention($T.of($L))", gradlePropertyName(name), List.class, prefixesInCode))
                .addAntFields(antField(type, name, description, CodeBlock.of("$T.of($L)", List.class, prefixesInCode)))
                .addAntMethods(antSetter(type, name, description))
                .addCliFields(picocliOption(type, GROUP_NAME, name, description, prefixesInDocs, ", "))
                .addGradleMethods(gradleProperty(gradleListPropertyOf(ClassName.get(String.class)), name, description))
                .addImmutableMethods(immutableMethod(type, name, description, CodeBlock.of("$T.of($L)", List.class, prefixesInCode)))
                .addMavenFields(mavenParameter(type, name, description, prefixesInDocs, CodeBlock.of("$T.of($L)", List.class, prefixesInCode)));
    }

    private static String stringsInDocs(final List<String> defaultPrefixes) {
        final var prefixesInDocs = new StringJoiner(", ");
        defaultPrefixes.forEach(prefixesInDocs::add);
        return prefixesInDocs.toString();
    }

    private static String stringsInCode(final List<String> defaultPrefixes) {
        final var prefixesInCode = new StringJoiner("\", \"", "\"", "\"");
        defaultPrefixes.forEach(prefixesInCode::add);
        return prefixesInCode.toString();
    }

    private static ConfigurationSetting validateMethodNamePrefixes() {
        final var name = "validateMethodNamePrefixes";
        final var description = "Validate user given names against list of allowed prefixes per type.";
        final var value = false;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `validateMethodNamePrefixes` configuration option is `false` which disables the validation of names according to your configured prefixes.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(true))
                        .setDescription("Changing the `validateMethodNamePrefixes` configuration option to `true` enables the validation of method names.")
                        .build())
                .build();
    }

    private Repositories() {
        // data class
    }

}
