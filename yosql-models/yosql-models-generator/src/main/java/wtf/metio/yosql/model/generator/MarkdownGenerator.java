/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.model.generator;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Generates markdown documentation for {@link ConfigurationGroup}s.
 */
final class MarkdownGenerator {

    private static final Function<String, String> LOWER_CASE = Strings::lowerCase;
    private static final Function<String, String> UPPER_CASE = Strings::upperCase;
    private static final Function<String, String> KEBAB_CASE = Strings::kebabCase;

    private final Mustache groupTemplate;
    private final Mustache settingTemplate;
    private final String yosqlVersion;

    MarkdownGenerator(
            final MustacheFactory mustacheFactory,
            final String yosqlVersion,
            final String settingTemplateName) {
        groupTemplate = mustacheFactory.compile("configurationGroup.md");
        settingTemplate = mustacheFactory.compile(settingTemplateName);
        this.yosqlVersion = yosqlVersion;
    }

    public String group(final ConfigurationGroup group) {
        return applyTemplate(groupTemplate, Map.of(
                "group", group,
                "yosqlVersion", yosqlVersion,
                // UTC, because Hugo compares a page's date against the current instant in UTC
                // and drops anything dated later. A date-only stamp taken in a zone ahead of
                // UTC is tomorrow midnight UTC, which silently removes every generated page
                // from the site for as many hours as the offset.
                "currentDate", LocalDate.now(ZoneOffset.UTC).toString(),
                "lower", LOWER_CASE,
                "upper", UPPER_CASE,
                "kebab", KEBAB_CASE,
                "hasExplanation", group.explanation().isPresent()
        ));
    }

    public String setting(final ConfigurationGroup group, final ConfigurationSetting setting) {
        final var relatedSettings = group.settings().stream()
                .filter(ConfigurationSetting::generateDocs)
                .filter(s -> !s.name().equals(setting.name()))
                .sorted(Comparator.comparing(ConfigurationSetting::name))
                .toList();
        final var scopes = new HashMap<String, Object>();
        scopes.put("group", group);
        scopes.put("setting", setting);
        scopes.put("frontMatterExampleCode", setting.frontMatterExample());
        scopes.put("frontMatterKey", setting.frontMatterKey());
        scopes.put("yosqlVersion", yosqlVersion);
        scopes.put("currentDate", LocalDate.now(ZoneOffset.UTC).toString());
        scopes.put("lower", LOWER_CASE);
        scopes.put("upper", UPPER_CASE);
        scopes.put("kebab", KEBAB_CASE);
        // One line of links rather than one bullet per sibling with its description repeated: the
        // group's own index page already carries the descriptions, and repeating them on all of a
        // group's pages buries whatever the page is actually about.
        scopes.put("relatedSettingsLine", relatedSettings.stream()
                .map(related -> "[%s](../%s/)".formatted(related.name(), Strings.lowerCase(related.name())))
                .collect(Collectors.joining(", ")));
        scopes.put("hasRelatedSettings", !relatedSettings.isEmpty());
        scopes.put("hasExplanation", setting.explanation().isPresent());
        // A heading with nothing under it reads as a gap in the documentation rather than as a
        // setting that takes any value at all, so the section only appears once there is one.
        scopes.put("hasExamples", !setting.examples().isEmpty());
        return applyTemplate(settingTemplate, scopes);
    }

    private static String applyTemplate(final Mustache template, final Map<String, Object> scopes) {
        try {
            final var writer = new StringWriter();
            template.execute(writer, scopes).flush();
            return writer.toString();
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
