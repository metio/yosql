/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.models.configuration.SchemaValidation;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.nio.file.Path;

import static wtf.metio.yosql.internals.jdk.Strings.upperCase;

/**
 * Configures what `YoSQL` does with the schema your project already describes.
 */
public final class Schema extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Schema.class.getSimpleName();
    private static final String PROJECT_BASE_DIRECTORY = "projectBaseDirectory";

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures how `YoSQL` checks statements against the schema they run against.")
                .setExplanation("""
                        `YoSQL` reads the `create table` statements your project already keeps and checks the rest of
                        your SQL against them: a column no table declares, a parameter whose type disagrees with the
                        column it is compared against, a record component that cannot hold what its column can
                        contain.

                        Nothing here connects to a database. The schema comes from your own DDL, which is why
                        generation still works in a checkout with no services running — and why it costs nothing in
                        a build that has no database available.

                        Anything it cannot read, it says nothing about. A statement in a dialect the parser does not
                        cover, a table created from a select, a type nobody mapped: each of those is *unknown*, and
                        an unknown skips a check rather than failing it. That is deliberate, and it is what makes it
                        safe to turn on in a project that was not written with it in mind.""")
                .addSettings(validation())
                .addSettings(sqlStatementsDirectory())
                .addSettings(vendor())
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                // Same as Files: a directory the user wrote is relative to their project, not to
                // whatever directory the build happens to have been started from.
                .addAntParameters(ParameterSpec.builder(Path.class, PROJECT_BASE_DIRECTORY, Modifier.FINAL).build())
                .addCliParameters(ParameterSpec.builder(Path.class, PROJECT_BASE_DIRECTORY, Modifier.FINAL).build())
                .addGradleParameters(ParameterSpec.builder(Path.class, PROJECT_BASE_DIRECTORY, Modifier.FINAL).build())
                .addMavenParameters(ParameterSpec.builder(Path.class, PROJECT_BASE_DIRECTORY, Modifier.FINAL).build())
                .build();
    }

    private static ConfigurationSetting validation() {
        final var name = "validation";
        final var description = "What to do about a statement that disagrees with the schema.";
        final var type = TypeName.get(SchemaValidation.class);
        final var value = SchemaValidation.WARN;
        return enumSetting(GROUP_NAME, name, description, value, type)
                .setExplanation("""
                        `WARN` is the default, so upgrading adds warnings and never a failure. Turning it up to
                        `ERROR` in an existing project is a migration rather than a switch: the first run finds
                        everything at once, and a build that fails on all of it is a build nobody can bisect. Deal
                        with what `WARN` reports first, then move up so that nothing new gets in.

                        A single statement can opt out with
                        [validateSchema](../../sql/validateschema/) in its front matter, for the query this cannot
                        read and you do not want to argue with.""")
                .addExamples(ConfigurationExample.builder()
                        .setValue(SchemaValidation.OFF.name())
                        .setDescription("Says nothing at all. Statements are generated exactly as they were before any of this existed.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(SchemaValidation.WARN.name())
                        .setDescription("The default. Every disagreement is reported and the build carries on, so nothing that passed before starts failing.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(SchemaValidation.ERROR.name())
                        .setDescription("A disagreement fails the build, naming the file, the statement and what it disagreed about.")
                        .build())
                .build();
    }

    private static ConfigurationSetting vendor() {
        final var name = "vendor";
        final var description = "Which database your DDL is written for, when the DDL itself does not say.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("""
                        Names the database whose spellings the schema uses, so that `bytea`, `timestamptz`,
                        `bigserial`, `jsonb` and the rest of a dialect's own type names are understood. Without it
                        only the spellings every database shares are — a column declared `bytea` reads as *unknown*,
                        which means no inferred parameter type and no generated result row type for any statement
                        touching it.

                        A schema file can say the same thing with a `-- vendor:` line of its own, and that is the
                        better place when a project describes several databases. This setting exists for the schema
                        you cannot edit: a Flyway or Liquibase migration directory, where every file is checksummed
                        and adding a comment to one already applied makes the tool refuse to run.

                        Write it the way a JDBC driver reports the database, which is the same spelling a
                        statement's own [vendor](../../sql/vendor/) is matched against — `PostgreSQL`, `MySQL`,
                        `MariaDB`, `H2`, `Oracle`, `Microsoft SQL Server`. It says nothing about which statements
                        are generated: every statement still applies to every database, and a file that names its
                        own vendor keeps it.""")
                .addExamples(ConfigurationExample.builder()
                        .setValue("PostgreSQL")
                        .setDescription("Reads the schema as PostgreSQL's, so a `bytea` column gives `byte[]` and a `timestamptz` gives `Instant`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("")
                        .setDescription("The default. Only the type spellings every supported database shares are understood, unless a schema file names a vendor itself.")
                        .build())
                .build();
    }

    private static ConfigurationSetting sqlStatementsDirectory() {
        final var name = "sqlStatementsDirectory";
        final var description = "Where the DDL describing your schema lives, when it is not among your statements.";
        final var value = "";
        final var configured = setting(GROUP_NAME, name, description, value)
                .setExplanation("""
                        Left empty — the default — the schema is read from the statements `YoSQL` already parses, so
                        a project that keeps its `create table` statements alongside its queries needs no
                        configuration at all.

                        Point this at a directory to read the DDL from somewhere else: a Flyway or Liquibase
                        migrations directory, most usefully. `alter table` applies to whatever came before it, so
                        the files are read in the order they applied: a `V<version>__<description>.sql` name is
                        ordered by its version, comparing each numeric segment as a number, which puts `V2__` ahead
                        of `V10__` where sorting the names as text would not. Everything else — a repeatable
                        `R__` migration, a plain `schema.sql` — keeps name order after those, so a directory that
                        is not migrations at all is read exactly as its names sort.

                        DDL marked with a [vendor](../../sql/vendor/) builds that vendor's schema rather than the
                        shared one, so a project supporting several databases describes only the tables that differ
                        more than once. The mark also decides how the column types are spelled: `bytea`,
                        `timestamptz` and `bigserial` mean what PostgreSQL means by them once the DDL says it is
                        PostgreSQL's, and every statement reading those columns gets the type without declaring a
                        vendor of its own. Marking the schema is the way to say which database a project uses;
                        marking a statement says the narrower thing, that it is written for that database and is
                        the fallback for no other.""")
                .build();
        // Resolved against the project rather than taken as written: every other directory setting
        // is, and a relative one taken as written looks in whatever directory the build was started
        // from — which, from a reactor root, is not the module's. Applied over the defaults the
        // string helper wrote rather than beside them, because the builder takes each once.
        return ConfigurationSetting.copyOf(configured)
                .withAntInitializer(resolveAgainstProject(name))
                .withCliInitializer(resolveAgainstProject(name))
                .withGradleInitializer(resolveAgainstProjectInGradle(name))
                .withMavenInitializer(resolveAgainstProject(name));
    }

    /**
     * Leaves an unset directory alone, so that "read the schema from the statements themselves"
     * stays the default rather than becoming the project's own root.
     */
    private static CodeBlock resolveAgainstProject(final String name) {
        return CodeBlock.of(".set$L($L == null || $L.isBlank() ? $L : $L.resolve($L).toAbsolutePath().toString())\n",
                upperCase(name), name, name, name, PROJECT_BASE_DIRECTORY, name);
    }

    /**
     * The same, where the value is read off a Gradle property rather than a field.
     */
    private static CodeBlock resolveAgainstProjectInGradle(final String name) {
        final var value = gradlePropertyName(name) + "().get()";
        return CodeBlock.of(".set$L($L.isBlank() ? $L : $L.resolve($L).toAbsolutePath().toString())\n",
                upperCase(name), value, value, PROJECT_BASE_DIRECTORY, value);
    }

    private Schema() {
        // data class
    }

}
