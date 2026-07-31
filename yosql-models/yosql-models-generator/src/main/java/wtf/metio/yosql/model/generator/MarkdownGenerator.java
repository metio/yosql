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
        scopes.put("frontMatterExampleCode", setting.frontMatterExampleCode().orElse("configValue"));
        scopes.put("yosqlVersion", yosqlVersion);
        scopes.put("currentDate", LocalDate.now(ZoneOffset.UTC).toString());
        scopes.put("lower", LOWER_CASE);
        scopes.put("upper", UPPER_CASE);
        scopes.put("kebab", KEBAB_CASE);
        scopes.put("relatedSettings", relatedSettings);
        scopes.put("hasRelatedSettings", !relatedSettings.isEmpty());
        scopes.put("hasExplanation", setting.explanation().isPresent());
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
