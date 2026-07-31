/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--@yosql sqlStatementSeparator: ;;

CREATE ALIAS getVersion AS '
String version() {
    return "1.0.0";
}
';;
