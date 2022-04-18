/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator.website;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

/**
 * Generates markdown documentation for {@link ConfigurationGroup}s.
 */
public final class MarkdownGenerator {

    private static final Function<String, String> LOWER_CASE = Strings::lowerCase;
    private static final Function<String, String> UPPER_CASE = Strings::upperCase;
    private static final Function<String, String> KEBAB_CASE = Strings::kebabCase;

    private final Mustache groupTemplate;
    private final Mustache settingTemplate;
    private final String yosqlVersion;

    public MarkdownGenerator(final MustacheFactory mustacheFactory, final String yosqlVersion) {
        groupTemplate = mustacheFactory.compile("configurationGroup.md");
        settingTemplate = mustacheFactory.compile("configurationSetting.md");
        this.yosqlVersion = yosqlVersion;
    }

    public String group(final ConfigurationGroup group) {
        return applyTemplate(groupTemplate, Map.of(
                "group", group,
                "yosqlVersion", yosqlVersion,
                "currentDate", LocalDate.now().toString(),
                "lower", LOWER_CASE,
                "upper", UPPER_CASE,
                "kebab", KEBAB_CASE
        ));
    }

    public String setting(final ConfigurationGroup group, final ConfigurationSetting setting) {
        final var relatedSettings = group.settings().stream()
                .filter(s -> !s.name().equals(setting.name()))
                .sorted(Comparator.comparing(ConfigurationSetting::name))
                .toList();
        return applyTemplate(settingTemplate, Map.of(
                "group", group,
                "setting", setting,
                "yosqlVersion", yosqlVersion,
                "currentDate", LocalDate.now().toString(),
                "lower", LOWER_CASE,
                "upper", UPPER_CASE,
                "kebab", KEBAB_CASE,
                "relatedSettings", relatedSettings,
                "hasRelatedSettings", !relatedSettings.isEmpty(),
                "hasExplanation", setting.explanation().isPresent()
        ));
    }

    private String applyTemplate(final Mustache template, final Map<String, Object> scopes) {
        try {
            final var writer = new StringWriter();
            template.execute(writer, scopes).flush();
            return writer.toString();
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
