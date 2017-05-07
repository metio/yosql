package de.xn__ho_hia.yosql.cli;

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

class YoSqlHelpFormatter implements HelpFormatter {

    private static final Set<String> IGNORED_OPTIONS = Stream
            .of("[arguments]") //$NON-NLS-1$
            .collect(Collectors.toSet());

    @Override
    @SuppressWarnings("nls")
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

        return at.render() + "\n";
    }

}
