/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.ParameterizedTypeName;
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
                executeOncePrefix(),
                executeOnceSuffix(),
                executeBatchPrefix(),
                executeBatchSuffix());
    }

    /**
     * @return Boolean configuration settings that can be set on repository or method level.
     */
    public static List<ConfigurationSetting> booleanMethods() {
        return List.of(
                executeOnce(),
                executeBatch(),
                usePreparedStatement(),
                catchAndRethrow(),
                writesReturnUpdateCount(),
                throwOnMultipleResults(),
                createConnection(),
                generateConnectionOverloads());
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
                .setExplanation("""
                        Writes an interface next to each repository, declaring the same methods, and has the
                        repository implement it. Depend on the interface and the class it is implemented by
                        becomes something you can substitute — a stub in a test, a caching decorator, a fake
                        that never touches a database.

                        The interface is named by
                        [repositoryInterfacePrefix](../repositoryinterfaceprefix/) and
                        [repositoryInterfaceSuffix](../repositoryinterfacesuffix/), neither of which is set by
                        default, so a `TenantRepository` implements a `TenantRepository` interface in the same
                        package — which does not compile. Set at least one of them before turning this on.""")
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("The default. A repository is a class and nothing else:")
                        .setResult("""
                                package com.example.persistence;

                                public final class TenantRepository {

                                    // ... rest of generated code

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("With `repositoryInterfaceSuffix` set to `Api`, the repository implements an interface your code can depend on instead:")
                        .setResult("""
                                package com.example.persistence;

                                public interface TenantRepositoryApi {

                                    Optional<Tenant> findTenant(UUID id);

                                }

                                public final class TenantRepository implements TenantRepositoryApi {

                                    // ... rest of generated code

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting executeOnce() {
        final var name = Constants.EXECUTE_ONCE;
        final var description = "Generate methods that are executed once with the given parameters";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("""
                        The ordinary method: one call, one execution, the parameters you passed. It is what you
                        want for almost every statement, which is why it is on.

                        Turning it off for a statement leaves only its batch method, which is worth doing for a
                        statement that is never sensibly run for a single row — a bulk import, say. A statement
                        with both turned off generates nothing, and `YoSQL` warns rather than leaving you to
                        notice the missing method.

                        The name is [executeOncePrefix](../executeonceprefix/) and
                        [executeOnceSuffix](../executeoncesuffix/) around the statement's own, neither set by
                        default.""")
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default:")
                        .setResult("""
                                public final class TenantRepository {

                                    public int insertTenant(final UUID id, final String slug) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Only the batch method is generated:")
                        .setResult("""
                                public final class TenantRepository {

                                    public int[] insertTenantBatch(final UUID[] id, final String[] slug) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting executeBatch() {
        final var name = Constants.EXECUTE_BATCH;
        final var description = "Generate methods that are executed as batch";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("""
                        A second method taking an array per parameter, adding a batch entry per element and
                        answering with the `int[]` the driver gives back. One round trip instead of one per row,
                        which is the difference between an import that takes a minute and one that takes an hour.

                        Only writing statements get one — batching a query would have nowhere to put the results.
                        Turn it off for a statement that is never run in bulk, if the extra method bothers you;
                        it costs nothing to leave on.

                        The name is [executeBatchPrefix](../executebatchprefix/) and
                        [executeBatchSuffix](../executebatchsuffix/) around the statement's own, the suffix being
                        `Batch` by default.""")
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default. A batch method sits alongside the single-row one:")
                        .setResult("""
                                public final class TenantRepository {

                                    public int insertTenant(final UUID id, final String slug) {
                                        // ... rest of generated code
                                    }

                                    public int[] insertTenantBatch(final UUID[] id, final String[] slug) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("No batch method is generated:")
                        .setResult("""
                                public final class TenantRepository {

                                    public int insertTenant(final UUID id, final String slug) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting usePreparedStatement() {
        final var name = "usePreparedStatement";
        final var description = "Should `PreparedStatement`s be used instead of `Statement`s";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .setGenerateDocs(false)
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
                .setExplanation("""
                        What a write answers with when it returns no rows. On, it is the number of rows the
                        statement changed; off, the method is `void`.

                        The count is worth having: an update that changed no rows is usually a bug the caller can
                        act on, and there is nowhere else to learn that. Turn it off for a statement whose count
                        is meaningless — a `create table`, or DDL generally.

                        This applies to [returning](../../sql/returningmode/) `none` only. A write that returns
                        rows answers with them.""")
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default. The caller can see how many rows the statement changed:")
                        .setResult("""
                                public final class TenantRepository {

                                    public int insertTenant(final UUID id, final String slug) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("The count is dropped:")
                        .setResult("""
                                public final class TenantRepository {

                                    public void insertTenant(final UUID id, final String slug) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting throwOnMultipleResults() {
        final var name = "throwOnMultipleResults";
        final var description = "Throw an exception in case a statement using `ReturningMode.SINGLE` produces more than 1 result.";
        final var value = false;
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("""
                        What a [returning](../../sql/returningmode/) `single` statement does when the database
                        answers with more than one row. Off, the first row is returned and the rest are dropped;
                        on, it throws.

                        A statement declared `single` is a claim that at most one row can match. Where that claim
                        rests on a unique constraint the database enforces, the check is redundant and the
                        default is right. Where it rests on you being sure — a lookup by something that ought to
                        be unique but is not constrained to be — turning this on converts a silent wrong answer
                        into a loud one.

                        It costs nothing at run time beyond reading one more row.""")
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("The default. A second row is ignored:")
                        .setResult("""
                                public final class TenantRepository {

                                    public Optional<Tenant> findTenantBySlug(final String slug) {
                                        // ... reads every row, answers with the first
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("A second row is a defect, and says so:")
                        .setResult("""
                                public final class TenantRepository {

                                    public Optional<Tenant> findTenantBySlug(final String slug) {
                                        // ... throws when the statement answers with more than one row
                                    }

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting createConnection() {
        final var name = "createConnection";
        final var description = "Controls whether the generated code should create/open connection itself or use a given connection.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("""
                        A method either takes a connection from the repository's `DataSource` and closes it when it
                        is done, or takes a `java.sql.Connection` as its first parameter and leaves it open for
                        whoever passed it in.

                        With [generateConnectionOverloads](../generateconnectionoverloads/) left on — it is on by
                        default — you get both and this setting decides nothing you have to think about. It matters
                        when you turn overloads off and want one shape rather than the other.""")
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default. The method opens a connection and closes it:")
                        .setResult("""
                                public final class SomeRepository {

                                    public List<Tenant> findTenants() {
                                        try (final var connection = dataSource.getConnection()) {
                                            // ... rest of generated code
                                        }
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("The connection comes from the caller, so the statement can join work already in progress:")
                        .setResult("""
                                public final class SomeRepository {

                                    public List<Tenant> findTenants(final Connection connection) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting generateConnectionOverloads() {
        final var name = "generateConnectionOverloads";
        final var description = "Generate both a DataSource-based method and a Connection-taking overload for every statement.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("""
                        Whether a statement gets its connection from the repository's `DataSource` or from the
                        caller is not a property of the statement — it is what the call site needs. One caller
                        reads a tenant on its own; another reads it as part of a transaction it already opened. So
                        every statement generates both, as overloads of one name:

                        ```java
                        Optional<Tenant> findTenant(UUID id);
                        Optional<Tenant> findTenant(Connection connection, UUID id);
                        ```

                        The `Connection` overload runs the statement on the connection it is given and does not
                        close it, which is what lets several statements share a transaction — see
                        [transactions](../../../sql/transactions/).

                        Turning this off generates only the shape
                        [createConnection](../createconnection/) selects, one method per statement.""")
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default. Each statement is reachable both ways:")
                        .setResult("""
                                public final class TenantRepository {

                                    public Optional<Tenant> findTenant(final UUID id) {
                                        try (final var connection = dataSource.getConnection()) {
                                            return findTenant(connection, id);
                                        }
                                        // ... rest of generated code
                                    }

                                    public Optional<Tenant> findTenant(final Connection connection, final UUID id) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Only the shape `createConnection` asks for is generated:")
                        .setResult("""
                                public final class TenantRepository {

                                    public Optional<Tenant> findTenant(final UUID id) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
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

    private static ConfigurationSetting executeOncePrefix() {
        final var name = "executeOncePrefix";
        final var description = "The method prefix to use for generated methods that execute once.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value for `executeOncePrefix` is the empty string. It does not add any prefix in front of methods that are executed once.")
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
                        .setDescription("In case you want to prefix methods that execute once with something, set the `executeOncePrefix` option.")
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

    private static ConfigurationSetting executeOnceSuffix() {
        final var name = "executeOnceSuffix";
        final var description = "The method suffix to use for generated methods that execute once.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value for `executeOnceSuffix` is the empty string. It does not add any suffix after methods that execute once.")
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
                        .setDescription("In case you want to suffix methods that execute once with something, set the `executeOnceSuffix` option.")
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

    private static ConfigurationSetting executeBatchPrefix() {
        final var name = "executeBatchPrefix";
        final var description = "The method prefix to use for generated methods that execute in a batch.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value for `executeBatchPrefix` is the empty string. It does not add any prefix in front of batch methods.")
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
                        .setDescription("In case you want to prefix batch methods with something, set the `executeBatchPrefix` option.")
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

    private static ConfigurationSetting executeBatchSuffix() {
        final var name = "executeBatchSuffix";
        final var description = "The method suffix to use for generated methods that execute in a batch.";
        final var value = "Batch";
        return setting(GROUP_NAME, name, description, value)
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value for `executeBatchSuffix` is 'Batch'. It adds the word 'Batch' after each batch method.")
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
                        .setDescription("In case you want to suffix batch methods with something else, set the `executeBatchSuffix` option.")
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
                        .setDescription("The default value of the `allowedWritePrefixes` configuration option is `%s` to allow several commonly used names for writing statements.".formatted(prefixesInDocs))
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
                        .setDescription("The default value of the `allowedReadPrefixes` configuration option is `%s` to allow several commonly used names for reading statements.".formatted(prefixesInDocs))
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
                        .setDescription("The default value of the `allowedCallPrefixes` configuration option is `%s` to allow several commonly used names for calling procedures.".formatted(prefixesInDocs))
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
