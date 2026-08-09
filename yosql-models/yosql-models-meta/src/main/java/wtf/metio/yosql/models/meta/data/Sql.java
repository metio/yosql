/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.javapoet.*;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.*;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static wtf.metio.yosql.models.meta.data.Repositories.INJECT_CONVERTERS;

public final class Sql extends AbstractConfigurationGroup {

    /**
     * What tells a batch method apart from the one that runs a statement once. Both are generated
     * from the same statement, so they cannot both be called what the statement is called.
     */
    private static final String BATCH_SUFFIX = "Batch";

    private static final String GROUP_NAME = Sql.class.getSimpleName();
    private static final String MERGE = "merge";
    private static final String RESULT_ROW_CONVERTER = "resultRowConverter";

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("The configuration for a single SQL statement.")
                .setExplanation("""
                        All of these options are to be placed in the front matter of SQL statements and their overwrite
                        their respective counterparts in the global configuration, e.g. in [repositories](../repositories/).""")
                .addAllSettings(settings())
                .addAllSettings(withExtraAnnotations(stringRepositorySettings()))
                .addAllSettings(withExtraAnnotations(booleanRepositorySettings()))
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableMethods(
                        fromStatements(),
                        merge(),
                        converter(),
                        executeOnceName(),
                        executeBatchName(),
                        joinMethodNameParts())
                .addImmutableAnnotations(immutableAnnotation())
                .addImmutableAnnotations(AnnotationSpec.builder(JsonSerialize.class)
                        .addMember("as", "$T.class", immutableType(immutableConfigurationName(GROUP_NAME)))
                        .build())
                .addImmutableAnnotations(AnnotationSpec.builder(JsonDeserialize.class)
                        .addMember("as", "$T.class", immutableType(immutableConfigurationName(GROUP_NAME)))
                        .build())
                .build();
    }

    private static List<ConfigurationSetting> settings() {
        return List.of(
                repository(),
                repositoryInterface(),
                name(),
                description(),
                validateSchema(),
                injectConverter(),
                generateResultRowType(),
                vendor(),
                type(),
                returningMode(),
                resultRowConverter(),
                resultRowType(),
                resultRowColumns(),
                parameters(),
                annotations());
    }

    private static List<ConfigurationSetting> stringRepositorySettings() {
        return Repositories.stringMethods().stream()
                .<ConfigurationSetting>map(setting -> ConfigurationSetting.copyOf(setting)
                        .withImmutableMethods(immutableMethod(ClassName.get(String.class),
                                setting.name(), setting.description())))
                .toList();
    }

    private static List<ConfigurationSetting> booleanRepositorySettings() {
        return Repositories.booleanMethods().stream()
                .filter(setting -> !INJECT_CONVERTERS.equals(setting.name()))
                .<ConfigurationSetting>map(setting -> ConfigurationSetting.copyOf(setting)
                        .withImmutableMethods(immutableMethod(ClassName.get(Boolean.class),
                                setting.name(), setting.description())))
                .toList();
    }

    /**
     * Names the wire key of every repository setting a statement may override.
     *
     * <p>On the method rather than on the setting: a setting's own annotation lists are read by
     * nobody — the generators annotate the group and the members it declares — so this was written
     * where it had no effect, and stayed unnoticed only because every one of these keys happens to
     * equal its setting's name. The first one needing a different key would have been bound from a
     * name Jackson never saw.</p>
     */
    private static List<? extends ConfigurationSetting> withExtraAnnotations(final List<ConfigurationSetting> settings) {
        return settings.stream()
                .map(setting -> ConfigurationSetting.copyOf(setting)
                        .withImmutableMethods(setting.immutableMethods().stream()
                                .map(method -> method.toBuilder()
                                        .addAnnotation(jsonProperty(setting.name()))
                                        .build())
                                .toList()))
                .toList();
    }

    private static AnnotationSpec jsonProperty(final String name) {
        return AnnotationSpec.builder(JsonProperty.class).addMember("value", "$S", name).build();
    }

    private static ConfigurationSetting repository() {
        final var name = "repository";
        final var description = "The fully qualified name of the target repository class.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setExplanation("""
                        In order to overwrite the target repository of a single SQL statement, use the `repository` option.
                        You can specify the fully qualified name of the repository that should contain your statement, or
                        you can just specify the name of the class and `YoSQL` will add the
                        [base package name](../../repositories/basepackagename/) and the `Repository` suffix
                        for you.""")
                .addTags(Tags.FRONT_MATTER)
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description))
                .build();
    }

    private static ConfigurationSetting repositoryInterface() {
        final var name = "repositoryInterface";
        final var description = "The fully qualified name of the target repository interface.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description, AnnotationSpec.builder(JsonIgnore.class).build()))
                .setGenerateDocs(false)
                .build();
    }

    private static ConfigurationSetting name() {
        final var name = "name";
        final var description = "The name of the SQL statement";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("yourSpecialName")
                        .setDescription("In case you use the `name` option, `YoSQL` will use the supplied value as a method name")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    public void yourSpecialName() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting generateResultRowType() {
        final var name = "generateResultRowType";
        final var description = "Write the record this statement builds its rows into.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setFrontMatterExampleCode("true")
                .setExplanation("""
                        Everything the record needs is already known — which columns the statement selects, what
                        each one holds, whether it can be null — so naming a record that does not exist yet and
                        setting this writes it for you, next to the converter that builds it.

                        ```sql
                        -- name: findTenant
                        -- returning: single
                        -- resultRowType: com.example.domain.Tenant
                        -- generateResultRowType: true
                        select id, slug, created_at from tenant
                        ```

                        Opt in per statement rather than whenever the record is missing. A `resultRowType` naming
                        a record that is not there is far more often a typo than a request, and writing a new
                        record for a misspelled name would replace a build error with a mystery.

                        It needs a [schema](../../schema/validation/) it can read, and it needs every selected
                        column to be one the catalog describes with a type it maps. Where any of that is missing
                        the record is not written, and the statement fails as it would have before — because half
                        a record is worse than none.

                        The record it writes is yours to take over: copy it into your own sources, drop the
                        setting, and nothing else changes.""")
                .addImmutableMethods(immutableMethod(ClassName.get(Boolean.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting validateSchema() {
        final var name = "validateSchema";
        final var description = "Whether this statement is checked against the schema.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setFrontMatterExampleCode("false")
                .setExplanation("""
                        Schema validation reads what it can and says nothing about the rest, so a statement it
                        cannot parse costs you nothing already. This is for the other case: a statement it *can*
                        read and gets wrong, or one whose schema lives somewhere `YoSQL` was never told about.

                        Set it to `false` and this statement is generated without any of the checks
                        [validation](../../schema/validation/) turns on. Nothing else changes about it.""")
                .addImmutableMethods(immutableMethod(ClassName.get(Boolean.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting injectConverter() {
        final var name = "injectConverter";
        final var description = "Whether this statement's converter is a constructor parameter of its repository.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setFrontMatterExampleCode("true")
                .setExplanation("""
                        A generated converter is constructed with no arguments, so a mapper that has to close over
                        something — a resolver, a tenant key, a clock — cannot be one. The way out is to write the
                        converter yourself and have the repository take it, which is what
                        [injectConverters](../../repositories/injectconverters/) does for every repository in the
                        project at once. In a project with dozens of them already wired up by hand, changing every
                        constructor for the sake of one statement is not a trade worth making.

                        Set this on the statement instead and only that converter is injected. The repository it
                        belongs to takes one more constructor parameter; every other repository is generated exactly
                        as before.

                        Statements sharing a converter share its field, so one of them asking is enough for it to
                        be injected — there is one field and it is initialised once. The converter needs an alias,
                        since that is what names the parameter.""")
                .addImmutableMethods(immutableMethod(ClassName.get(Boolean.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting description() {
        final var name = "description";
        final var description = "The description for the SQL statement";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("Some random description")
                        .setDescription("In case you use the `description` option, `YoSQL` will use the supplied value as a JavaDoc comment")
                        .setResult("""
                                package com.example.persistence;

                                public class SomeRepository {

                                    /**
                                     * Some random description
                                     */
                                    public void someMethod() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting vendor() {
        final var name = "vendor";
        final var description = "The vendor name of the database the SQL statement is intended for";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setFrontMatterExampleCode("PostgreSQL")
                .setExplanation("""
                        Where one query cannot be written for every database you support, write one statement per
                        database and give them the same [name](../name/). They become a single method that picks
                        its statement from the connection at run time.

                        The value is matched against the JDBC driver's own
                        `DatabaseMetaData.getDatabaseProductName()`, so it has to be exactly what the driver
                        reports — `PostgreSQL`, `MySQL`, `H2`, `Microsoft SQL Server`, `Oracle`. A statement of the
                        same name **without** a vendor is the fallback for every database not named, and a name
                        with no fallback fails against a database none of its statements claim.

                        Nothing here inspects your SQL. Naming a vendor selects a statement; it does not translate
                        one.""")
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("PostgreSQL")
                        .setDescription("Statements sharing a name become one method that chooses between them:")
                        .setResult("""
                                public final class TenantRepository {

                                    public List<Tenant> findTenants() {
                                        try (final var connection = dataSource.getConnection()) {
                                            final var databaseMetaData = connection.getMetaData();
                                            final var databaseProductName = databaseMetaData.getDatabaseProductName();
                                            String query = null;
                                            switch (databaseProductName) {
                                              case "PostgreSQL":
                                                query = FIND_TENANTS_POSTGRE_SQL;
                                                break;
                                              default:
                                                query = FIND_TENANTS;
                                                break;
                                            }
                                            // ... rest of generated code
                                        }
                                    }

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting type() {
        final var name = "type";
        final var description = "The type of the SQL statement.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setFrontMatterExampleCode("reading")
                .setExplanation("""
                        Whether a statement reads, writes, or calls a stored procedure. It decides what is
                        generated for it, and it is normally not written at all: the statement's
                        [name](../name/) settles it.

                        A name beginning with one of the
                        [read prefixes](../../repositories/allowedreadprefixes/) is `reading`, one of the
                        [write prefixes](../../repositories/allowedwriteprefixes/) is `writing`, and one of the
                        [call prefixes](../../repositories/allowedcallprefixes/) is `calling`. `YoSQL` does not
                        parse your SQL to work this out, and it does not need to.

                        Set it where you want a name the prefixes do not cover — a `countTenants` that reads, a
                        `purgeTenants` that writes. A statement whose name matches no prefix and which sets no
                        type fails the build rather than quietly generating nothing.""")
                .addImmutableMethods(immutableMethod(ClassName.get(SqlStatementType.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("reading")
                        .setDescription("Runs the statement as a query and maps what comes back:")
                        .setResult("""
                                public final class TenantRepository {

                                    public List<Tenant> countTenants() {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("writing")
                        .setDescription("Runs the statement as an update, and generates a batch method alongside:")
                        .setResult("""
                                public final class TenantRepository {

                                    public int purgeTenants(final Instant before) {
                                        // ... rest of generated code
                                    }

                                    public int[] purgeTenantsBatch(final Instant[] before) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("calling")
                        .setDescription("Runs the statement as a `CallableStatement`, for a stored procedure:")
                        .setResult("""
                                public final class TenantRepository {

                                    public List<Tenant> rebuildTenantIndex() {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting returningMode() {
        final var name = "returningMode";
        final var description = "The returning mode of the SQL statement.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setFrontMatterExampleCode("single")
                .setExplanation("""
                        How many rows the statement answers with, which decides what the generated method returns.
                        It is written `returning` in the front matter.

                        The row type comes from elsewhere — [resultRowType](../resultrowtype/) or
                        [resultRowConverter](../resultrowconverter/), or a `Map<String, Object>` when neither is
                        named. This setting only wraps it.

                        | `returning` | You get |
                        | --- | --- |
                        | `none` | nothing, or the number of rows a write changed |
                        | `single` | `Optional<T>` |
                        | `multiple` | `List<T>` |
                        | `cursor` | `Stream<T>`, read lazily |

                        `single` is for a statement that can answer with at most one row. A second row is
                        silently ignored unless [throwOnMultipleResults](../throwonmultipleresults/) is on, which
                        is worth turning on wherever you believe the query really is unique.

                        `cursor` reads rows as the stream is consumed, so a result larger than memory is fine —
                        but the stream holds the `ResultSet`, the statement and, for the `DataSource` form, the
                        connection until it is closed. Close it. Most drivers also need a fetch size and
                        auto-commit off before they stream rather than buffer, and both are connection settings.

                        `none` on a write answers with the number of rows changed unless
                        [writesReturnUpdateCount](../writesreturnupdatecount/) says otherwise. A write that
                        returns rows — `insert ... returning *` — takes `single` or `multiple` like a read.""")
                .addImmutableMethods(immutableMethod(ClassName.get(ReturningMode.class), name, description, jsonProperty("returning")))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("single")
                        .setDescription("At most one row, so the method answers with an `Optional`:")
                        .setResult("""
                                public final class TenantRepository {

                                    public Optional<Tenant> findTenant(final UUID id) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("multiple")
                        .setDescription("Every row, read into a list before the method returns:")
                        .setResult("""
                                public final class TenantRepository {

                                    public List<Tenant> findTenants(final UUID accountId) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("cursor")
                        .setDescription("Rows as they are read, for a result that need not fit in memory. Close the stream:")
                        .setResult("""
                                public final class TenantRepository {

                                    public Stream<Tenant> findTenants(final UUID accountId) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("none")
                        .setDescription("No rows. A write answers with the number it changed:")
                        .setResult("""
                                public final class TenantRepository {

                                    public int insertTenant(final UUID id, final String slug) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build())
                .build();
    }

    private static ConfigurationSetting resultRowConverter() {
        final var name = RESULT_ROW_CONVERTER;
        final var description = "The fully-qualified name of the converter class to use";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setExplanation("""
                        Name the class and nothing else. `YoSQL` reads it to find the single public method that
                        takes a `ResultSet`; that method's name is what the repository calls, and its return type
                        is what the statement produces. The converter is injected into a field named after the
                        class, with a lower-case first letter.

                        The class has to be visible under [sourceDirectory](../../files/sourcedirectory/), because
                        it is read from source rather than loaded. Naming a class that is missing, that has no
                        such method, or that has more than one, fails the build.

                        To have the converter written for you instead, name a record with
                        [resultRowType](../resultrowtype/).""")
                .addImmutableMethods(immutableMethod(ClassName.get(ResultRowConverter.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting resultRowType() {
        final var name = "resultRowType";
        final var description = "The fully-qualified name of a record to build each result row into";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setExplanation("""
                        Name a record and `YoSQL` writes the converter for you. It reads the record's canonical
                        constructor out of its source file — under [sourceDirectory](../../files/sourcedirectory/) — and
                        emits one `resultSet.getX(...)` call per component, in a class of its own. No reflection is
                        involved at any point, so the result survives a GraalVM native image intact.

                        A component takes its column from its own name, `camelCase` read as `snake_case`: `tenantId`
                        reads the `tenant_id` column. When a column is named something else, alias it in the SQL —
                        `select amount_cents as minor_units` — which keeps the mapping next to the query that produces
                        it.

                        Components whose own type is a record are built from the same flat row: nesting groups values
                        on the Java side and the query knows nothing about it, so a nested component claims the column
                        matching its own name, not a prefixed one.

                        A type the generator cannot otherwise read works if it says how: a `static` factory called
                        `valueOf`, taking one value the generator does know, is called with the column's value. That is
                        the same convention enums follow, and it is what makes a one-component record a value wrapped
                        around a column rather than a nesting.

                        A column no component claims, or a component no column supplies, fails the build and names the
                        file, the statement and the component.""")
                .addImmutableMethods(immutableMethod(ClassName.get(String.class), name, description))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting resultRowColumns() {
        final var name = "resultRowColumns";
        final var description = "Overrides which column a component of the result row type reads";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setExplanation("""
                        A component reads the column its own name implies, `camelCase` read as `snake_case`. Where the
                        two disagree, aliasing the column in the query is usually the better answer, because it puts the
                        mapping next to the column being renamed. This is for when the query is not yours to change —
                        a view you do not own, or SQL generated elsewhere.

                        Keys are component paths from the root of the result row type, so a component of a nested record
                        is addressed through the component holding it:

                        ```sql
                        -- resultRowColumns:
                        --   amount.minorUnits: amount_cents
                        --   at: created_at
                        ```

                        A column override belongs to the type rather than to one query, so every statement naming the
                        same result row type shares one set: overrides declared on any of them apply to all of them, and
                        two statements mapping the same component to different columns fail the build.""")
                .addImmutableMethods(immutableMethod(TypicalTypes.MAP_OF_STRING_AND_STRINGS, name, description))
                .setMergeCode(CodeBlock.of("$T.mergeColumns(first.$L(), second.$L())", ResultRowColumns.class, name, name))
                .addTags(Tags.FRONT_MATTER)
                .build();
    }

    private static ConfigurationSetting parameters() {
        final var name = "parameters";
        final var description = "The parameters of the SQL statement.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setExplanation("""
                        A statement binds its parameters by name — `:tenantId` — and each becomes a method
                        parameter, in the order the names first appear in the statement. A name used more than
                        once is one method parameter bound at every position it occupies.

                        What `YoSQL` needs from you is the Java type of each one, and only when it cannot work it
                        out. A statement naming a record with [resultRowType](../resultrowtype/) takes each
                        parameter's type from the component of the same name, so a query selecting a tenant by its
                        id needs nothing here: the record already says the id is a `UUID`.

                        Where there is no such component — every write statement, for a start — name the types:

                        ```sql
                        -- parameters:
                        --   id: uuid
                        --   slug: string
                        --   createdAt: instant
                        insert into tenant (id, slug, created_at) values (:id, :slug, :createdAt)
                        ```

                        A type is a fully-qualified class name, a primitive, or one of the short names for the
                        types statements are overwhelmingly written in: `string`, `object`, `uuid`, `instant`,
                        `localdate`, `localdatetime`, `localtime`, `offsetdatetime`, `offsettime`,
                        `zoneddatetime`, `duration`, `period`, `bigdecimal`, `biginteger`, `date`, `time`,
                        `timestamp`, `array`, `blob`, `clob`, `ref`, `rowid`, `sqlxml`, `url`, `uri`, `currency`,
                        `locale`, `zoneid`, `inputstream` and `reader`. Case does not matter, so `uuid` and
                        `UUID` name the same type.

                        A parameter that neither source settles **fails the build**, naming the file, the
                        statement and every parameter still without a type. The alternative is binding it as
                        `java.lang.Object`, which compiles and then accepts anything at all — exactly the type
                        safety a generated repository exists to provide.

                        A parameter needing more than a type keeps the longer form, and the two can be mixed
                        across statements in the same file:

                        ```sql
                        -- parameters:
                        --   - name: payload
                        --     type: java.lang.String
                        --     sqlType: 2005
                        ```""")
                .addImmutableMethods(immutableMethod(TypicalTypes.listOf(SqlParameter.class), name, description))
                .setMergeCode(CodeBlock.of("$T.mergeParameters(first.$L(), second.$L())", SqlParameter.class, name, name))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("a mapping of name to type")
                        .setDescription("""
                                The short form: one line per parameter, for the parameters that need nothing but a
                                type.""")
                        .setResult("""
                                public final class SomeRepository {

                                    public int insertTenant(final UUID id, final String slug) {
                                        // ... rest of generated code
                                    }

                                }
                                """)
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("a list of objects")
                        .setDescription("""
                                The long form, for a parameter that also needs a JDBC type, a scale or a
                                variant.""")
                        .setResult("""
                                public final class SomeRepository {

                                    public int insertDocument(final String payload) {
                                        // ... rest of generated code
                                    }

                                }
                                """)
                        .build())
                .build();
    }

    private static ConfigurationSetting annotations() {
        final var name = "annotations";
        final var description = "The additional annotations to be placed on generated methods.";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setExplanation("This list is empty by default and thus no annotations are added to generated methods.")
                .addImmutableMethods(immutableMethod(TypicalTypes.listOf(Annotation.class), name, description))
                .setMergeCode(CodeBlock.of("$T.mergeAnnotations(first.$L(), second.$L())", Annotation.class, name, name))
                .addTags(Tags.FRONT_MATTER)
                .addExamples(ConfigurationExample.builder()
                        .setValue("your.own.Annotation")
                        .setDescription("In order to add an annotation to the generated methods, specify its fully-qualified name.")
                        .setResult("""
                                package com.example.persistence;
                                                                
                                import your.own.Annotation;

                                public class SomeRepository {

                                    @Annotation
                                    public void someMethod() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("your.other.Annotation")
                        .setDescription("In order to add an annotation with some member, specify name of the annotation member, its value, and its type. The type defaults to `java.lang.String`.")
                        .setResult("""
                                package com.example.persistence;
                                                                
                                import your.other.Annotation;

                                public class SomeRepository {

                                    @Annotation(member = "value")
                                    public void someMethod() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }
                                """)
                        .build())
                .setFrontMatterExampleCode("""
                        
                        --  - type: your.own.Annotation
                        --    members:
                        --      - key: someMember
                        --        value: this is your value
                        --        type: java.lang.String
                        --      - key: another
                        --        value: 5
                        --        type: int
                        --  - type: your.other.Annotation
                        --    members:
                        --      - key: value
                        --        value: yep
                        --  - type: some.annotation.WithoutMembers""")
                .build();
    }

    private static MethodSpec fromStatements() {
        final var statements = TypicalTypes.listOf(immutableType("SqlStatement"));
        final var configuration = immutableType("SqlConfiguration");
        return MethodSpec.methodBuilder("fromStatements")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(statements, "statements", Modifier.FINAL).build())
                .returns(configuration)
                .addStatement("$T latest = $T.$L().build()", configuration, configuration, BUILDER_METHOD_NAME)
                .addCode(CodeBlock.builder()
                        .beginControlFlow("for (final var $L : $L)", "statement", "statements")
                        .addStatement("final var config = $L.getConfiguration()", "statement")
                        .addStatement("latest = $L(latest, config)", MERGE)
                        .endControlFlow()
                        .build())
                .addStatement("return latest")
                .build();
    }

    private static MethodSpec merge() {
        final var configuration = immutableType("SqlConfiguration");
        return MethodSpec.methodBuilder(MERGE)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(configuration, "first", Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(configuration, "second", Modifier.FINAL).build())
                .returns(configuration)
                .addStatement(CodeBlock.builder()
                        .add("return $T.$L($L)", configuration, COPY_OF_METHOD_NAME, "first")
                        .add(withAllSettings())
                        .build())
                .build();
    }

    private static CodeBlock withAllSettings() {
        final var builder = CodeBlock.builder();
        settings().forEach(setting -> addSetting(builder, setting));
        stringRepositorySettings().forEach(setting -> addSetting(builder, setting));
        booleanRepositorySettings().forEach(setting -> addSetting(builder, setting));
        return builder.build();
    }

    private static void addSetting(final CodeBlock.Builder builder, final ConfigurationSetting setting) {
        setting.mergeCode().ifPresentOrElse(
                code -> builder.add("$>$>\n.with$L($L)$<$<", Strings.upperCase(setting.name()), code),
                () -> builder.add("$>$>\n.with$L(first.$L().or(second::$L))$<$<",
                        Strings.upperCase(setting.name()), setting.name(), setting.name()));
    }

    private static MethodSpec converter() {
        final var parameterType = TypicalTypes.supplierOf(TypicalTypes.optionalOf(ResultRowConverter.class));
        final var defaultConverter = "defaultConverter";
        return MethodSpec.methodBuilder("converter")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(parameterType, defaultConverter).build())
                .returns(ResultRowConverter.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement(CodeBlock.builder()
                        .add("return $L()", RESULT_ROW_CONVERTER)
                        .add("$>\n.or($L)", defaultConverter)
                        .add("\n.orElseThrow()$<")
                        .build())
                .build();
    }

    private static MethodSpec joinMethodNameParts() {
        return MethodSpec.methodBuilder("joinMethodNameParts")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String.class)
                .varargs(true)
                .addParameter(ParameterSpec.builder(ArrayTypeName.of(String.class), "strings", Modifier.FINAL).build())
                .addStatement("final var part = new $T()", AtomicInteger.class)
                .addStatement(CodeBlock.builder()
                        .add("return $T.stream($L)", Arrays.class, "strings")
                        .add("$>\n.filter($T::nonNull)", Objects.class)
                        .add("\n.map($T::strip)", String.class)
                        .add("\n.filter($T.not($T::isBlank))", Predicate.class, String.class)
                        .add("\n.map(string -> part.getAndIncrement() == 0")
                        .add("$>\n? string")
                        .add("\n: string.substring(0, 1).toUpperCase($T.ROOT) + string.substring(1))$<", Locale.class)
                        .add("\n.collect($T.joining())$<", Collectors.class)
                        .build())
                .build();
    }

    private static MethodSpec executeOnceName() {
        return MethodSpec.methodBuilder("executeOnceName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return $L().orElse($S)", "name", "")
                .build();
    }

    private static MethodSpec executeBatchName() {
        return MethodSpec.methodBuilder("executeBatchName")
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Value.Lazy.class)
                .addStatement("return $L().map(value -> joinMethodNameParts(value, $S)).orElse($S)", "name", BATCH_SUFFIX, "")
                .build();
    }

    private Sql() {
        // data class
    }

}
