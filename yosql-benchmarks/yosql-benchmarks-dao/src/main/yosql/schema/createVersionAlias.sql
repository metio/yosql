/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

--@yosql sqlStatementSeparator: ;;

CREATE ALIAS getVersion AS '
String version() {
    return "1.0.0";
}
';;
