/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

--@yosql sqlStatementSeparator: ;;

-- name: createNextPrimeFunction
CREATE ALIAS NEXT_PRIME AS '
String nextPrime(String value) {
    return new BigInteger(value).nextProbablePrime().toString();
}
';;

-- name: createRandomNumberFunction
CREATE ALIAS RANDOM_NUMBER AS '
int randomNumber() {
    return 4;
}
';;

-- name: createNamesFunction
CREATE ALIAS NAMES AS '
List<String> names() {
    return List.of("alice", "bob");
}
';;
