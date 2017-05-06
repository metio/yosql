package de.xn__ho_hia.yosql.cli;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import joptsimple.HelpFormatter;
import joptsimple.OptionDescriptor;

class YoSqlHelpFormatter implements HelpFormatter {

    @Override
    @SuppressWarnings("nls")
    public String format(final Map<String, ? extends OptionDescriptor> options) {
        final AsciiTable at = new AsciiTable();
        at.setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 30, 70, 20 }));
        at.getContext().setGridTheme(TA_GridThemes.FULL);

        at.addRule();
        at.addRow("NAME", "DESCRIPTION", "DEFAULTS");
        for (final Entry<String, ? extends OptionDescriptor> entry : options.entrySet()) {
            final String parameterName = entry.getKey();
            if (!parameterName.contains("arguments") && !"help".equalsIgnoreCase(parameterName)) {
                at.addRule();
                final OptionDescriptor value = entry.getValue();
                at.addRow(parameterName, value.argumentDescription(), value.defaultValues().stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .filter(string -> !"\u0000".equals(string))
                        .collect(Collectors.joining(", ")));
            }
        }
        at.addRule();

        return at.render() + "\n";
    }

}
