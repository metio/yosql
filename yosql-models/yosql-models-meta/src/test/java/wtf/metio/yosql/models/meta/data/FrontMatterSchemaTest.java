/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The published schema and the model a statement is bound into say the same thing.
 *
 * <p>The schema is generated from whatever carries {@link Tags#FRONT_MATTER}, and it sets
 * {@code additionalProperties: false} — so it is not a hint but a ruling, and a disagreement is
 * wrong in both directions. A setting tagged but absent from the statement model puts a key in the
 * schema that the parser rejects, and the reader's editor happily completes it. A setting in the
 * statement model but untagged is flagged as illegal in an editor while the build accepts it.</p>
 *
 * <p>Nothing else can notice: the schema is generated, the model is generated, and no example writes
 * every setting.</p>
 */
@DisplayName("the front matter schema")
class FrontMatterSchemaTest {

    /**
     * Everything the schema generator would list, which is every group's tagged settings.
     */
    private static Set<String> tagged() {
        return Stream.concat(AllConfigurations.allConfigurationGroups(), Stream.of(Sql.configurationGroup()))
                .flatMap(group -> group.settings().stream())
                .filter(setting -> setting.tags().contains(Tags.FRONT_MATTER))
                .map(ConfigurationSetting::frontMatterKey)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Everything a statement can actually be bound from: the Sql group, less what Jackson is told to
     * ignore. {@code repositoryInterface} is derived rather than written, and carries
     * {@code JsonIgnore} to say so.
     */
    private static Set<String> bound() {
        return Sql.configurationGroup().settings().stream()
                .filter(FrontMatterSchemaTest::isBindable)
                .map(ConfigurationSetting::frontMatterKey)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private static boolean isBindable(final ConfigurationSetting setting) {
        return setting.immutableMethods().stream()
                .flatMap(method -> method.annotations().stream())
                .noneMatch(annotation -> JSON_IGNORE.equals(annotation.type().toString()));
    }

    private static final String JSON_IGNORE = "com.fasterxml.jackson.annotation.JsonIgnore";

    @Test
    @DisplayName("lists nothing a statement cannot be bound from")
    void listsOnlyBoundSettings() {
        final var unbindable = new TreeSet<>(tagged());
        unbindable.removeAll(bound());
        Assertions.assertTrue(unbindable.isEmpty(),
                () -> "the schema offers " + unbindable + ", which SqlConfiguration has no property "
                        + "for — front matter using them fails with UnrecognizedPropertyException");
    }

    /**
     * Every repository setting a statement may override says its own wire key.
     *
     * <p>The annotation is what Jackson binds by, and it is added in one place for all of them. That
     * place used to write it onto the setting, whose annotation lists nothing reads, so it reached
     * no generated method at all — invisible while every key equals its setting's name, and a
     * statement bound from a name Jackson never saw the moment one does not.</p>
     */
    @Test
    @DisplayName("names the wire key of every repository setting a statement may override")
    void annotatesInheritedSettings() {
        final var inherited = Repositories.booleanMethods().stream()
                .map(ConfigurationSetting::name)
                .collect(Collectors.toCollection(TreeSet::new));
        final var unannotated = Sql.configurationGroup().settings().stream()
                .filter(setting -> inherited.contains(setting.name()))
                .filter(setting -> setting.immutableMethods().stream()
                        .flatMap(method -> method.annotations().stream())
                        .noneMatch(annotation ->
                                ConfigurationSetting.JSON_PROPERTY.equals(annotation.type().toString())))
                .map(ConfigurationSetting::name)
                .collect(Collectors.toCollection(TreeSet::new));

        Assertions.assertTrue(unannotated.isEmpty(),
                () -> unannotated + " reach SqlConfiguration without a JsonProperty naming the key "
                        + "they are bound from");
    }

    @Test
    @DisplayName("lists everything a statement can be bound from")
    void listsEveryBoundSetting() {
        final var missing = new TreeSet<>(bound());
        missing.removeAll(tagged());
        Assertions.assertTrue(missing.isEmpty(),
                () -> "the schema omits " + missing + " while the parser accepts them, and it sets "
                        + "additionalProperties: false — so an editor flags valid front matter");
    }

}
