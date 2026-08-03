/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.models.configuration.SchemaValidation;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

/**
 * Configures what `YoSQL` does with the schema your project already describes.
 */
public final class Schema extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Schema.class.getSimpleName();

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
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .build();
    }

    private static ConfigurationSetting validation() {
        final var name = "validation";
        final var description = "What to do about a statement that disagrees with the schema.";
        final var type = TypeName.get(SchemaValidation.class);
        final var value = SchemaValidation.OFF;
        return enumSetting(GROUP_NAME, name, description, value, type)
                .setExplanation("""
                        Turning this on in an existing project is a migration rather than a switch: the first run
                        finds everything at once, and a build that fails on all of it at the same time is a build
                        nobody can bisect. Start at `WARN`, fix what it reports, then move to `ERROR` so that
                        nothing new gets in.

                        A single statement can opt out with
                        [validateSchema](../../sql/validateschema/) in its front matter, for the query this cannot
                        read and you do not want to argue with.""")
                .addExamples(ConfigurationExample.builder()
                        .setValue(SchemaValidation.OFF.name())
                        .setDescription("The default. Statements are generated exactly as they were before any of this existed.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(SchemaValidation.WARN.name())
                        .setDescription("Every disagreement is reported and the build carries on. This is what to turn on first.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(SchemaValidation.ERROR.name())
                        .setDescription("A disagreement fails the build, naming the file, the statement and what it disagreed about.")
                        .build())
                .build();
    }

    private static ConfigurationSetting sqlStatementsDirectory() {
        final var name = "sqlStatementsDirectory";
        final var description = "Where the DDL describing your schema lives, when it is not among your statements.";
        final var value = "";
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("""
                        Left empty — the default — the schema is read from the statements `YoSQL` already parses, so
                        a project that keeps its `create table` statements alongside its queries needs no
                        configuration at all.

                        Point this at a directory to read the DDL from somewhere else: a Flyway or Liquibase
                        migrations directory, most usefully. Files are read in name order, and `alter table` applies
                        to whatever came before it, which is what makes `V1__create.sql` followed by
                        `V2__add_column.sql` describe the schema those two migrations leave behind.

                        DDL marked with a [vendor](../../sql/vendor/) builds that vendor's schema rather than the
                        shared one, so a project supporting several databases describes only the tables that differ
                        more than once.""")
                .build();
    }

    private Schema() {
        // data class
    }

}
