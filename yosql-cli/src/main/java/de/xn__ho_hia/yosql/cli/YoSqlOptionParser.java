package de.xn__ho_hia.yosql.cli;

import joptsimple.OptionParser;
import joptsimple.OptionSpec;

class YoSqlOptionParser {

    final OptionParser    parser;
    final OptionSpec<?>[] options;

    public YoSqlOptionParser(
            final OptionParser parser,
            final OptionSpec<?>... options) {
        this.parser = parser;
        this.options = options;
    }

}
