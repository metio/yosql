/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli.parser;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import joptsimple.HelpFormatter;
import joptsimple.OptionDescriptor;

/**
 * Customized help formatter that uses an {@link AsciiTable} to render its output.
 */
public final class YoSqlHelpFormatter implements HelpFormatter {

    private static final Set<String> IGNORED_OPTIONS = Stream
            .of("[arguments]") //$NON-NLS-1$
            .collect(Collectors.toSet());

    @Override
    public String format(final Map<String, ? extends OptionDescriptor> options) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 30, 70, 20 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow("Name", "Description", "Defaults");
        for (final Entry<String, ? extends OptionDescriptor> entry : options.entrySet()) {
            final String parameterName = entry.getKey();
            if (!IGNORED_OPTIONS.contains(parameterName)) {
                at.addRule();
                final OptionDescriptor value = entry.getValue();
                at.addRow("--" + parameterName, value.argumentDescription(), value.defaultValues().stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        // XXX: have to filter because AsciiTable runs into an infinite loop otherwise
                        .filter(string -> !"\u0000".equals(string))
                        .collect(Collectors.joining(", ")));
            }
        }
        at.addRule();

        return "\n" + at.render() + "\n";
    }

}
