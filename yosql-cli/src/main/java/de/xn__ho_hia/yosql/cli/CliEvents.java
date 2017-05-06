package de.xn__ho_hia.yosql.cli;

import ch.qos.cal10n.BaseName;

@BaseName("cli-events")
enum CliEvents {

    PROBLEM_DURING_OPTION_PARSING,

    HELP_REQUIRED,

    DETAIL_HELP_REQUIRED,

    UNKNOWN_COMMAND,

    HELP_FOR_VERSION,

    HELP_FOR_GENERATE,

    INFORMATION_NEEDED;

}
